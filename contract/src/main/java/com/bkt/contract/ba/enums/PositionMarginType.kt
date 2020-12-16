package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: 调整保证金模式
 * @Author: XGod
 * @CreateDate: 2020/12/16 16:28
 */
enum class PositionMarginType(val value: Int) {
    /**
     * 增加保证金
     */
    @SerializedName("1")
    INCREASE(1),

    /**
     * 减少保证金
     */
    @SerializedName("2")
    DECREASE(2)
}