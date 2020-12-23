package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Description: 有效方式 (timeInForce):
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/1 20:50
 */
enum class TimeInForce(val value: String):Serializable {
    /**
     * GTC - Good Till Cancel 成交为止
     */
    @SerializedName("GTC")
    GTC("GTC"),

    /**
     *  IOC - Immediate or Cancel 无法立即成交(吃单)的部分就撤销
     */
    @SerializedName("IOC")
    IOC("IOC"),

    /**
     *  FOK - Fill or Kill 无法全部立即成交就撤销
     */
    @SerializedName("FOK")
    FOK("FOK"),

    /**
     * GTX - Good Till Crossing 无法成为挂单方就撤销
     */
    @SerializedName("GTX")
    GTX("GTX")
}