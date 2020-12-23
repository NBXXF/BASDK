package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: Ba合约类型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/17 19:31
 */
enum class BaContractType(val value: String) {

    /**
     * 永续合约
     */
    @SerializedName("PERPETUAL")
    PERPETUAL("PERPETUAL"),


    /**
     * 当季合约
     */
    @SerializedName("CURRENT_QUARTER")
    CURRENT_QUARTER("CURRENT_QUARTER"),


    /**
     * 次季合约
     */
    @SerializedName("PERPETUAL")
    NEXT_QUARTER("NEXT_QUARTER")
}