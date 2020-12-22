package com.bkt.contract.ba.sdk

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import com.bkt.contract.ba.common.TickerDtoToPairInfoPoListFunction
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.enums.SocketEvent
import com.bkt.contract.ba.model.dto.*
import com.bkt.contract.ba.model.event.*
import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.model.po.PairTradePo
import com.bkt.contract.ba.service.inner.DepthDbService
import com.bkt.contract.ba.service.inner.PairDbService
import com.bkt.contract.ba.service.inner.TradeDbService
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.websocket.WsManager
import com.xxf.arch.websocket.listener.WsStatusListener
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.Response
import okio.ByteString
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Callable

/**
 * @Description: ba Proxy socket api
 * @Author: XGod
 * @CreateDate: 2020/12/2 20:45
 */
abstract class ContractProxySocketService : WsStatusListener {
    private val bus: Subject<Any> = PublishSubject.create<Any>().toSerialized();
    private val buffer: LinkedHashMap<SocketEvent, SocketRequestBody> = linkedMapOf();

    class TickerEventWrapper(val list: List<TickerEventDto>);

    /**
     * 获取web socket manger对象
     */
    protected abstract fun getWsManager(): Observable<WsManager>;

    /**
     * 订阅socket event
     */
    private fun subEvent(event: SocketEvent, body: SocketRequestBody, wsManager: WsManager) {
        /**
         * 订阅事件一定要buffer 便于框架重连
         */
        buffer.put(event, body);
        log(" send msg:" + body);
        if (wsManager != null && wsManager.isWsConnected) {
            wsManager.sendMessage(JsonUtils.toJsonString(body))
        }
    }

    /**
     * 取消订阅socket
     */
    private fun unSubEvent(event: SocketEvent, body: SocketRequestBody, wsManager: WsManager) {
        buffer.remove(event);
        log(" send msg:" + body);
        if (wsManager != null && wsManager.isWsConnected) {
            wsManager.sendMessage(JsonUtils.toJsonString(body))
        }
    }

