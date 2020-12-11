package com.bkt.contract.ba.common.merge

import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.xxf.arch.XXF
import com.xxf.database.xxf.objectbox.MergeFunction

/**
 * @Description: 合并深度
 * @Author: XGod
 * @CreateDate: 2020/12/10 18:48
 */
internal class DepthEventDtoPoMergeFunction : MergeFunction<DepthEventDtoPo> {
    private val max: Int = 50;
    override fun apply(insert: DepthEventDtoPo, inserted: DepthEventDtoPo?): DepthEventDtoPo {
        /**
         * 合并 socket来的是增量
         */
        return DepthEventDtoPo(insert.symbol, merge(inserted?.bids, insert.bids), merge(inserted?.asks, insert.asks));
    }

    fun merge(olds: List<DepthEventDtoPo.BookItem>?, news: List<DepthEventDtoPo.BookItem>?): List<DepthEventDtoPo.BookItem> {
        val mergeList: MutableList<DepthEventDtoPo.BookItem> = mutableListOf();
        if (olds != null) {
            mergeList.addAll(olds)
        };
        if (news != null) {
            mergeList.addAll(news)
        }


        val mergeMap: MutableMap<String, DepthEventDtoPo.BookItem> = mutableMapOf();
        for (bookItem in mergeList) {
            mergeMap.put(bookItem.price.toPlainString(), bookItem);
        }
        val sortedByDescending = mergeMap.values.sortedByDescending { it.price };
        if (sortedByDescending.size > max) {
            return sortedByDescending.subList(0, max);
        }
        return sortedByDescending;
    }
}