package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: 合约类型
 * @Author: XGod
 * @CreateDate: 2020/12/8 14:27
 */
enum class ContractType(val value: String) {
    /**
     * USDT合约
     */
    @SerializedName("USDT")
    USDT("USDT"),

    /**
     * 币本位合约
     */
    @SerializedName("USD")
    USD("USD");

    companion object {
        /**
         * 转换方法
         */
        fun from(value: String): ContractType? {
            try {
                return valueOf(value);
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null;
        }
    }
}