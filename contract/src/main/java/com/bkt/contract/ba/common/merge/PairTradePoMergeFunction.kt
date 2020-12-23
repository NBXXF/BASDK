package com.bkt.contract.ba.common.merge

import com.bkt.contract.ba.model.dto.TradeEventDto
import com.bkt.contract.ba.model.po.PairTradePo
import com.xxf.database.xxf.objectbox.MergeFunction

/**
 * @Description: 交易对对应的成交
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/11 14:25
 */
class PairTradePoMergeFunction : MergeFunction<PairTradePo> {
    private val max = 20;
    override fun apply(insert: PairTradePo, inserted: PairTradePo?): PairTradePo {
        val map: MutableMap<Long, TradeEventDto> = mutableMapOf();
        inserted?.trades?.forEach {
            map.put(it.time.origin, it);
        }
        insert.trades.forEach {
            map.put(it.time.origin, it);
        }
        var sortedByDescending = map.values.sortedByDescending { it.time.origin };
        if (sortedByDescending.size > max) {
            sortedByDescending = sortedByDescending.subList(0, max)
        }
        insert.trades = sortedByDescending;
        return insert;
    }
}