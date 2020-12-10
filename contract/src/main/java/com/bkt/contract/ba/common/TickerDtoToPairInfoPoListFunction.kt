package com.bkt.contract.ba.common

import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.po.PairInfoPo
import io.reactivex.functions.Function

/**
 * @Description: ticker dto转pairinfo po
 * @Author: XGod
 * @CreateDate: 2020/12/10 10:01
 */
internal class TickerDtoToPairInfoPoListFunction : Function<List<TickerEventDto>, List<PairInfoPo>> {
    override fun apply(t: List<TickerEventDto>): List<PairInfoPo> {
        val list: MutableList<PairInfoPo> = mutableListOf();
        for (item: TickerEventDto in t) {
            val pairInfoPo = PairInfoPo(item.symbol);
            pairInfoPo.ticker = item;
            list.add(pairInfoPo);
        }
        return list;
    }
}