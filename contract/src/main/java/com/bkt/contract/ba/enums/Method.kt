package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Description: 订阅取消订阅枚举
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/10 10:53
 */
enum class Method(val value: String) : Serializable {
    /**
     * 订阅
     */
    @SerializedName("SUBSCRIBE")
    SUBSCRIBE("SUBSCRIBE"),

    /**
     * 取消订阅
     */
    @SerializedName("UNSUBSCRIBE")
    UNSUBSCRIBE("UNSUBSCRIBE"),
}