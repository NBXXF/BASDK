package com.bkt.contract.ba.model.dto

/**
 * @Description: 查询持仓模式返回
 * @Author: XGod
 * @CreateDate: 2020/12/18 11:50
 */
class PositionSideDualDto(val dualSidePosition: Boolean) {
    override fun toString(): String {
        return "PositionSideDualDto(dualSidePosition=$dualSidePosition)"
    }
}