package com.bkt.contract.ba.model.dto

import com.bkt.contract.ba.enums.IncomeType
import com.bkt.contract.ba.model.PairConfigProviderModel
import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.formatobject.TimeFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_yyyy_s_MM_s_dd_HH_c_mm_FormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter

/**
 * @Description: 资金流水 收益
 * @Author: XGod
 * @CreateDate: 2020/12/15 15:25
 */
open class IncomeDto : PairConfigProviderModel {
    /**
     * v1/income (HMAC SHA256)
     *  {
    "symbol": "", // 交易对，仅针对涉及交易对的资金流
    "incomeType": "TRANSFER",   // 资金流类型
    "income": "-0.37500000", // 资金流数量，正数代表流入，负数代表流出
    "asset": "USDT", // 资产内容
    "info":"TRANSFER", // 备注信息，取决于流水类型
    "time": 1570608000000, // 时间
    "tranId":"9689322392",      // 划转ID
    "tradeId":""                    // 引起流水产生的原始交易ID
    }
     */
    var symbol: String? = null

    var incomeType: IncomeType? = null

    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var income: NumberFormatObject? = null

    var asset: String? = null

    var info: String? = null


    @JsonAdapter(Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter::class)
    var time: TimeFormatObject? = null;

    var tranId: String? = null

    var tradeId: String? = null


    override fun provideSymbol(): String? {
        return this.symbol;
    }


    override fun toString(): String {
        return "IncomeDto(symbol=$symbol, incomeType=$incomeType, income=$income, asset=$asset, info=$info, time=$time, tranId=$tranId, tradeId=$tradeId)"
    }

}