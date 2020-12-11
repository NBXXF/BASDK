package com.bkt.contract.ba.model.event

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter

/**
 * @Description: 指数价
 *
 *
 * @Author: XGod
 * @CreateDate: 2020/12/11 20:31
 */
class IndexPriceEvent {
    /**
     *   <pair>@indexPrice
     *    {
    "e": "indexPriceUpdate",  // 事件类型
    "E": 1591261236000,       // 事件时间
    "i": "BTCUSD",            // 标的交易对
    "p": "9636.57860000",       // 指数价格
    }
     */

    /**
     * 交易对名称
     */
    @SerializedName("i")
    var symbol: String;

    /**
     * 指数价格
     */
    @SerializedName("p")
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val indexPrice: NumberFormatObject

    constructor(symbol: String, indexPrice: NumberFormatObject) {
        this.symbol = symbol
        this.indexPrice = indexPrice
    }

    override fun toString(): String {
        return "IndexPriceEvent(symbol='$symbol', indexPrice=$indexPrice)"
    }
}