package com.bkt.contract.ba.service

import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_percent_auto_2_2_DOWN_Signed_FormatTypeAdapter
import com.xxf.arch.utils.NumberUtils
import java.math.BigDecimal
import kotlin.math.max

/**
 * @Description: 公共服务
 * @Author: XGod
 * @CreateDate: 2020/12/12 16:37
 */
internal interface CommonService : ExportService {

    companion object {
        internal val INSTANCE: CommonService by lazy {
            object : CommonService {
            }
        }
    }

    /**
     * 计算涨跌额
     */
    fun getRiseFallAmount(closePrice: BigDecimal, openPrice: BigDecimal): BigDecimal {
        return NumberUtils.subtract(closePrice, openPrice);
    }

    /**
     * 计算涨跌额 格式化好的对象
     */
    fun getRiseFallAmountFormatObject(closePrice: BigDecimal, openPrice: BigDecimal): NumberFormatObject {
        val riseFallAmount = getRiseFallAmount(closePrice, openPrice);
        return NumberFormatObject(riseFallAmount, riseFallAmount.toPlainString());
    }

    /**
     * 计算涨跌幅
     */
    fun getRiseFallRange(closePrice: BigDecimal, openPrice: BigDecimal): BigDecimal {
        return NumberUtils.divide(NumberUtils.subtract(closePrice, openPrice), openPrice, max(closePrice.scale(), openPrice.scale()));
    }

    /**
     * 计算涨跌幅 格式化好的对象
     */
    fun getRiseFallRangeFormatObject(closePrice: BigDecimal, openPrice: BigDecimal): NumberFormatObject {
        val riseFallRange = getRiseFallRange(closePrice, openPrice);
        return NumberFormatObject(riseFallRange, Number_percent_auto_2_2_DOWN_Signed_FormatTypeAdapter().format(riseFallRange));
    }
}