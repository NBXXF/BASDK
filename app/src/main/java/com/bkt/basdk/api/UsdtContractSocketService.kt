package com.bkt.basdk.api

import android.text.TextUtils
import com.bkt.contract.ba.sdk.ContractProxySocketService
import com.xxf.arch.XXF
import com.xxf.arch.http.OkHttpClientBuilder
import com.xxf.arch.websocket.WsManager
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * @Description: USDT 精本位合约socket
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/9 19:36
 */
class UsdtContractSocketService private constructor() : ContractProxySocketService() {
    companion object {
        val instance: UsdtContractSocketService by lazy {
            UsdtContractSocketService();
        }
    }

    val wssUrl: String
        get() {
            /**
             * 动态设置 支持域名切换
             */
            return "wss://stream.binancefuture.com/ws"
        };
    var mWsManager: WsManager? = null;
    override fun getWsManager(): Observable<WsManager> {
        return Observable.fromCallable(object : Callable<WsManager> {
            override fun call(): WsManager {
                if (mWsManager != null && TextUtils.equals(mWsManager?.tag(), wssUrl)) {
                    return mWsManager!!;
                }
                mWsManager = WsManager.Builder(XXF.getApplication())
                        .wsUrl(wssUrl)
                        .tag(wssUrl)
                        .needReconnect(true)
                        .client(OkHttpClientBuilder()
                                //  .addInterceptor(BhHttpHeaderInterceptor())
                                //.addInterceptor(BktHttpLoggerInterceptor())
                                .build()
                                .newBuilder()
                                //.cookieJar(BhHttpCookieJar())
                                .pingInterval(8, TimeUnit.SECONDS)
                                .build())
                        .build()
                mWsManager!!.setWsStatusListener(this@UsdtContractSocketService);
                mWsManager!!.startConnect()
                return mWsManager!!;
            }
        }).subscribeOn(Schedulers.io());
    }
}