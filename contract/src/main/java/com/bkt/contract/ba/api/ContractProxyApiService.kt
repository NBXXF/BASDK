package com.bkt.contract.ba.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @Description: ba Proxy api
 * @Author: XGod
 * @CreateDate: 2020/12/1 20:10
 */
interface ContractProxyApiService {

    /**
     * 测试api
     *
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    fun testApi(): Observable<JsonObject?>?
}