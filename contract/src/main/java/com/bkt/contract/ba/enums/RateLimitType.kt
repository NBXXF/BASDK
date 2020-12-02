package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: 限制种类
 * @Author: XGod
 * @CreateDate: 2020/12/2 9:50
 */
enum class RateLimitType(val value: String) {

    /**
     * 单位时间请求权重之和上限
     */
    @SerializedName("REQUESTS_WEIGHT")
    REQUESTS_WEIGHT("REQUESTS_WEIGHT"),

    /**
     * 单位时间下单(撤单)次数上限
     */
    @SerializedName("ORDERS")
    ORDERS("ORDERS")
}