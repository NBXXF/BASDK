package com.bkt.contract.ba.model.dto

import java.math.BigDecimal

/**
 * @Description: 调整逐仓保证金 返回结果
 * @Author: XGod
 * @CreateDate: 2020/12/16 16:23
 */
class PositionMarginResultDto : BaResultDto() {
    val amount:BigDecimal?=null;
}