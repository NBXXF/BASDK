package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Description: 持仓方向
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/1 20:47
 */
enum class PositionDirection(val value: String) : Serializable {

    /**
     * 单一持仓方向
     */
    @SerializedName("BOTH")
    BOTH("BOTH"),

    /**
     *  LONG 多头(双向持仓下)
     */
    @SerializedName("LONG")
    LONG("LONG"),

    /**
     *     SHORT 空头(双向持仓下)
     */
    @SerializedName("SHORT")
    SHORT("SHORT")
}