package com.bkt.contract.ba.model.dto

/**
 * @Description: ba 状态的模型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/16 15:49
 */
open class BaResultDto {
    val code: Long? = 0;
    val msg: String? = null;

    override fun toString(): String {
        return "BaResultDto(code=$code, msg='$msg')"
    }
}