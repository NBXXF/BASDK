package com.bkt.contract.ba.common.jsontypeadapter

import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import java.math.BigDecimal

/**
 * @Description: 绝对值
 * @Author: XGod
 * @CreateDate: 2020/12/23 14:54
 */
class Number_Abs_UNFormatTypeAdapter: Number_UNFormatTypeAdapter() {

    override fun format(origin: BigDecimal): String {
        return super.format(origin.abs())
    }
}