package com.bkt.contract.ba.common.merge

import android.text.TextUtils
import com.bkt.contract.ba.model.po.PairInfoPo
import com.xxf.database.xxf.objectbox.ListMergeFunction

/**
 * @Description: 交易对信息合并
 * @Author: XGod
 * @CreateDate: 2020/12/7 10:46
 */
internal class PairInfoPoListMergeFunction : ListMergeFunction<PairInfoPo> {
    override fun apply(insert: MutableList<PairInfoPo>, inserted: MutableMap<Long, PairInfoPo>): MutableList<PairInfoPo> {
        for (insertItem: PairInfoPo in insert) {
            if (insertItem != null) {
                val insertedItem: PairInfoPo? = inserted.get(insertItem._id);
                if (insertedItem != null) {
                    if (insertItem.ticker == null && insertedItem.ticker != null) {
                        insertItem.ticker == insertedItem.ticker;
                    }
                    if (insertItem.config == null && insertedItem.config != null) {
                        insertItem.config = insertedItem.config;
                    }
                    if (insertItem.index <= 0 && insertedItem.index > 0) {
                        insertItem.index = insertedItem.index;
                    }
                    if (TextUtils.isEmpty(insertItem.contractType) && !TextUtils.isEmpty(insertedItem.contractType)) {
                        insertItem.contractType = insertedItem.contractType;
                    }
                }
            }
        }
        return insert;
    }
}