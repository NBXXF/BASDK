package com.bkt.contract.ba.service

import com.bkt.contract.ba.model.ExchangeInfoDto
import com.bkt.contract.ba.model.TickerEventDto
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.CacheType
import retrofit2.http.Cache
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Description: ba Proxy http api
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
    fun testApi(): Observable<JsonObject>


    /**
     * 获取交易规则和交易对
     * https://binance-docs.github.io/apidocs/futures/cn/#0f3f2d5ee7
     */
    @GET("fapi/v1/exchangeInfo")
    fun getExchangeInfo(@Cache type: CacheType): Observable<ExchangeInfoDto>;

    /**
     * 单个交易对 24hr价格变动情况
     */
    @GET("fapi/v1/ticker/24hr")
    fun getTicker24hr(@Cache type: CacheType, @Query("symbol") symbol: String): Observable<TickerEventDto>;

    /**
     * 所有交易对 24hr价格变动情况
     */
    @GET("fapi/v1/ticker/24hr")
    fun getTicker24hr(@Cache type: CacheType): Observable<List<TickerEventDto>>;
}