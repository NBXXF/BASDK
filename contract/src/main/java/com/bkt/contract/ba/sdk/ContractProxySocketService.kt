package com.bkt.contract.ba.sdk

import com.bkt.contract.ba.enums.SocketEvent
import com.bkt.contract.ba.model.dto.DepthEventDto
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.event.BaseSEvent
import com.bkt.contract.ba.model.event.KLineSEvent
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.websocket.WsManager
import com.xxf.arch.websocket.listener.WsStatusListener
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.Response
import okio.ByteString
import java.util.concurrent.Callable
import kotlin.collections.LinkedHashMap

/**
 * @Description: ba Proxy socket api
 * @Author: XGod
 * @CreateDate: 2020/12/2 20:45
 */
abstract class ContractProxySocketService : WsStatusListener {
    private val bus: Subject<Any> = PublishSubject.create<Any>().toSerialized();
    private val buffer: LinkedHashMap<SocketEvent, Any?> = linkedMapOf();


    /**
     * 获取web socket manger对象
     */
    protected abstract fun getWsManager(): WsManager;

    /**
     * 订阅socket event
     */
    fun subEvent(event: SocketEvent, stream: Any?) {
        /**
         * 订阅事件一定要buffer 便于框架重连
         */
        buffer.put(event, stream);
        if (this.getWsManager() != null && this.getWsManager().isWsConnected) {
            //TODO 发送事件
        }
    }

    /**
     * 取消订阅socket
     */
    fun unSubEvent(event: SocketEvent) {
        buffer.remove(event);
        if (this.getWsManager() != null && this.getWsManager().isWsConnected) {
            //TODO 发送事件
        }
    }

    /**
     * 订阅ticker事件
     */
    fun subTicker(vararg pairs: String): Observable<TickerEventDto> {
        return bus.ofType(TickerEventDto::class.java)
                .doOnSubscribe {
                    subEvent(SocketEvent.MiniTicker24hr, null)
                }.doOnDispose {
                    unSubEvent(SocketEvent.MiniTicker24hr)
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
                    subEvent(SocketEvent.KLine, null)
                }.doOnDispose {
                    unSubEvent(SocketEvent.KLine)
                };
    }

    /**
     * 订阅深度
     */
    fun subDepth(symbol: String): Observable<DepthEventDto> {
        return bus.ofType(DepthEventDto::class.java)
                .doOnSubscribe {
                    subEvent(SocketEvent.DepthUpdate, null)
                }.doOnDispose {
                    unSubEvent(SocketEvent.DepthUpdate)
                };
    }


    override fun onMessage(text: String?) {
        Observable
                .fromCallable(object : Callable<BaseSEvent> {
                    override fun call(): BaseSEvent {
                        val baseResEvent = JsonUtils.toBean(text, BaseSEvent::class.java);
                        if (baseResEvent != null) {
                            val result = when (baseResEvent.e) {
                                SocketEvent.MiniTicker24hr -> JsonUtils.toBean(text, TickerEventDto::class.java);
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

    override fun onMessage(bytes: ByteString?) {
        TODO("Not yet implemented")
    }

    override fun onClosed(code: Int, reason: String?) {
        TODO("Not yet implemented")
    }

    override fun onOpen(response: Response?) {
        try {
            /**
             * 网络连接好后 需要重新将事件发出去
             */
            val temp: LinkedHashMap<SocketEvent, Any?> = linkedMapOf();
            temp.putAll(buffer);
            temp.forEach {
                subEvent(it.key, it.value);
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onFailure(t: Throwable?, response: Response?) {
        TODO("Not yet implemented")
    }

    override fun onReconnect() {
        TODO("Not yet implemented")
    }

    override fun onClosing(code: Int, reason: String?) {
        TODO("Not yet implemented")
    }
}