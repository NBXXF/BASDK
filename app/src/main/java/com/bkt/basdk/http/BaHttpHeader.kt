package com.bkt.basdk.http

import com.bkt.basdk.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Description: ba http header
 * @Author: XGod
 * @CreateDate: 2020/12/18 14:57
 */
class BaHttpHeader : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        /**
         * 接口鉴权类型
        每个接口都有自己的鉴权类型,鉴权类型决定了访问时应当进行何种鉴权
        如果需要 API-key,应当在HTTP头中以X-MBX-APIKEY字段传递
        API-key 与 API-secret 是大小写敏感的
        可以在网页用户中心修改API-key 所具有的权限,例如读取账户信息、发送交易指令、发送提现指令
        鉴权类型	描述
        NONE	不需要鉴权的接口
        TRADE	需要有效的API-KEY和签名
        USER_DATA	需要有效的API-KEY和签名
        USER_STREAM	需要有效的API-KEY
        MARKET_DATA	需要有效的API-KEY
         */
        var request = chain.request()
        request = request.newBuilder().addHeader("X-MBX-APIKEY", BuildConfig.X_MBX_APIKEY).build();
        return chain.proceed(request)
    }
}