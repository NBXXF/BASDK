package com.bkt.contract.ba.common

import com.bkt.contract.ba.model.po.PairInfoPo
import io.reactivex.functions.Function

/**
 * @Description: 交易对信息转换成name list
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/10 9:43
 */
internal class PairInfoToPairNameListFunction : Function<List<PairInfoPo>, List<String>> {
    override fun apply(t: List<PairInfoPo>): List<String> {
        val list: MutableList<String> = mutableListOf();
        t.forEach {
            it.symbol?.let { it1 -> list.add(it1) };
        }
        return list;
    }
}