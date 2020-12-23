package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Description: (逐仓) (全仓)
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/15 13:45
 */
enum class MarginType (val value: String) : Serializable {
    /**
     *(逐仓)
     */
    @SerializedName("ISOLATED",alternate = ["isolated"])
    ISOLATED("ISOLATED"),

    /**
     * (全仓)
     */
    @SerializedName("CROSSED",alternate = ["crossed"])
    CROSSED("CROSSED"),
}