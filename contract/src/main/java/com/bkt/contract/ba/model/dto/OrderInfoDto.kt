package com.bkt.contract.ba.model.dto

import android.text.TextUtils
import com.bkt.contract.ba.enums.*
import com.bkt.contract.ba.model.PairConfigProviderModel
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.formatobject.TimeFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter

/**
 * @Description: 订单信息  socket和socket 事件：ORDER_TRADE_UPDATE 复用一个模型
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/14 14:27
 */
open class OrderInfoDto : PairConfigProviderModel {
    /**
     * https://binance-docs.github.io/apidocs/testnet/cn/#060a012f0b
     * socket 事件：ORDER_TRADE_UPDATE
     * {
    "s":"BTCUSDT",                  // 交易对
    "c":"TEST",                     // 客户端自定订单ID
    // 特殊的自定义订单ID:
    // "autoclose-"开头的字符串: 系统强平订单
    // "adl_autoclose": ADL自动减仓订单
    "S":"SELL",                     // 订单方向
    "o":"TRAILING_STOP_MARKET", // 订单类型
    "f":"GTC",                      // 有效方式
    "q":"0.001",                    // 订单原始数量
    "p":"0",                        // 订单原始价格
    "ap":"0",                       // 订单平均价格
    "sp":"7103.04",                 // 条件订单触发价格，对追踪止损单无效
    "x":"NEW",                      // 本次事件的具体执行类型
    "X":"NEW",                      // 订单的当前状态
    "i":8886774,                    // 订单ID
    "l":"0",                        // 订单末次成交量
    "z":"0",                        // 订单累计已成交量
    "L":"0",                        // 订单末次成交价格
    "N": "USDT",                    // 手续费资产类型
    "n": "0",                       // 手续费数量
    "T":1568879465651,              // 成交时间
    "t":0,                          // 成交ID
    "b":"0",                        // 买单净值
    "a":"9.91",                     // 卖单净值
    "m": false,                     // 该成交是作为挂单成交吗？
    "R":false   ,                   // 是否是只减仓单
    "wt": "CONTRACT_PRICE",         // 触发价类型
    "ot": "TRAILING_STOP_MARKET",   // 原始订单类型
    "ps":"LONG"                     // 持仓方向
    "cp":false,                     // 是否为触发平仓单; 仅在条件订单情况下会推送此字段
    "AP":"7476.89",                 // 追踪止损激活价格, 仅在追踪止损单时会推送此字段
    "cr":"5.0",                     // 追踪止损回调比例, 仅在追踪止损单时会推送此字段
    "rp":"0"                            // 该交易实现盈亏

    }

     */
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
    @SerializedName("avgPrice", alternate = ["ap"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var avgPrice: NumberFormatObject? = null

    /**
     * 用户自定义的订单号
     */
    @SerializedName("clientOrderId", alternate = ["c"])
    var clientOrderId: String? = null


    /**
     * 成交量(张数)
     */
    @SerializedName("executedQty", alternate = ["z"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var executedQty: NumberFormatObject? = null


    /**
     * 系统订单号
     */
    @SerializedName("orderId", alternate = ["i"])
    var orderId: String? = null;

    /**
     * 原始委托数量
     */
    @SerializedName("origQty", alternate = ["q"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var origQty: NumberFormatObject? = null


    /**
     * 触发前订单类型
     */
    @SerializedName("origType", alternate = ["ot"])
    var origType: OrderType? = null


    /**
     * 委托价格
     */
    @SerializedName("price", alternate = ["p"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var price: NumberFormatObject? = null


    /**
     * 是否仅减仓
     */
    @SerializedName("reduceOnly", alternate = ["R"])
    var reduceOnly: Boolean = false


    /**
     * 买卖方向
     */
    @SerializedName("side", alternate = ["S"])
    var side: Side? = null


    /**
     * 持仓方向
     */
    @SerializedName("positionSide", alternate = ["ps"])
    var positionSide: PositionDirection? = null

    /**
     * 订单状态
     */
    @SerializedName("status", alternate = ["X"])
    var status: OderStatus? = null

    /**
     * 触发价,对`TRAILING_STOP_MARKET`无效
     */
    @SerializedName("stopPrice", alternate = ["sp"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var stopPrice: NumberFormatObject? = null

    /**
     * 是否条件全平仓
     */
    @SerializedName("closePosition", alternate = ["cp"])
    var closePosition = false

    /**
     * 交易对
     */
    @SerializedName("symbol", alternate = ["s"])
    var symbol: String? = null


    /**
     * 订单时间
     */
    @SerializedName("time", alternate = ["T"])
    @JsonAdapter(Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter::class)
    var time: TimeFormatObject? = null

    /**
     * timeInForce
     */
    @SerializedName("timeInForce", alternate = ["f"])
    var timeInForce: TimeInForce? = null

    /**
     * 订单类型
     */
    @SerializedName("type", alternate = ["o"])
    var type: OrderType? = null

    /**
     * 跟踪止损激活价格, 仅`TRAILING_STOP_MARKET` 订单返回此字段
     */
    @SerializedName("activatePrice", alternate = ["AP"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var activatePrice: NumberFormatObject? = null

    /**
     * 跟踪止损回调比例, 仅`TRAILING_STOP_MARKET` 订单返回此字段
     */
    @SerializedName("priceRate", alternate = ["cr"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var priceRate: NumberFormatObject? = null

    /**
     * 条件价格触发类型
     */
    @SerializedName("workingType", alternate = ["wt"])
    var workingType: WorkingType? = null;


    override fun provideSymbol(): String? {
        return symbol;
    }

    override fun toString(): String {
        return "OrderInfoDto(avgPrice=$avgPrice, clientOrderId=$clientOrderId, executedQty=$executedQty, orderId=$orderId, origQty=$origQty, origType=$origType, price=$price, reduceOnly=$reduceOnly, side=$side, positionSide=$positionSide, status=$status, stopPrice=$stopPrice, closePosition=$closePosition, symbol=$symbol, time=$time, timeInForce=$timeInForce, type=$type, activatePrice=$activatePrice, priceRate=$priceRate, workingType=$workingType)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderInfoDto

        if (!TextUtils.equals(orderId, other.orderId)) return false
        return true
    }
}