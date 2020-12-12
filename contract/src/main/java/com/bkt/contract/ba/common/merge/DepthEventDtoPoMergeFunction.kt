package com.bkt.contract.ba.common.merge

import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.xxf.arch.XXF
import com.xxf.arch.utils.NumberUtils
import com.xxf.database.xxf.objectbox.MergeFunction

/**
 * @Description: 合并深度
 * @Author: XGod
 * @CreateDate: 2020/12/10 18:48
 */
internal class DepthEventDtoPoMergeFunction : MergeFunction<DepthEventDtoPo> {
    /**
     * 最多20条
     */
    private val max: Int = 20;
    override fun apply(insert: DepthEventDtoPo, inserted: DepthEventDtoPo?): DepthEventDtoPo {
        /**
         * 合并 socket来的是增量
         */
        return DepthEventDtoPo(insert.symbol, merge(inserted?.bids, insert.bids), merge(inserted?.asks, insert.asks));
    }

    fun merge(olds: List<DepthEventDtoPo.BookItem>?, news: List<DepthEventDtoPo.BookItem>?): List<DepthEventDtoPo.BookItem> {
        val mergeList: MutableList<DepthEventDtoPo.BookItem> = mutableListOf();
        if (news != null) {
            mergeList.addAll(news)
        }

        if (mergeList.size < max && olds != null) {
            mergeList.addAll(olds)
        }

        val mergeMap: MutableMap<String, DepthEventDtoPo.BookItem> = mutableMapOf();
        for (bookItem in mergeList) {
            /**
             * 过滤价格小于0 或者数量小于0
             */
            if (bookItem.price.origin.toDouble() > 0.0 && bookItem.amount.origin.toDouble() > 0.0) {
                mergeMap.put(bookItem.price.format, bookItem);
            }
        }
        val sortedByDescending = mergeMap.values.sortedByDescending { it.price.origin };
        if (sortedByDescending.size > max) {
            return sortedByDescending.subList(0, max);
        }
        return sortedByDescending;
    }
}