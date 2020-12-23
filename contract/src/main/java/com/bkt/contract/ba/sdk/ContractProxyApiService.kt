package com.bkt.contract.ba.sdk

import com.bkt.contract.ba.enums.PositionMarginType
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
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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
     * 获取服务器时间
     */
    @GET("v1/time")
    fun getServerTime(): Observable<BktResDto<ServerTimeDto>>;

    /**
     * 获取交易规则和交易对
     * https://binance-docs.github.io/apidocs/futures/cn/#0f3f2d5ee7
     */
    @GET("v1/exchangeInfo")
    fun getExchangeInfo(@Cache type: CacheType): Observable<BktResDto<ExchangeInfoDto>>;


    /**
     * 单个交易对 24hr价格变动情况
     * https://binance-docs.github.io/apidocs/futures/cn/#24hr
     */
    @GET("v1/ticker/24hr")
    fun getTicker24hr(@Cache type: CacheType,
                      @Header("cache") cacheTime: Long,
                      @Query("symbol") symbol: String): Observable<BktResDto<TickerEventDto>>;

    /**
     * 所有交易对 24hr价格变动情况
     * https://binance-docs.github.io/apidocs/futures/cn/#24hr
     */
    @GET("v1/ticker/24hr")
    fun getTicker24hr(@Cache type: CacheType): Observable<BktResDto<List<TickerEventDto>>>;


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
                  @Query("limit") limit: Int?): Observable<BktResDto<List<KLineEventDto>>>

    /**
     * 获取深度
     * https://binance-docs.github.io/apidocs/futures/cn/#0f3f2d5ee7
     * @param limit  默认 500; 可选值:[5, 10, 20, 50, 100, 500, 1000]
     */
    @GET("v1/depth")
    fun getDepth(@Cache type: CacheType,
                 @Header("cache") cacheTime: Long,
                 @Query("symbol") symbol: String,
                 @Query("limit") limit: Int): Observable<BktResDto<DepthEventDtoPo>>;

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
    ): Observable<BktResDto<ListOrSingle<PremiumIndexPriceDto>>>;


    /**
     * 获取最新价
     * @param symbol 不发送交易对参数，则会返回所有交易对信息
     */
    @GET("v1/ticker/price")
    fun getTickerPrice(@Query("symbol") symbol: String?): Observable<BktResDto<ListOrSingle<TickerPriceDto>>>;

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
    // @POST("v1/order")
    //这里用自己的接口
    @POST("v1/orderCreate")
    @FormUrlEncoded
    fun createOrder(@FieldMap map: Map<String, Any>): Observable<BktResDto<OrderInfoDto>>;


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
    ): Observable<BktResDto<ListOrSingle<OrderInfoDto>>>;

    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#user_data-4
     * 查询当前挂单 (USER_DATA)
     */
    @GET("v1/openOrders")
    fun getOpenOrders(@Query("symbol") symbol: String?,
                      @Query("recvWindow") recvWindow: Long?,
                      @Query("timestamp") timestamp: Long
    ): Observable<BktResDto<ListOrSingle<OrderInfoDto>>>;


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
    ): Observable<BktResDto<ListOrSingle<OrderInfoDto>>>;


    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#trade-12
     * 查询持仓
     */
    @GET("v2/positionRisk")
    fun getPositionRisk(
            @Query("symbol") symbol: String?,
            @Query("recvWindow") recvWindow: Long?,
            @Query("timestamp") timestamp: Long
    ): Observable<BktResDto<ListOrSingle<PositionRiskDto>>>;

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
    ): Observable<BktResDto<ListOrSingle<TradInfoDto>>>;


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
    ): Observable<BktResDto<ListOrSingle<IncomeDto>>>;


    /**
     * 杠杆分层标准
     *
     * 注意！！:USDT和USD 不一样 一个传递pair  一个传递 symbol
     * 如果symbol 和pair都不传 那么就是所有的
     */
    @GET("v1/leverageBracket")
    fun getLeverageBracket(@Query("symbol") symbol: String?,
                           @Query("pair") pair: String?,
                           @Query("recvWindow") recvWindow: Long?,
                           @Query("timestamp") timestamp: Long)
            : Observable<BktResDto<ListOrSingle<LeverageBracketDto>>>;

    /**
     * 账户余额 (USER_DATA)
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#user_data-6
     */
    @GET("{version}/balance")
    fun getBalance(
            @Path("version") version: String,
            @Query("recvWindow") recvWindow: Long?,
            @Query("timestamp") timestamp: Long): Observable<BktResDto<ListOrSingle<CoinBalanceDto>>>;

    /**
     * 撤销全部订单 (TRADE)
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#trade-6
     */
    // @DELETE("v1/allOpenOrders")
    //用自己转发的
    @POST("v1/orderCancelAll")
    fun cancelAllOrder(@Query("symbol") symbol: String,
                       @Query("recvWindow") recvWindow: Long?,
                       @Query("timestamp") timestamp: Long): Observable<BktResDto<ListOrSingle<OrderInfoDto>>>;


    /**
     * 撤销指定订单 (TRADE)
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#trade-6
     */
    // @DELETE("v1/order")
    //用自己转发的
    @POST("v1/orderCancel")
    fun cancelOrder(@Query("symbol") symbol: String,
                    @Query("orderId") orderId: String?,
                    @Query("origClientOrderId") origClientOrderId: String?,
                    @Query("recvWindow") recvWindow: Long?,
                    @Query("timestamp") timestamp: Long): Observable<BktResDto<OrderInfoDto>>;

    /**
     * 调整逐仓保证金
     *https://binance-docs.github.io/apidocs/delivery_testnet/cn/#trade-11
     */
    @POST("v1/positionMargin")
    @FormUrlEncoded
    fun changePositionMargin(
            @Field("symbol") symbol: String,
            @Field("positionSide") positionSide: String?,
            @Field("amount") amount: String,
            @Field("type") type: Int,
            @Field("recvWindow") recvWindow: Long?,
            @Field("timestamp") timestamp: Long
    ): Observable<BktResDto<PositionMarginResultDto>>;

    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#user_data-8
     * 持仓ADL队列估算
     */
    @GET("v1/adlQuantile")
    fun getAdlQuantile(@Query("symbol") symbol: String?,
                       @Query("recvWindow") recvWindow: Long?,
                       @Query("timestamp") timestamp: Long)
            : Observable<BktResDto<ListOrSingle<AdlQuantileDto>>>;


    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#trade-9
     * 调整开仓杠杆 (TRADE)
     */
    @POST("v1/leverage")
    @FormUrlEncoded
    fun changeLeverage(@Field("symbol") symbol: String,
                       @Field("leverage") leverage: Int,
                       @Field("recvWindow") recvWindow: Long?,
                       @Field("timestamp") timestamp: Long): Observable<BktResDto<ChangeLeverageResDto>>;

    /**
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#user_data
     * 查询持仓模式
     */
    @GET("v1/positionSide/dual")
    fun getPositionSideDual(@Query("recvWindow") recvWindow: Long?,
                            @Query("timestamp") timestamp: Long): Observable<BktResDto<PositionSideDualDto>>;

    /**
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#trade
     * 更改持仓模式
     */
    @POST("v1/positionSide/dual")
    @FormUrlEncoded
    fun changePositionSideDual(
            @Field("dualSidePosition") dualSidePosition: String,
            @Field("recvWindow") recvWindow: Long?,
            @Field("timestamp") timestamp: Long): Observable<BktResDto<BaResultDto>>;


    /**
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#websocket-2
     * 创建一个新的user data stream,返回值为一个listenKey,即websocket订阅的stream名称。如果该帐户具有有效的listenKey,则将返回该listenKey并将其有效期延长60分钟。
     */
    // @POST("v1/listenKey")
    //这里用自己的
    @POST("v1/listenKeyCreate")
    fun createListenKey(): Observable<BktResDto<ListenKeyDto>>;

    /**
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#listenkey-user_stream
     *有效期延长至本次调用后60分钟
     */
    @PUT("v1/listenKey")
    fun lengthenListenKey(): Observable<BktResDto<JsonObject>>;


    /**
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#listenkey-user_stream-2
     * 关闭listenKey 关闭某账户数据流
     */
    @DELETE("v1/listenKey")
    fun deleteListenKey(): Observable<BktResDto<JsonObject>>;


    /**
     * https://binance-docs.github.io/apidocs/delivery_testnet/cn/#user_data-7
     * 获取用户账户信息
     * 对于单向持仓模式，"positions"仅会展示"BOTH"方向的持仓
     * 对于双向持仓模式，"positions"会展示所有"BOTH", "LONG", 和"SHORT"方向的持仓
     */
    @GET("v1/account")
    fun getAccount(
            @Cache type: CacheType,
            @Header("cache") cacheTime: Long,
            @Query("recvWindow") recvWindow: Long?,
            @Query("timestamp") timestamp: Long): Observable<BktResDto<AccountInfoDto>>;
}