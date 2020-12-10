package com.bkt.contract.ba.sdk

import android.util.Log
import com.bkt.contract.ba.enums.SocketEvent
import com.bkt.contract.ba.model.dto.DepthEventDto
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.event.BaseSEvent
import com.bkt.contract.ba.model.event.KLineSEvent
import com.bkt.contract.ba.model.event.SocketRequestBody
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.websocket.WsManager
import com.xxf.arch.websocket.listener.WsStatusListener
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.Response
import okio.ByteString
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
     */
    fun subDepth(symbol: String): Observable<DepthEventDto> {
        return bus.ofType(DepthEventDto::class.java)
                .doOnSubscribe {
                    //subEvent(SocketEvent.DepthUpdate, null)
                }.doOnDispose {
                    //  unSubEvent(SocketEvent.DepthUpdate)
                };
    }


    override fun onMessage(text: String?) {
        log("onMessage:$text")
        Observable
                .fromCallable(object : Callable<BaseSEvent> {
                    override fun call(): BaseSEvent {
                        val baseResEvent = JsonUtils.toBean(text, BaseSEvent::class.java);
                        if (baseResEvent != null) {
                            val result = when (baseResEvent.e) {
                                SocketEvent.MiniTicker24hr -> TickerEventWrapper(JsonUtils.toBeanList(text, TickerEventDto::class.java));
                                SocketEvent.KLine -> JsonUtils.toBean(text, KLineSEvent::class.java);
                                SocketEvent.DepthUpdate -> JsonUtils.toBean(text, DepthEventDto::class.java);
                                else -> throw  RuntimeException("not support event:" + baseResEvent.e);
                            }
                            if (result != null) {
                                bus.onNext(result);
                            }
                        }
                        return baseResEvent;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
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