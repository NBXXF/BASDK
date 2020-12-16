package com.bkt.contract.ba.model.dto

import com.bkt.contract.ba.enums.*
import com.bkt.contract.ba.model.PairConfigProviderModel
import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.formatobject.TimeFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter

/**
 * @Description: 订单信息
 * @Author: XGod
 * @CreateDate: 2020/12/14 14:27
 */
open class OrderInfoDto : PairConfigProviderModel {
    /**
     * {
     * "avgPrice": "0.0",              // 平均成交价
     * "clientOrderId": "abc",             // 用户自定义的订单号
     * "cumBase": "0",                         // 成交金额(标的数量)
     * "executedQty": "0",                 // 成交量(张数)
     * "orderId": 1573346959,              // 系统订单号
     * "origQty": "0.40",                  // 原始委托数量
     * "origType": "TRAILING_STOP_MARKET", // 触发前订单类型
     * "price": "0",                       // 委托价格
     * "reduceOnly": false,                // 是否仅减仓
     * "side": "BUY",                      // 买卖方向
     * "positionSide": "SHORT",            // 持仓方向
     * "status": "NEW",                    // 订单状态
     * "stopPrice": "9300",                    // 触发价,对`TRAILING_STOP_MARKET`无效
     * "closePosition": false,   // 是否条件全平仓
     * "symbol": "BTCUSD_200925",              // 交易对
     * "pair": "BTCUSD",   // 标的交易对
     * "time": 1579276756075,              // 订单时间
     * "timeInForce": "GTC",               // 有效方法
     * "type": "TRAILING_STOP_MARKET",     // 订单类型
     * "activatePrice": "9020",            // 跟踪止损激活价格, 仅`TRAILING_STOP_MARKET` 订单返回此字段
     * "priceRate": "0.3",                 // 跟踪止损回调比例, 仅`TRAILING_STOP_MARKET` 订单返回此字段
     * "updateTime": 1579276756075,        // 更新时间
     * "workingType": "CONTRACT_PRICE",        // 条件价格触发类型
     * "priceProtect": false            // 是否开启条件单触发保护
     * }
     */

    /**
     * 平均成交价
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var avgPrice: NumberFormatObject? = null

    /**
     * 用户自定义的订单号
     */
    var clientOrderId: String? = null

    /**
     * 成交金额(标的数量)
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var cumBase: NumberFormatObject? = null

    /**
     * 成交量(张数)
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var executedQty: NumberFormatObject? = null


    /**
     * 系统订单号
     */
    var orderId: Long = 0

    /**
     * 原始委托数量
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var origQty: NumberFormatObject? = null


    /**
     * 触发前订单类型
     */
    var origType: OrderType? = null


    /**
     * 委托价格
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var price: NumberFormatObject? = null


    /**
     * 是否仅减仓
     */
    var reduceOnly: Boolean = false


    /**
     * 买卖方向
     */
    var side: Side? = null


    /**
     * 持仓方向
     */
    var positionSide: PositionDirection? = null

    /**
     * 订单状态
     */
    var status: OderStatus? = null

    /**
     * 触发价,对`TRAILING_STOP_MARKET`无效
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var stopPrice: NumberFormatObject? = null

    /**
     * 是否条件全平仓
     */
    var closePosition = false

    /**
     * 交易对
     */
    var symbol: String? = null

    /**
     * 标的交易对
     */
    var pair: String? = null


    /**
     * 订单时间
     */
    @JsonAdapter(Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter::class)
    var time: TimeFormatObject? = null

    /**
     * timeInForce
     */
    var timeInForce: TimeInForce? = null

    /**
     * 订单类型
     */
    var type: OrderType? = null

    /**
     * 跟踪止损激活价格, 仅`TRAILING_STOP_MARKET` 订单返回此字段
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var activatePrice: NumberFormatObject? = null

    /**
     * 跟踪止损回调比例, 仅`TRAILING_STOP_MARKET` 订单返回此字段
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var priceRate: NumberFormatObject? = null


    /**
     * 更新时间
     */
    @JsonAdapter(Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter::class)
    var updateTime: TimeFormatObject? = null;

    /**
     * 条件价格触发类型
     */
    var workingType: WorkingType? = null;

    /**
     * 是否开启条件单触发保护
     */
    var priceProtect = false


    override fun provideSymbol(): String? {
        return symbol;
    }

}