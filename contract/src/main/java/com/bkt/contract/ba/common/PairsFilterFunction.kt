package com.bkt.contract.ba.common

import com.bkt.contract.ba.enums.BaContractType
import com.bkt.contract.ba.model.po.PairInfoPo
import io.reactivex.functions.Function

/**
 * @Description: 交易对过滤 现在只需要永续合约 其他合约暂时不要
 * @Author: XGod
 * @CreateDate: 2020/12/24 15:51
 */
class PairsFilterFunction : Function<List<PairInfoPo>, List<PairInfoPo>> {
    override fun apply(t: List<PairInfoPo>): List<PairInfoPo> {
        val filter = t.filter {
            it.config != null;
        }
        return filter;
    }
}