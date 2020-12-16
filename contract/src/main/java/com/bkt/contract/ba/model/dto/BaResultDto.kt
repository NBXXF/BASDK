package com.bkt.contract.ba.model.dto

/**
 * @Description: ba 状态的模型
 * @Author: XGod
 * @CreateDate: 2020/12/16 15:49
 */
class BaResultDto() {
    val code: Long? = 0;
    val msg: String? = null;

    override fun toString(): String {
        return "BaResultDto(code=$code, msg='$msg')"
    }
}