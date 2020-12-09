package com.bkt.basdk.api

import android.text.TextUtils
import com.bkt.contract.ba.sdk.ContractProxySocketService
import com.xxf.arch.XXF
import com.xxf.arch.http.OkHttpClientBuilder
import com.xxf.arch.websocket.WsManager
import java.util.concurrent.TimeUnit

/**
 * @Description: USDT 币本位合约socket
 * @Author: XGod
 * @CreateDate: 2020/12/9 19:36
 */
class UsdContractSocketService private constructor() : ContractProxySocketService() {
    companion object {
        val instance: UsdContractSocketService by lazy {
            UsdContractSocketService();
        }
    }

    val wssUrl: String
        get() {
            /**
             * 动态设置 支持域名切换
             */
            return "wss://dstream.binancefuture.com"
        };
    var mWsManager: WsManager? = null;
    override fun getWsManager(): WsManager {
        if (mWsManager == null || !TextUtils.equals(mWsManager?.tag(), wssUrl)) {
            mWsManager = WsManager.Builder(XXF.getApplication())
                    .wsUrl(wssUrl)
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
            mWsManager!!.setWsStatusListener(this)
            mWsManager!!.startConnect()
        }
        return mWsManager!!;
    }
}