package com.bkt.contract.ba.common.jsontypeadapter

import com.xxf.arch.json.typeadapter.format.NumberObjectFormatTypeAdapter
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

/**
 * @Description: 百分数格式化 0-4位小数, 安卓源码会自动*100
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/5 15:36
 *
 *
 * 用[com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject]接收
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
class Number_percent_auto_0_4_DOWN_FormatTypeAdapter : NumberObjectFormatTypeAdapter() {
    @Throws(Exception::class)
    override fun format(origin: BigDecimal): String {
        val percentInstance = NumberFormat.getPercentInstance(Locale.CHINA)
        percentInstance.isGroupingUsed = false
        percentInstance.minimumFractionDigits = 0
        percentInstance.maximumFractionDigits = 4
        percentInstance.roundingMode = RoundingMode.DOWN
        return percentInstance.format(origin)
    }
}