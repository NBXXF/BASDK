package com.bkt.contract.ba.model.dto

import com.bkt.contract.ba.enums.PositionDirection
import com.bkt.contract.ba.enums.Side
import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter

/**
 * @Description: 成交信息
 * @Author: XGod
 * @CreateDate: 2020/12/15 13:59
 */
class TradInfoDto {
    /**
     * v1/userTrades (HMAC SHA256)
     * {
     * "buyer": false, // 是否是买方
     * "commission": "-0.07819010", // 手续费
     * "commissionAsset": "USDT", // 手续费计价单位
     * "id": 698759, // 交易ID
     * "maker": false, // 是否是挂单方
     * "orderId": 25851813, // 订单编号
     * "price": "7819.01", // 成交价
     * "qty": "0.002", // 成交量
     * "quoteQty": "15.63802", // 成交额
     * "realizedPnl": "-0.91539999",   // 实现盈亏
     * "side": "SELL", // 买卖方向
     * "positionSide": "SHORT",  // 持仓方向
     * "symbol": "BTCUSDT", // 交易对
     * "time": 1569514978020 // 时间
     * }
     */

    /**
     *  是否是买方
     */
    var buyer: Boolean = false

    /**
     * 手续费
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var commission: NumberFormatObject? = null

    /**
     * 手续费计价单位
     */
    var commissionAsset: String? = null

    /**
     * 交易ID
     */
    var id: String? = null

    /**
     * 是否是挂单方
     */
    var maker: Boolean = false


    /**
     * 订单编号
     */
    var orderId: String? = null;

    /**
     * 成交价
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var price: NumberFormatObject? = null

    /**
     * 成交量
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var qty: NumberFormatObject? = null

    /** 成交额
     *
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var quoteQty: NumberFormatObject? = null


    /**
     * 实现盈亏
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var realizedPnl: NumberFormatObject? = null

    /**
     * 买卖方向
     */
    var side: Side? = null

    /**
     * 持仓方向
     */
    var positionSide: PositionDirection? = null

    /**
     * 交易对
     */
    var symbol: String? = null


    /**
     * 时间
     */
    var time: Long = 0

}