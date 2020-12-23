package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Description: 买卖方向
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/1 20:44
 */
enum class Side(val value: String) : Serializable {
    /**
     * 买入
     */
    @SerializedName("BUY")
    BUY("BUY"),

    /**
     * 卖出
     */
    @SerializedName("SELL")
    SELL("SELL"),
}