package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: order socket事件的具体执行类型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/17 15:19
 */
enum class OrderEventType(val value: String) {
    /**
     * 本次事件的具体执行类型

    NEW
    PARTIAL_FILL 部分成交
    FILL 成交
    CANCELED 已撤
    CALCULATED
    EXPIRED 订单失效
    TRADE 交易
     */
    @SerializedName(" NEW")
    NEW(" NEW"),

    @SerializedName("PARTIAL_FILL")
    PARTIAL_FILL("PARTIAL_FILL"),

    @SerializedName("FILL")
    FILL("FILL"),

    @SerializedName("CANCELED")
    CANCELED("CANCELED"),

    @SerializedName("CALCULATED")
    CALCULATED("CALCULATED"),

    @SerializedName("EXPIRED")
    EXPIRED("EXPIRED"),

    @SerializedName("TRADE")
    TRADE("TRADE")
}