package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: socket 事件
 * @Author: XGod
 * @CreateDate: 2020/12/3 16:10
 */
enum class SocketEvent(val value: String) {

    /**
     * 精简Ticker
     */
    @SerializedName("24hrMiniTicker")
    MiniTicker24hr("24hrMiniTicker"),

    /**
     * 完整Ticker
     */
    @SerializedName("24hrTicker")
    Ticker24hr("24hrTicker"),

    /**
     * K线
     */
    @SerializedName("kline")
    KLine("kline"),

    /**
     * 深度
     */
    @SerializedName("depthUpdate")
    DepthUpdate("depthUpdate"),

    /**
     * 成交
     */
    @SerializedName("aggTrade")
    AggTrade("aggTrade"),


    /**
     * 市价变化
     */
    @SerializedName("markPriceUpdate")
    MarkPriceUpdate("markPriceUpdate"),


    /**
     * 指数价变化
     */
    @SerializedName("indexPriceUpdate")
    IndexPriceUpdate("indexPriceUpdate")
}