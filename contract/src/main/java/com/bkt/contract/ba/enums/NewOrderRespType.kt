package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description:
 * @Author: XGod
 * @CreateDate: 2020/12/14 14:18
 */
enum class NewOrderRespType(val value: String) {
    @SerializedName("ACK")
    ACK("ACK"),

    @SerializedName("RESULT")
    RESULT("RESULT");
}