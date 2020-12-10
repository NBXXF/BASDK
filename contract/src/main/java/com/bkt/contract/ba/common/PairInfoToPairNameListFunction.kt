package com.bkt.contract.ba.common

import com.bkt.contract.ba.model.po.PairInfoPo
import io.reactivex.functions.Function

/**
 * @Description: 交易对信息转换成name list
 * @Author: XGod
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