package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Description: 订单类型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/1 20:30
 */
enum class OrderType(val value: String) : Serializable {

    /**
     * 限价单
     */
    @SerializedName("LIMIT")
    LIMIT("LIMIT"),

    /**
     * 市价单
     */
    @SerializedName("MARKET")
    MARKET("MARKET"),


    /**
     * 止损限价单
     */
    @SerializedName("STOP")
    STOP("STOP"),


    /**
     * 止损市价单
     */
    @SerializedName("STOP_MARKET")
    STOP_MARKET("STOP_MARKET"),


    /**
     * 止盈限价单
     */
    @SerializedName("TAKE_PROFIT")
    TAKE_PROFIT("TAKE_PROFIT"),

    /**
     * 止盈市价单
     */
    @SerializedName("TAKE_PROFIT_MARKET")
    TAKE_PROFIT_MARKET("TAKE_PROFIT_MARKET"),

    /**
     * 跟踪止损单
     */
    @SerializedName("TRAILING_STOP_MARKET")
    TRAILING_STOP_MARKET("TRAILING_STOP_MARKET"),
}