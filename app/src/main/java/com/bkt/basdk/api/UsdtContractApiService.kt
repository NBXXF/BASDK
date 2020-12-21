package com.bkt.basdk.api

import com.bkt.basdk.http.DefaultCallAdapter
import com.bkt.basdk.http.DefaultRxHttpCacheDirectoryProvider
import com.bkt.basdk.http.MyLoggerInterceptor
import com.bkt.basdk.http.MyLoggerInterceptor2
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.xxf.arch.annotation.BaseUrlProvider
import com.xxf.arch.annotation.RxHttpCacheProvider
import com.xxf.arch.annotation.RxJavaInterceptor
import com.xxf.arch.http.url.UrlProvider

/**
 * @Description: 合约api 采用伪代理模式;只是配置微服务api基本信息(如主机,拦截器等);其核心声明和实现并不在此
 * @Author: XGod
 * @CreateDate: 2020/12/2 10:13
 *
 *
 * 正式版本的接口 https://fapi.binance.com/ 好像需要代理
 * 测试版本接口 https://testnet.binancefuture.com/
 */
class UsdtUrlProvider : UrlProvider {
    override fun getBaseUrl(apiClazz: Class<*>?): String {
       // return "https://testnet.binancefuture.com/fapi/";
        return "http://bkex.ide/api/f/";
    }
}

@BaseUrlProvider(UsdtUrlProvider::class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider::class)
@com.xxf.arch.annotation.Interceptor(com.bkt.basdk.http.BaHttpHeader::class,MyLoggerInterceptor::class, MyLoggerInterceptor2::class)
@RxJavaInterceptor(DefaultCallAdapter::class)
interface UsdtContractApiService : ContractProxyApiService {

}