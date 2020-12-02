package com.bkt.basdk.api

import com.bkt.contract.ba.api.ContractProxyApiService
import com.xxf.arch.annotation.BaseUrl
import com.xxf.arch.annotation.RxHttpCacheProvider
import com.xxf.arch.annotation.RxJavaInterceptor

/**
 * @Description: 合约api
 * @Author: XGod
 * @CreateDate: 2020/12/2 10:13
 */

@BaseUrl("http://api.map.baidu.com/")
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider::class)
@com.xxf.arch.annotation.Interceptor(MyLoggerInterceptor::class, MyLoggerInterceptor2::class)
@RxJavaInterceptor(DefaultCallAdapter::class)
interface ContractApiService : ContractProxyApiService {


}