package com.bkt.contract.ba.sdk

import com.bkt.contract.ba.model.dto.*
import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.google.gson.JsonObject
import com.xxf.arch.json.datastructure.ListOrEmpty
import com.xxf.arch.json.datastructure.ListOrSingle
import io.reactivex.Observable
import retrofit2.CacheType
import retrofit2.http.*

/**
 * @Description: ba Proxy http api
 * @Author: XGod
 * @CreateDate: 2020/12/1 20:10
 */

@JvmSuppressWildcards
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
    @GET("v1/exchangeInfo")
    fun getExchangeInfo(@Cache type: CacheType): Observable<ExchangeInfoDto>;


    /**
     * 单个交易对 24hr价格变动情况
     * https://binance-docs.github.io/apidocs/futures/cn/#24hr
     */
    @GET("v1/ticker/24hr")
    fun getTicker24hr(@Cache type: CacheType, @Query("symbol") symbol: String): Observable<TickerEventDto>;

    /**
     * 所有交易对 24hr价格变动情况
     * https://binance-docs.github.io/apidocs/futures/cn/#24hr
     */
    @GET("v1/ticker/24hr")
    fun getTicker24hr(@Cache type: CacheType): Observable<List<TickerEventDto>>;


    /**
     * K线数据
     *
     * https://binance-docs.github.io/apidocs/futures/cn/#k
     * @param symbol    STRING	YES	交易对
     * @param interval    ENUM	YES	时间间隔
     * @param startTime    LONG	NO	起始时间
     * @param  endTime    LONG	NO	结束时间
     * @param  limit    INT	NO	默认值:500 最大值:1500.
     */
    @GET("v1/klines")
    fun getKLines(@Cache type: CacheType,
                  @Header("cache") cacheTime: Long,
                  @Query("symbol") symbol: String,
                  @Query("interval") interval: String,
                  @Query("startTime") startTime: Long?,
                  @Query("endTime") endTime: Long?,
                  @Query("limit") limit: Int?): Observable<List<KLineEventDto>>

    /**
     * 获取深度
     * https://binance-docs.github.io/apidocs/futures/cn/#0f3f2d5ee7
     * @param limit  默认 500; 可选值:[5, 10, 20, 50, 100, 500, 1000]
     */
    @GET("v1/depth")
    fun getDepth(@Cache type: CacheType,
                 @Header("cache") cacheTime: Long,
                 @Query("symbol") symbol: String,
                 @Query("limit") limit: Int): Observable<DepthEventDtoPo>;

    /**
     * 获取成交
     * https://binance-docs.github.io/apidocs/futures/cn/#c59e471e81
     */
    @GET("v1/aggTrades")
    fun getTrades(@Cache type: CacheType,
                  @Header("cache") cacheTime: Long,
                  @Query("symbol") symbol: String,
                  @Query("startTime") startTime: Long?,
                  @Query("endTime") endTime: Long?,
                  @Query("limit") limit: Long?
    ): Observable<List<TradeEventDto>>;


    /**
     * 获取指数价和资金汇率
     */
    @GET("v1/premiumIndex")
    fun getPremiumIndex(@Cache type: CacheType,
                        @Header("cache") cacheTime: Long,
                        @Query("symbol") symbol: String,
                        @Query("pair") pair: String?
    ): Observable<ListOrSingle<PremiumIndexPriceDto>>;


    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#trade-2
     * 下单
     *
     * symbol	STRING	YES	交易对
     * side	ENUM	YES	买卖方向 SELL, BUY
     * positionSide	ENUM	NO	持仓方向，单向持仓模式下非必填，默认且仅可填BOTH;在双向持仓模式下必填,且仅可选择 LONG 或 SHORT
     * type	ENUM	YES	订单类型 LIMIT, MARKET, STOP, TAKE_PROFIT, STOP_MARKET, TAKE_PROFIT_MARKET, TRAILING_STOP_MARKET
     * reduceOnly	STRING	NO	true, false; 非双开模式下默认false；双开模式下不接受此参数； 使用closePosition不支持此参数。
     * quantity	DECIMAL	NO	下单数量,使用closePosition不支持此参数。
     * price	DECIMAL	NO	委托价格
     * newClientOrderId	STRING	NO	用户自定义的订单号，不可以重复出现在挂单中。如空缺系统会自动赋值
     * stopPrice	DECIMAL	NO	触发价, 仅 STOP, STOP_MARKET, TAKE_PROFIT, TAKE_PROFIT_MARKET 需要此参数
     * closePosition	STRING	NO	true, false；触发后全部平仓，仅支持STOP_MARKET和TAKE_PROFIT_MARKET；不与quantity合用；自带只平仓效果，不与reduceOnly 合用
     * activationPrice	DECIMAL	NO	追踪止损激活价格，仅TRAILING_STOP_MARKET 需要此参数, 默认为下单当前市场价格(支持不同workingType)
     * callbackRate	DECIMAL	NO	追踪止损回调比例，可取值范围[0.1, 5],其中 1代表1% ,仅TRAILING_STOP_MARKET 需要此参数
     * timeInForce	ENUM	NO	有效方法
     * workingType	ENUM	NO	stopPrice 触发类型: MARK_PRICE(标记价格), CONTRACT_PRICE(合约最新价). 默认 CONTRACT_PRICE
     * priceProtect	STRING	NO	条件单触发保护："true","false", 默认"false". 仅 STOP, STOP_MARKET, TAKE_PROFIT, TAKE_PROFIT_MARKET 需要此参数
     * newOrderRespType	ENUM	NO	"ACK", "RESULT", 默认 "ACK"
     * recvWindow	LONG	NO
     * timestamp	LONG	YES
     */
    @POST("v1/order")
    @FormUrlEncoded
    fun createOrder(@FieldMap map: Map<String, Any>): Observable<OrderInfoDto>;


    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#user_data-3
     * 查询当前挂单 (USER_DATA)
     */
    @GET("v1/openOrder")
    fun getOpenOrder(@Query("symbol") symbol: String,
                     @Query("orderId") orderId: String?,
                     @Query("origClientOrderId") origClientOrderId: String?,
                     @Query("recvWindow") recvWindow: Long?,
                     @Query("timestamp") timestamp: Long
    ): Observable<ListOrSingle<OrderInfoDto>>;

    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#user_data-4
     * 查询当前挂单 (USER_DATA)
     */
    @GET("v1/openOrders")
    fun getOpenOrders(@Query("symbol") symbol: String?,
                      @Query("recvWindow") recvWindow: Long?,
                      @Query("timestamp") timestamp: Long
    ): Observable<ListOrSingle<OrderInfoDto>>;


    /**
     * 查询所有订单(包括历史订单) (USER_DATA)
     *
     * 请注意，如果订单满足如下条件，不会被查询到：
     * 订单的最终状态为 CANCELED 或者 EXPIRED, 并且
     * 订单没有任何的成交记录, 并且
     * 订单生成时间 + 30天 < 当前时间
     *
     * @param orderId 只返回此orderID及之后的订单，缺省返回最近的订单
     * @param limit  默认值:500 最大值:1000
     */
    @GET("v1/allOrders")
    fun getAllOrders(
            @Query("symbol") symbol: String,
            @Query("orderId") orderId: String?,
            @Query("startTime") startTime: Long?,
            @Query("endTime") endTime: Long?,
            @Query("limit") limit: Int?,
            @Query("recvWindow") recvWindow: Long?,
            @Query("timestamp") timestamp: Long
    ): Observable<ListOrSingle<OrderInfoDto>>;


    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#trade-12
     * 查询持仓
     */
    @GET("v2/positionRisk")
    fun getPositionRisk(
            @Query("symbol") symbol: String?,
            @Query("recvWindow") recvWindow: Long?,
            @Query("timestamp") timestamp: Long
    ): Observable<ListOrSingle<PositionRiskDto>>;

    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#v2-user_data-3
     * 账户成交历史 (USER_DATA)
     */
    @GET("v1/userTrades")
    fun getUserTrades(
            @Query("symbol") symbol: String,
            @Query("startTime") startTime: Long?,
            @Query("endTime") endTime: Long?,
            @Query("fromId") fromId: String?,
            @Query("limit") limit: Int?,
            @Query("recvWindow") recvWindow: Long?,
            @Query("timestamp") timestamp: Long
    ): Observable<ListOrSingle<TradInfoDto>>;


    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#user_data-6
     * 获取账户损益资金流水(USER_DATA)
     *
     * @param limit 默认值:100 最大值:1000
     */
    @GET("v1/income")
    fun getUserIncome(
            @Query("symbol") symbol: String?,
            @Query("incomeType") incomeType: String?,
            @Query("startTime") startTime: Long?,
            @Query("endTime") endTime: Long?,
            @Query("limit") limit: Int?,
            @Query("recvWindow") recvWindow: Long?,
            @Query("timestamp") timestamp: Long
    ): Observable<ListOrSingle<IncomeDto>>;

}