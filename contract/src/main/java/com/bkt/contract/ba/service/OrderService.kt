package com.bkt.contract.ba.service

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.enums.IncomeType
import com.bkt.contract.ba.enums.OderStatus
import com.bkt.contract.ba.enums.OrderType
import com.bkt.contract.ba.model.dto.*
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.MapTypeToken
import com.xxf.arch.json.datastructure.ListOrSingle
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.utils.NumberUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Description: 订单下单service
 * @Author: XGod
 * @CreateDate: 2020/12/14 11:27
 */
interface OrderService : ExportService {
    companion object {
        internal val INSTANCE: OrderService by lazy {
            object : OrderService {
            }
        }
    }

    /**
     * 下单
     * @param order
     */
    fun createOrder(order: OrderRequestDto): Observable<OrderInfoDto> {
        return BaClient.instance.getApiService(order.symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<OrderInfoDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<OrderInfoDto> {
                        return t.createOrder(JsonUtils.toMap(JsonUtils.toJsonString(order), MapTypeToken<String, Any>()));
                    }
                });
    }

    /**
     * 查询当前挂单 ==当前委托
     * @param orderId  系统订单号
     * @param origClientOrderId  用户自定义的订单号
     * @param recvWindow
     * @param timestamp
     */
    fun getOpenOrder(symbol: String, orderId: String?, origClientOrderId: String?, recvWindow: Long?, timestamp: Long): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getOpenOrder(symbol, orderId, origClientOrderId, recvWindow, timestamp);
                    }
                });
    }

    /**
     * 查看当前全部挂单 ==当前委托
     *
     *@param symbol
     */
    fun getOpenOrders(symbol: String, recvWindow: Long?, timestamp: Long): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getOpenOrders(symbol, recvWindow, timestamp);
                    }
                });
    }

    /**
     * 查看当前全部挂单 ==当前委托
     * 最多返回 40条  注意不是完全返回所有数据
     */
    fun getOpenOrders(type: ContractType, recvWindow: Long?, timestamp: Long): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.initializer?.getApiService(type)!!.getOpenOrders(null, recvWindow, timestamp);
    }

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
     * @param startTime
     * @param endTime
     * @param lim
     */
    fun getAllOrders(
            symbol: String,
            orderId: String?,
            startTime: Long?,
            endTime: Long?,
            limit: Int?,
            recvWindow: Long?,
            timestamp: Long
    ): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getAllOrders(symbol, orderId, startTime, endTime, limit, recvWindow, timestamp);
                    }
                });
    }

    /**
     * 获取持仓
     * 按交易对获取
     */
    fun getPositionRisk(symbol: String,
                        recvWindow: Long?,
                        timestamp: Long): Observable<ListOrSingle<PositionRiskDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<PositionRiskDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<PositionRiskDto>> {
                        return Observable.zip(t.getPositionRisk(symbol, recvWindow, timestamp),
                                UserService.INSTANCE.getLeverageBrackets(symbol),
                                object : BiFunction<ListOrSingle<PositionRiskDto>, List<LeverageBracketDto.BracketsBean>, ListOrSingle<PositionRiskDto>> {
                                    override fun apply(t1: ListOrSingle<PositionRiskDto>, t2: List<LeverageBracketDto.BracketsBean>): ListOrSingle<PositionRiskDto> {
                                        /**
                                         * 持倉接口並未返回
                                         * 需要赋值这三个字段
                                        var earningRate: NumberFormatObject? = null
                                        var marginRate: NumberFormatObject? = null;
                                        var maintenanceMarginRate: NumberFormatObject? = null;
                                         */
                                        t1.forEach {
                                            val earningRateDecimal = NumberUtils.divide(it.unRealizedProfit?.origin, it.isolatedMargin?.origin, Math.max(it.unRealizedProfit?.origin!!.scale(), it.isolatedMargin?.origin!!.scale()));
                                            it.earningRate = NumberFormatObject(earningRateDecimal, NumberUtils.formatRoundDown(earningRateDecimal, 2, 2));

                                            it.maintenanceMarginRate = t2.getBracket(it.leverage)?.maintMarginRatio;
                                        }
                                        return t1;
                                    }
                                });
                    }
                });
    }

    /**
     * 获取持仓
     * 按类型获取
     */
    fun getPositionRisk(type: ContractType,
                        recvWindow: Long?): Observable<ListOrSingle<PositionRiskDto>> {
        return Observable.zip(
                UserService.INSTANCE.getLeverageBrackets(type),
                BaClient.instance.initializer?.getApiService(type)!!.getPositionRisk(null, recvWindow, System.currentTimeMillis()), object : BiFunction<Map<String, List<LeverageBracketDto.BracketsBean>>, ListOrSingle<PositionRiskDto>, ListOrSingle<PositionRiskDto>> {
            override fun apply(t1: Map<String, List<LeverageBracketDto.BracketsBean>>, t2: ListOrSingle<PositionRiskDto>): ListOrSingle<PositionRiskDto> {
                /**
                 * 持倉接口並未返回
                 * 需要赋值这三个字段
                var earningRate: NumberFormatObject? = null
                var marginRate: NumberFormatObject? = null;
                var maintenanceMarginRate: NumberFormatObject? = null;
                 */
                t2.forEach {
                    val earningRateDecimal = NumberUtils.divide(it.unRealizedProfit?.origin, it.isolatedMargin?.origin, Math.max(it.unRealizedProfit?.origin!!.scale(), it.isolatedMargin?.origin!!.scale()));
                    it.earningRate = NumberFormatObject(earningRateDecimal, NumberUtils.formatRoundDown(earningRateDecimal, 2, 2));

                    val leverageBracket: List<LeverageBracketDto.BracketsBean>? = t1.get(CommonService.INSTANCE.convertPair(it.symbol));
                    if (leverageBracket != null) {
                        val bracket = leverageBracket.getBracket(it.leverage);
                        if (bracket != null) {
                            it.maintenanceMarginRate = bracket.maintMarginRatio;
                        }
                    }
                }
                return t2;
            }
        });
    }

    /**
     * 通过杠杆倍数获取维持保证金率
     */
    private fun List<LeverageBracketDto.BracketsBean>.getBracket(leverage: Int): LeverageBracketDto.BracketsBean? {
        var firstMatch: LeverageBracketDto.BracketsBean? = null;
        for (item: LeverageBracketDto.BracketsBean in this) {
            if (leverage <= item.initialLeverage) {
                firstMatch = item;
            }
        }
        return firstMatch;
    }

    /**
     * 获取历史成交
     * 按交易对获取
     */
    fun getUserTrades(
            symbol: String,
            startTime: Long?,
            endTime: Long?,
            fromId: String?,
            limit: Int?,
            recvWindow: Long?,
            timestamp: Long
    ): Observable<ListOrSingle<TradInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<TradInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<TradInfoDto>> {
                        return t.getUserTrades(symbol, startTime, endTime, fromId, limit, recvWindow, timestamp);
                    }
                });
    }

    /**
     * 获取历史委托
     * 按交易对获取
     * 实现逻辑:从全部订单中过滤
     */
    fun getHistoryEntrust(symbol: String,
                          orderId: String?,
                          startTime: Long?,
                          endTime: Long?,
                          limit: Int?,
                          recvWindow: Long?,
                          timestamp: Long): Observable<ListOrSingle<OrderInfoDto>> {
        return this.getAllOrders(symbol, orderId, startTime, endTime, limit, recvWindow, timestamp)
                .map(object : Function<ListOrSingle<OrderInfoDto>, ListOrSingle<OrderInfoDto>> {
                    override fun apply(t: ListOrSingle<OrderInfoDto>): ListOrSingle<OrderInfoDto> {
                        val results: ListOrSingle<OrderInfoDto> = ListOrSingle();
                        /**
                         * 限价计划委托=TAKE_PROFIT 止盈限价单  STOP 止损限价单
                         * 查询历史委托
                         * 目前历史委托只有 OrderType.TAKE_PROFIT  OrderType.STOP
                         */
                        t.forEach {
                            if (it.status != null && it.status != OderStatus.NEW
                                    && (it.type == OrderType.TAKE_PROFIT || it.type == OrderType.STOP)) {
                                results.add(it);
                            }
                        }
                        return results;
                    }
                });
    }

    /**
     * 按交易对获取资金流水
     * @param limit  默认值:100 最大值:1000
     */
    fun getUserIncome(
            symbol: String,
            incomeType: IncomeType?,
            startTime: Long?,
            endTime: Long?,
            limit: Int?,
            recvWindow: Long?,
            timestamp: Long
    ): Observable<ListOrSingle<IncomeDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<IncomeDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<IncomeDto>> {
                        return t.getUserIncome(symbol, if (incomeType == null) null else incomeType.value, startTime, endTime, limit, recvWindow, timestamp);
                    }
                });
    }

    /**
     * 按类型获取资金流水
     */
    fun getUserIncome(
            type: ContractType,
            incomeType: IncomeType?,
            startTime: Long?,
            endTime: Long?,
            limit: Int?,
            recvWindow: Long?,
            timestamp: Long
    ): Observable<ListOrSingle<IncomeDto>> {
        return BaClient.instance.initializer?.getApiService(type)!!
                .getUserIncome(null, if (incomeType == null) null else incomeType.value, startTime, endTime, limit, recvWindow, timestamp);
    }
}