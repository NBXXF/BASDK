package com.bkt.contract.ba.service

import com.bkt.contract.ba.common.HttpDataFunction
import com.bkt.contract.ba.enums.*
import com.bkt.contract.ba.model.dto.*
import com.bkt.contract.ba.model.event.OrderUpdateEvent
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.bkt.contract.ba.sdk.ContractProxySocketService
import com.google.gson.JsonElement
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.MapTypeToken
import com.xxf.arch.json.datastructure.ListOrSingle
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_percent_auto_2_2_DOWN_FormatTypeAdapter
import com.xxf.arch.utils.NumberUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * @Description: 订单下单service
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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
     *  会发送订单socket,接收事件请订阅 subOrderChange()
     * @param order
     */
    fun createOrder(order: OrderRequestDto): Observable<OrderInfoDto> {
        return BaClient.instance.getApiService(order.symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<OrderInfoDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<OrderInfoDto> {
                        return t.createOrder(JsonUtils.toMap(JsonUtils.toJsonString(order), MapTypeToken<String, Any>()))
                                .map(HttpDataFunction())
                                .doOnNext {
                                    /**
                                     * 主动模拟socket发送一遍信号
                                     * 订单变化socket 不会时时
                                     */
                                    val orderUpdateEvent = OrderUpdateEvent();
                                    orderUpdateEvent.orderEventType = OrderEventType.NEW;
                                    orderUpdateEvent.order = it;
                                    ContractProxySocketService.bus.onNext(orderUpdateEvent);
                                };
                    }
                });
    }

    /**
     * 查询当前挂单 ==当前委托
     * @param orderId  系统订单号
     * @param origClientOrderId  用户自定义的订单号
     * @param recvWindow
     */
    fun getOpenOrder(symbol: String, orderId: String?, origClientOrderId: String?, recvWindow: Long?): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getOpenOrder(symbol, orderId, origClientOrderId, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
                    }
                });
    }

    /**
     * 查看当前全部挂单 ==当前委托
     *
     *@param symbol
     */
    fun getOpenOrders(symbol: String, recvWindow: Long?): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getOpenOrders(symbol, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
                    }
                });
    }

    /**
     * 查看当前全部挂单 ==当前委托
     * 最多返回 40条  注意不是完全返回所有数据
     */
    fun getOpenOrders(type: ContractType, recvWindow: Long?): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.initializer?.getApiService(type)!!.getOpenOrders(null, recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction());
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
            recvWindow: Long?
    ): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getAllOrders(symbol, orderId, startTime, endTime, limit, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
                    }
                });
    }

    /**
     * 获取指定交易对持仓
     * 按交易对获取
     */
    fun getPositionRisk(symbol: String,
                        recvWindow: Long?): Observable<List<PositionRiskDto>> {
        return PairService.INSTANCE.getPairType(symbol)
                .flatMap(object : Function<ContractType, ObservableSource<List<PositionRiskDto>>> {
                    override fun apply(type: ContractType): ObservableSource<List<PositionRiskDto>> {
                        return getPositionRiskInner(type, symbol, recvWindow);
                    }
                })
    }

    /**
     * 按类型获取持仓
     */
    fun getPositionRisk(type: ContractType,
                        recvWindow: Long?): Observable<List<PositionRiskDto>> {
        return getPositionRiskInner(type, null, recvWindow);
    }

    /**
     * 获取持仓
     * @param symbol 传空代表当前类型所有币种余额
     */
    private fun getPositionRiskInner(type: ContractType,
                                     symbol: String?,
                                     recvWindow: Long?): Observable<List<PositionRiskDto>> {
        return Observable.zip(
                UserService.INSTANCE.getLeverageBrackets(type),
                UserService.INSTANCE.getAccount(type, recvWindow),
                CommonService.INSTANCE.getAdlQuantileByType(type, recvWindow),
                BaClient.instance.initializer?.getApiService(type)!!.getPositionRisk(symbol, recvWindow, System.currentTimeMillis())
                        .map(HttpDataFunction())
                        .map {
                            it.filter {
                                NumberUtils.compare(it.positionAmt?.origin, 0) != 0;
                            }
                        },
                object : io.reactivex.functions.Function4<Map<String, List<LeverageBracketDto.BracketsBean>>, AccountInfoDto, LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>, List<PositionRiskDto>, List<PositionRiskDto>> {
                    override fun apply(leverageMap: Map<String, List<LeverageBracketDto.BracketsBean>>, accountInfoDto: AccountInfoDto, adlQuantiles: LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>, positionRisks: List<PositionRiskDto>): List<PositionRiskDto> {
                        val positionMap: MutableMap<String, AccountInfoDto.PositionDetailsDto> = mutableMapOf();
                        accountInfoDto.positions?.forEach {
                            it.symbol?.let { it1 -> positionMap.put(it1, it) };
                        }
                        /**
                         * 持倉接口並未返回
                         * 需要赋值这三个字段
                        var marginRate: NumberFormatObject? = null;
                        var maintenanceMarginRate: NumberFormatObject? = null;
                        var adlQuantile: AdlQuantileDto.AdlQuantileItem? = null;
                         */
                        positionRisks.forEach {

                            val leverageBracket: List<LeverageBracketDto.BracketsBean>? = leverageMap.get(CommonService.INSTANCE.convertPair(it.symbol));
                            var bracket: LeverageBracketDto.BracketsBean? = null;
                            if (leverageBracket != null) {
                                bracket = leverageBracket.getBracket(it.leverage);
                                it.maintenanceMarginRate = bracket?.maintMarginRatio;
                            }

                            /**
                             * 逐仓仓位维持保证金/（逐仓模式下钱包余额+未实现盈亏）
                             */
                            val positionDetails: AccountInfoDto.PositionDetailsDto? = positionMap.get(it.symbol);
                            if (positionDetails != null) {
                                val isolatedMainMargin = NumberUtils.subtract(
                                        NumberUtils.multiply(
                                                NumberUtils.multiply(it.positionAmt?.origin?.abs(), it.markPrice?.origin),
                                                it.maintenanceMarginRate?.origin),
                                        bracket?.cum);
                                val marginRateDecimal = NumberUtils.divide(isolatedMainMargin, NumberUtils.add(it.isolatedWallet?.origin, it.unRealizedProfit?.origin));
                                it.marginRate = NumberFormatObject(marginRateDecimal, Number_percent_auto_2_2_DOWN_FormatTypeAdapter().format(marginRateDecimal));
                            }

                            it.adlQuantile = adlQuantiles.get(it.symbol);
                        }
                        return positionRisks;
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
            recvWindow: Long?
    ): Observable<ListOrSingle<TradInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<TradInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<TradInfoDto>> {
                        return t.getUserTrades(symbol, startTime, endTime, fromId, limit, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
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
                          recvWindow: Long?): Observable<ListOrSingle<OrderInfoDto>> {
        return this.getAllOrders(symbol, orderId, startTime, endTime, limit, recvWindow)
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
     * 订阅订单变化事件
     */
    fun subOrderChange(type: ContractType): Observable<OrderUpdateEvent> {
        return BaClient.instance
                .initializer!!
                .getSocketService(type)
                .subOrderChange(type);
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
            recvWindow: Long?
    ): Observable<ListOrSingle<IncomeDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<IncomeDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<IncomeDto>> {
                        return t.getUserIncome(symbol, if (incomeType == null) null else incomeType.value, startTime, endTime, limit, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
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
            recvWindow: Long?
    ): Observable<ListOrSingle<IncomeDto>> {
        return BaClient.instance.initializer?.getApiService(type)!!
                .getUserIncome(null, if (incomeType == null) null else incomeType.value, startTime, endTime, limit, recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction());
    }


    /**
     * 针对交易对 撤销全部订单
     */
    fun cancelAllOrder(symbol: String, recvWindow: Long?): Observable<JsonElement> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<JsonElement>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<JsonElement> {
                        return t.cancelAllOrder(symbol, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
                    }
                });
    }

    /**
     * 针对交易对 撤销一个单
     */
    fun cancelOrder(symbol: String, orderId: String?, origClientOrderId: String?, recvWindow: Long?): Observable<OrderInfoDto> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<OrderInfoDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<OrderInfoDto> {
                        return t.cancelOrder(symbol, orderId, origClientOrderId, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
                    }
                });
    }

    /**
     * 调整保证金
     */
    fun changePositionMargin(
            symbol: String,
            positionSide: PositionDirection?,
            amount: String,
            type: PositionMarginType,
            recvWindow: Long?
    ): Observable<PositionMarginResultDto> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<PositionMarginResultDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<PositionMarginResultDto> {
                        return t.changePositionMargin(symbol, if (positionSide == null) null else positionSide.value, amount, type.value, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction())
                    }
                });
    }
}