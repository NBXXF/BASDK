package com.bkt.contract.ba.model.dto

/**
 * @Description: 查询持仓模式返回
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/18 11:50
 */
class PositionSideDualDto(val dualSidePosition: Boolean) {
    override fun toString(): String {
        return "PositionSideDualDto(dualSidePosition=$dualSidePosition)"
    }
}