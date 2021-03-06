package com.bkt.contract.ba.model.dto

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.formatobject.TimeFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_HH_c_mm_c_ss_FormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_yyyy_s_MM_s_dd_FormatTypeAdapter
import java.math.BigDecimal

/**
 * @Description: socket 和http 复用一个模型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/11 11:01
 */
open class TradeEventDto {
    /**
     * id : 28457
     * price : 4.00000100
     * qty : 12.00000000
     * quoteQty : 48.00
     * time : 1499865549590
     * isBuyerMaker : true
     */

    /**
     * {
    "e": "aggTrade",  // 事件类型
    "E": 123456789,   // 事件时间
    "s": "BNBUSDT",    // 交易对
    "a":
    "p": "0.001",     // 成交价格
    "q": "100",       // 成交笔数
    "f": 100,         // 被归集的首个交易ID
    "l": 105,         // 被归集的末次交易ID
    "T": 123456785,   // 成交时间
    "m": true         // 买方是否是做市方。如true，则此次成交是一个主动卖出单，否则是一个主动买入单。
    }
     */

    @SerializedName("symbol", alternate = ["s"])
    var symbol: String;

    /**
     * 成交价格
     */
    @SerializedName("price", alternate = ["p"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var price: NumberFormatObject;

    /**
     * 成交量
     */
    @SerializedName("qty", alternate = ["q"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var qty: NumberFormatObject;


    /**
     *  时间
     */
    @SerializedName("time", alternate = ["T"])
    @JsonAdapter(Time_HH_c_mm_c_ss_FormatTypeAdapter::class)
    var time: TimeFormatObject;

    /**
     * 买方是否为挂单方
     */
    @SerializedName("isBuyerMaker", alternate = ["m"])
    var isBuyerMaker: Boolean = false


    constructor(symbol: String, price: NumberFormatObject, qty: NumberFormatObject, time: TimeFormatObject, isBuyerMaker: Boolean) {
        this.symbol = symbol
        this.price = price
        this.qty = qty
        this.time = time
        this.isBuyerMaker = isBuyerMaker
    }


    override fun toString(): String {
        return "TradeEventDto(symbol='$symbol', price=$price, qty=$qty, time=$time, isBuyerMaker=$isBuyerMaker)"
    }
}