    /**
     * 订阅ticker事件
     *  @param applyDispose 是否跟随取消socket
     */
    fun subTicker(applyDispose: Boolean = true): Observable<List<TickerEventDto>> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<List<TickerEventDto>>> {
            override fun apply(t: WsManager): ObservableSource<List<TickerEventDto>> {
                return bus.ofType(TickerEventWrapper::class.java)
                        .map { it.list }
                        .doOnSubscribe {
                            subEvent(SocketEvent.MiniTicker24hr, SocketRequestBody.subscribeBody(listOf("!miniTicker@arr")), t)
                        }.doOnDispose {
                            if (applyDispose) {
                                unSubEvent(SocketEvent.MiniTicker24hr, SocketRequestBody.unSubscribeBody(listOf("!miniTicker@arr")), t)
                            }
                        };
            }
        });

    }


    /**
     * 订阅K线
     * @param interval  m -> 分钟; h -> 小时; d -> 天; w -> 周; M -> 月
     * 1m
     * 3m
     * 5m
     * 15m
     * 30m
     * 1h
     * 2h
     * 4h
     * 6h
     * 8h
     * 12h
     * 1d
     * 3d
     * 1w
     * 1M
     */
    fun subKLine(symbol: String, interval: String): Observable<KLineEventDto> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<KLineEventDto>> {
            override fun apply(t: WsManager): ObservableSource<KLineEventDto> {
                return bus.ofType(KLineSEvent::class.java)
                        .doOnSubscribe {
                            subEvent(SocketEvent.KLine, SocketRequestBody.subscribeBody(listOf(String.format("%s@kline_%s", symbol, interval))), t)
                        }.doOnDispose {
                            unSubEvent(SocketEvent.KLine, SocketRequestBody.unSubscribeBody(listOf(String.format("%s@kline_%s", symbol, interval))), t)
                        }
                        .filter { TextUtils.equals(it.s, symbol) }
                        .map(object : Function<KLineSEvent, KLineEventDto> {
                            override fun apply(t: KLineSEvent): KLineEventDto {
                                return t.k;
                            }
                        });
            }
        });

    }

    /**
     * 订阅深度 全量
     * @param symbol
     * @param levels
     * @param updateTime 更新间隔ms
     */
    fun subDepth(symbol: String, levels: Int = 20, updateTime: Long = 100): Observable<DepthEventDtoPo> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<DepthEventDtoPo>> {
            override fun apply(t: WsManager): ObservableSource<DepthEventDtoPo> {
                return bus.ofType(DepthEventDtoPo::class.java)
                        .doOnSubscribe {
                            subEvent(SocketEvent.DepthUpdate, SocketRequestBody.subscribeBody(listOf(String.format("%s@depth%s@%sms", symbol.toLowerCase(), levels, updateTime))), t);
                        }.doOnDispose {
                            unSubEvent(SocketEvent.DepthUpdate, SocketRequestBody.unSubscribeBody(listOf(String.format("%s@depth%s@%sms", symbol.toLowerCase(), levels, updateTime))), t);
                        }.filter { TextUtils.equals(it.symbol, symbol) };
            }
        });
    }

    /**
     * 订阅成交  增量
     */
    fun subTrade(symbol: String): Observable<TradeEventDto> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<TradeEventDto>> {
            override fun apply(t: WsManager): ObservableSource<TradeEventDto> {
                return bus.ofType(TradeEventDto::class.java)
                        .doOnSubscribe {
                            subEvent(SocketEvent.DepthUpdate, SocketRequestBody.subscribeBody(listOf(String.format("%s@aggTrade", symbol.toLowerCase()))), t);
                        }.doOnDispose {
                            unSubEvent(SocketEvent.DepthUpdate, SocketRequestBody.unSubscribeBody(listOf(String.format("%s@aggTrade", symbol.toLowerCase()))), t);
                        }.filter { TextUtils.equals(it.symbol, symbol) };
            }
        });
    }

    /**
     * 订阅市价 和汇率结算
     */
    fun subMarkPrice(symbol: String): Observable<PremiumIndexPriceDto> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<PremiumIndexPriceDto>> {
            override fun apply(t: WsManager): ObservableSource<PremiumIndexPriceDto> {
                return bus.ofType(PremiumIndexPriceDto::class.java)
                        .doOnSubscribe {
                            subEvent(SocketEvent.DepthUpdate, SocketRequestBody.subscribeBody(listOf(String.format("%s@markPrice", symbol.toLowerCase()))), t);
                        }.doOnDispose {
                            unSubEvent(SocketEvent.DepthUpdate, SocketRequestBody.unSubscribeBody(listOf(String.format("%s@markPrice", symbol.toLowerCase()))), t);
                        }.filter { TextUtils.equals(it.symbol, symbol) };
            }
        });
    }

    /**
     * 订阅指数价
     * 注意 只有usd有
     * ustd 并没有该socket
     */
    fun subIndexPrice(symbol: String): Observable<IndexPriceEvent> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<IndexPriceEvent>> {
            override fun apply(t: WsManager): ObservableSource<IndexPriceEvent> {
                return bus.ofType(IndexPriceEvent::class.java)
                        .doOnSubscribe {
                            subEvent(SocketEvent.DepthUpdate, SocketRequestBody.subscribeBody(listOf(String.format("%s@indexPrice", symbol.toLowerCase()))), t);
                        }.doOnDispose {
                            unSubEvent(SocketEvent.DepthUpdate, SocketRequestBody.unSubscribeBody(listOf(String.format("%s@indexPrice", symbol.toLowerCase()))), t);
                        }.filter { TextUtils.equals(it.symbol, symbol) };
            }
        });
    }

    /**
     * 订阅订单变化事件
     */
    fun subOrderChange(type: ContractType): Observable<OrderUpdateEvent> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<OrderUpdateEvent>> {
            override fun apply(t: WsManager): ObservableSource<OrderUpdateEvent> {
                return bus.ofType(OrderUpdateEvent::class.java)
                        .filter {
                            it.order != null
                                    && it.orderEventType != null
                                    && type == it.order.getPairConfig()?.contractType;
                        }
            }
        });
    }

    /**
     * Balance和Position更新推送
     */
    fun subAccountUpdate(): Observable<AccountUpdateEvent.AccountChangeInfo> {
        return getWsManager().flatMap(object : Function<WsManager, ObservableSource<AccountUpdateEvent.AccountChangeInfo>> {
            override fun apply(t: WsManager): ObservableSource<AccountUpdateEvent.AccountChangeInfo> {
                return bus.ofType(AccountUpdateEvent::class.java)
                        .filter { it.account != null }
                        .map { it.account }
            }
        });
    }


    override fun onMessage(text: String?) {
        log("onMessage:$text")
        Observable
                .fromCallable(object : Callable<Boolean> {
                    override fun call(): Boolean {
                        var jsonObject: JSONObject? = null;
                        try {
                            jsonObject = JSONObject(text);
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                        if (jsonObject == null) {
                            val jsonArray = JSONArray(text);
                            val firstElement: JSONObject = jsonArray.getJSONObject(0);
                            if (firstElement.has("e")) {
                                val event = firstElement.getString("e");
                                when (event) {
                                    SocketEvent.MiniTicker24hr.value -> {
                                        val toBeanList = JsonUtils.toBeanList(jsonArray.toString(), TickerEventDto::class.java);
                                        cacheMiniTicker24hr(toBeanList);
                                        bus.onNext(TickerEventWrapper(toBeanList));
                                    }
                                    SocketEvent.DepthUpdate.value -> {
                                        val toBean = JsonUtils.toBeanList(text, DepthEventDtoPo::class.java).get(0);
                                        cacheDepth(toBean)
                                        bus.onNext(toBean);
                                    }
                                    SocketEvent.AggTrade.value -> {
                                        val toBean = JsonUtils.toBeanList(text, TradeEventDto::class.java).get(0);
                                        cacheTrade(toBean);
                                        bus.onNext(toBean);
                                    }
                                    SocketEvent.MarkPriceUpdate.value -> {
                                        val toBean = JsonUtils.toBeanList(text, PremiumIndexPriceDto::class.java).get(0);
                                        bus.onNext(toBean);
                                    }
                                    SocketEvent.IndexPriceUpdate.value -> {
                                        val toBean = JsonUtils.toBeanList(text, IndexPriceEvent::class.java).get(0);
                                        bus.onNext(toBean);
                                    }
                                    SocketEvent.KLine.value -> {
                                        val toBean = JsonUtils.toBeanList(text, KLineSEvent::class.java).get(0);
                                        bus.onNext(toBean);
                                    }
                                    SocketEvent.ORDER_TRADE_UPDATE.value -> {
                                        val toBean = JsonUtils.toBeanList(text, OrderUpdateEvent::class.java).get(0);
                                        bus.onNext(toBean);
                                    }
                                    SocketEvent.ACCOUNT_UPDATE.value -> {
                                        val toBean = JsonUtils.toBeanList(text, AccountUpdateEvent::class.java).get(0);
                                        bus.onNext(toBean);
                                    }
                                    else -> throw  RuntimeException("not support event:" + event);
                                }
                            }
                        } else if (jsonObject.has("e")) {
                            val event = jsonObject.getString("e");
                            when (event) {
                                SocketEvent.MiniTicker24hr.value -> {
                                    val listOf = listOf(JsonUtils.toBean(jsonObject.toString(), TickerEventDto::class.java));
                                    cacheMiniTicker24hr(listOf);
                                    bus.onNext(TickerEventWrapper(listOf));
                                };
                                SocketEvent.DepthUpdate.value -> {
                                    val toBean = JsonUtils.toBean(text, DepthEventDtoPo::class.java);
                                    cacheDepth(toBean)
                                    bus.onNext(toBean);
                                }
                                SocketEvent.AggTrade.value -> {
                                    val toBean = JsonUtils.toBean(text, TradeEventDto::class.java);
                                    cacheTrade(toBean)
                                    bus.onNext(toBean);
                                }
                                SocketEvent.MarkPriceUpdate.value -> {
                                    val toBean = JsonUtils.toBean(text, PremiumIndexPriceDto::class.java);
                                    bus.onNext(toBean);
                                }
                                SocketEvent.IndexPriceUpdate.value -> {
                                    val toBean = JsonUtils.toBean(text, IndexPriceEvent::class.java);
                                    bus.onNext(toBean);
                                }
                                SocketEvent.KLine.value -> {
                                    val toBean = JsonUtils.toBean(text, KLineSEvent::class.java)
                                    bus.onNext(toBean);
                                }
                                SocketEvent.ORDER_TRADE_UPDATE.value -> {
                                    val toBean = JsonUtils.toBean(text, OrderUpdateEvent::class.java);
                                    bus.onNext(toBean);
                                }
                                SocketEvent.ACCOUNT_UPDATE.value -> {
                                    val toBean = JsonUtils.toBean(text, AccountUpdateEvent::class.java);
                                    bus.onNext(toBean);
                                }
                                else -> throw  RuntimeException("not support event:" + event);
                            }
                        }
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnError {
                    log("parse ex:" + it);
                }
                .subscribe();
    }

    @SuppressLint("CheckResult")
    private fun cacheMiniTicker24hr(list: List<TickerEventDto>) {
        Observable.just(list)
                .map(TickerDtoToPairInfoPoListFunction())
                .flatMap(object : Function<List<PairInfoPo>, ObservableSource<List<PairInfoPo>>> {
                    override fun apply(t: List<PairInfoPo>): ObservableSource<List<PairInfoPo>> {
                        return PairDbService.insertOrUpdate(t);
                    }
                }).blockingFirst();
    }

    @SuppressLint("CheckResult")
    private fun cacheDepth(depth: DepthEventDtoPo) {
        DepthDbService.insertOrUpdate(depth)
                .blockingFirst();
    }

    @SuppressLint("CheckResult")
    private fun cacheTrade(trade: TradeEventDto) {
        Observable.just(trade)
                .map(object : Function<TradeEventDto, PairTradePo> {
                    override fun apply(t: TradeEventDto): PairTradePo {
                        return PairTradePo(t.symbol, listOf(t));
                    }
                }).flatMap(object : Function<PairTradePo, ObservableSource<PairTradePo>> {
                    override fun apply(t: PairTradePo): ObservableSource<PairTradePo> {
                        return TradeDbService.insertOrUpdate(t);
                    }
                }).blockingFirst();
    }

    private fun log(log: String) {
        XXF.getLogger().d("==========>WebSocket" + log)
    }

    override fun onMessage(bytes: ByteString?) {
        log("onMessage:bytes:" + (bytes?.size
                ?: " null"))
    }

    override fun onClosed(code: Int, reason: String?) {
        log("onClosed:code:$code reason:$reason")
    }

    @SuppressLint("CheckResult")
    override fun onOpen(response: Response?) {
        log("onOpen:" + response!!.request.url + " code:" + response.code + " msg:" + response.message)
        /**
         * 网络连接好后 需要重新将事件发出去
         */
        getWsManager()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<WsManager> {
                    override fun accept(t: WsManager) {
                        val temp: LinkedHashMap<SocketEvent, SocketRequestBody> = linkedMapOf();
                        temp.putAll(buffer);
                        temp.forEach {
                            subEvent(it.key, it.value, t);
                        }
                    }
                })
    }

    override fun onFailure(t: Throwable?, response: Response?) {
        log("onFailure:" + "throwable:" + Log.getStackTraceString(t))
    }

    override fun onReconnect() {
        log("onReconnect:")
    }

    override fun onClosing(code: Int, reason: String?) {
        log("onClosing:code:$code reason:$reason")
    }
}