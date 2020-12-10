package com.bkt.contract.ba.sdk

import android.annotation.SuppressLint
import android.util.Log
import com.bkt.contract.ba.common.TickerDtoToPairInfoPoListFunction
import com.bkt.contract.ba.enums.SocketEvent
import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.event.KLineSEvent
import com.bkt.contract.ba.model.event.SocketRequestBody
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.service.inner.DepthDbService
import com.bkt.contract.ba.service.inner.PairDbService
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.websocket.WsManager
import com.xxf.arch.websocket.listener.WsStatusListener
import io.reactivex.Observable
import io.reactivex.ObservableSource
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
    protected abstract fun getWsManager(): WsManager;

    /**
     * 订阅socket event
     */
    private fun subEvent(event: SocketEvent, body: SocketRequestBody) {
        /**
         * 订阅事件一定要buffer 便于框架重连
         */
        buffer.put(event, body);
        log(" send msg:" + body);
        if (this.getWsManager() != null && this.getWsManager().isWsConnected) {
            this.getWsManager().sendMessage(JsonUtils.toJsonString(body))
        }
    }

    /**
     * 取消订阅socket
     */
    private fun unSubEvent(event: SocketEvent, body: SocketRequestBody) {
        buffer.remove(event);
        if (this.getWsManager() != null && this.getWsManager().isWsConnected) {
            this.getWsManager().sendMessage(JsonUtils.toJsonString(body))
        }
    }

    /**
     * 订阅ticker事件
     */
    fun subTicker(): Observable<List<TickerEventDto>> {
        return bus.ofType(TickerEventWrapper::class.java)
                .map { it.list }
                .doOnSubscribe {
                    subEvent(SocketEvent.MiniTicker24hr, SocketRequestBody.subscribeBody(listOf("!miniTicker@arr")))
                }.doOnDispose {
                    unSubEvent(SocketEvent.MiniTicker24hr, SocketRequestBody.unSubscribeBody(listOf("!miniTicker@arr")))
                };
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
    fun subKLine(symbol: String, interval: String): Observable<KLineSEvent> {
        return bus.ofType(KLineSEvent::class.java)
                .doOnSubscribe {
                    // subEvent(SocketEvent.KLine, null)
                }.doOnDispose {
                    // unSubEvent(SocketEvent.KLine)
                };
    }

    /**
     * 订阅深度
     * @param symbol
     * @param updateTime 更新间隔ms
     */
    fun subDepth(symbol: String, updateTime: Long = 100): Observable<DepthEventDtoPo> {
        return bus.ofType(DepthEventDtoPo::class.java)
                .doOnSubscribe {
                    subEvent(SocketEvent.DepthUpdate, SocketRequestBody.subscribeBody(listOf(String.format("%s@depth@%sms", symbol.toLowerCase(), updateTime))));
                }.doOnDispose {
                    unSubEvent(SocketEvent.DepthUpdate, SocketRequestBody.subscribeBody(listOf(String.format("%s@depth@%sms", symbol.toLowerCase(), updateTime))));
                };
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
                                    SocketEvent.KLine.value -> JsonUtils.toBean(text, KLineSEvent::class.java);
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
                                SocketEvent.KLine.value -> JsonUtils.toBean(text, KLineSEvent::class.java);
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

    override fun onOpen(response: Response?) {
        log("onOpen:" + response!!.request.url + " code:" + response.code + " msg:" + response.message)
        try {
            /**
             * 网络连接好后 需要重新将事件发出去
             */
            val temp: LinkedHashMap<SocketEvent, SocketRequestBody> = linkedMapOf();
            temp.putAll(buffer);
            temp.forEach {
                subEvent(it.key, it.value);
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
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