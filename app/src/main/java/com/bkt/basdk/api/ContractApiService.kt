package com.bkt.basdk.api

import com.bkt.basdk.model.HttpStatus
import com.bkt.contract.ba.service.ContractProxyApiService
import com.xxf.arch.annotation.BaseUrl
import com.xxf.arch.annotation.RxHttpCacheProvider
import com.xxf.arch.annotation.RxJavaInterceptor
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @Description: 合约api 采用伪代理模式;只是配置微服务api基本信息(如主机,拦截器等);其核心声明和实现并不在此
 * @Author: XGod
 * @CreateDate: 2020/12/2 10:13
 */

@BaseUrl("http://api.map.baidu.com/")
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider::class)
@com.xxf.arch.annotation.Interceptor(MyLoggerInterceptor::class, MyLoggerInterceptor2::class)
@RxJavaInterceptor(DefaultCallAdapter::class)
interface ContractApiService : ContractProxyApiService {

    /**
     * 测试api
     *
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    fun testApi2(): Observable<HttpStatus?>

}