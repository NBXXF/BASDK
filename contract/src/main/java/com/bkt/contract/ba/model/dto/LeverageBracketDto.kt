package com.bkt.contract.ba.model.dto

import android.text.TextUtils
import com.bkt.contract.ba.service.CommonService
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import java.lang.reflect.Type

/**
 * @Description: 杠杆分层标准
 * @Author: XGod
 * @CreateDate: 2020/12/16 9:52
 */
class LeverageBracketDto {
    /**
     * 注意两套接口 有差异
     * /fapi/v1/leverageBracket
     *
     *
     * {
     * "symbol": "ETHUSDT",
     * "brackets": [
     * {
     * "bracket": 1,   // 层级
     * "initialLeverage": 75,  // 该层允许的最高初始杠杆倍数
     * "notionalCap": 10000,  // 该层对应的名义价值上限
     * "notionalFloor": 0,  // 该层对应的名义价值下限
     * "maintMarginRatio": 0.0065, // 该层对应的维持保证金率
     * "cum":0. // 速算数
     * },
     * ]
     * }
     *
     *
     * dapi/v1/leverageBracket
     *
     *
     * {
     * "pair": "BTCUSD",
     * "brackets": [
     * {
     * "bracket": 1,   // 层级
     * "initialLeverage": 125,  // 该层允许的最高初始杠杆倍数
     * "qtyCap": 50,  // 该层对应的数量上限
     * "qtylFloor": 0,  // 该层对应的数量下限
     * "maintMarginRatio": 0.004 // 该层对应的维持保证金率
     * },
     * ]
     * }
     * ]
     */
    /**
     * 注意！！！！！
     * 接口USDT 有symbol字段 无 pair
     * 接口USD有pair 无symbol
     *
     * 复写了get方法 所以pair一定是有值的
     */
    val symbol: String? = null
    val pair: String? = null
        get() {
            if (TextUtils.isEmpty(field)) {
                return CommonService.INSTANCE.convertPair(symbol);
            }
            return field;
        }
    val brackets: List<BracketsBean>? = null


    class BracketsBean {
        /**
         * bracket : 1
         * initialLeverage : 75
         * notionalCap : 10000
         * notionalFloor : 0
         * maintMarginRatio : 0.0065
         * cum : 0.0
         */
        /**
         * 层级
         */
        val bracket: Int = 0

        /**
         * 该层允许的最高初始杠杆倍数
         */
        val initialLeverage: Int = 0

        /**
         * 该层对应的数量上限
         */
        val notionalCap: Double = 0.0

        /**
         * 该层对应的数量下限
         */
        val notionalFloor: Double = 0.0

        /**
         * 该层对应的维持保证金率
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val maintMarginRatio: NumberFormatObject? = null

        /**
         * 速算数
         */
        val cum = 0.0

        override fun toString(): String {
            return "BracketsBean(bracket=$bracket, initialLeverage=$initialLeverage, notionalCap=$notionalCap, notionalFloor=$notionalFloor, maintMarginRatio=$maintMarginRatio, cum=$cum)"
        }


    }

    override fun toString(): String {
        return "LeverageBracketDto(symbol=$symbol, pair=$pair, brackets=$brackets)"
    }


}