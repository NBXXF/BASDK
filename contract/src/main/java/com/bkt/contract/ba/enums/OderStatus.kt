package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Description: 订单状态
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/1 20:18
 *
 * 文档地址:https://binance-docs.github.io/apidocs/futures/cn/#api
 */
enum class OderStatus(val value: String) : Serializable {

    /**
     * 新建订单
     */
    @SerializedName("NEW")
    NEW("NEW"),

    /**
     * 部分成交
     */
    @SerializedName("PARTIALLY_FILLED")
    PARTIALLY_FILLED("PARTIALLY_FILLED"),

    /**
     * 全部成交
     */
    @SerializedName("FILLED")
    FILLED("FILLED"),

    /**
     * 已撤销
     */
    @SerializedName("CANCELED")
    CANCELED("CANCELED"),

    /**
     * 订单被拒绝
     */
    @SerializedName("REJECTED")
    REJECTED("REJECTED"),

    /**
     * 订单过期(根据timeInForce参数规则)
     */
    @SerializedName("EXPIRED")
    EXPIRED("EXPIRED")
}