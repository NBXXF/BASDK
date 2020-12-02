package com.bkt.basdk.model

/**
 * @Description: TODO @XGode
 * @Author: XGod
 * @CreateDate: 2020/12/2 12:10
 */
class HttpStatus {
    val status: Int = 0;
    val message: String? = null;
    override fun toString(): String {
        return "HttpStatus(status=$status, message=$message)"
    }
}