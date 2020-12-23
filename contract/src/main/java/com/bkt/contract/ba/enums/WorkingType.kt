package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: 条件价格触发类型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/2 9:48
 */
enum class WorkingType(val value:String) {

    @SerializedName("MARK_PRICE")
    MARK_PRICE("MARK_PRICE"),

    @SerializedName("CONTRACT_PRICE")
    CONTRACT_PRICE("CONTRACT_PRICE")
}