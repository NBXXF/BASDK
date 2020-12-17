package com.bkt.contract.ba.model.dto

import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter

/**
 * @Description: T调整杠杆倍数返回结果
 * @Author: XGod
 * @CreateDate: 2020/12/17 10:02
 */
class ChangeLeverageResDto {

    var symbol: String? = null;

    /**
     *  杠杆倍数
     */
    val leverage: Int = 0;

    /**
     * 当前杠杆倍数允许的名义价值上限
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var maxNotionalValue: NumberFormatObject? = null


    override fun toString(): String {
        return "ChangeLeverageResultDto(symbol=$symbol, leverage=$leverage, maxNotionalValue=$maxNotionalValue)"
    }


}