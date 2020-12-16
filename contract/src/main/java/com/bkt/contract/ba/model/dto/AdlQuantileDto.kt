package com.bkt.contract.ba.model.dto

/**
 * @Description: 持仓ADL队列估算 (USER_DATA)
 *
 *
 * https://binance-docs.github.io/apidocs/testnet/cn/#user_data-8
 * @Author: XGod
 * @CreateDate: 2020/12/16 18:11
 */
class AdlQuantileDto {

    var symbol: String? = null

    var adlQuantile: AdlQuantileItem? = null;

    class AdlQuantileItem {
        /**
         * v1/adlQuantile
         * // 对于单向持仓模式或者是逐仓状态下的双向持仓模式的交易对，会返回 "LONG", "SHORT" 和 "BOTH" 分别表示不同持仓方向上持仓的adl队列分数
         * "LONG": 1,  // 双开模式下多头持仓的ADL队列估算分
         * "SHORT": 2,     // 双开模式下空头持仓的ADL队列估算分
         * "BOTH": 0       // 单开模式下持仓的ADL队列估算分
         * "HEDGE": 0   // HEDGE 仅作为指示出现, 请忽略数值
         * }
         * }
         * ]
         *
         * 每30秒更新数据
         * 队列分数0，1，2，3，4，分数越高说明在ADL队列中的位置越靠前
         * 对于单向持仓模式或者是逐仓状态下的双向持仓模式的交易对，会返回 "LONG", "SHORT" 和 "BOTH" 分别表示不同持仓方向上持仓的adl队列分数
         * 对于全仓状态下的双向持仓模式的交易对，会返回 "LONG", "SHORT" 和 "HEDGE", 其中"HEDGE"的存在仅作为标记;其中如果多空均有持仓的情况下,"LONG"和"SHORT"返回共同计算后相同的队列分数。
         */
        /**
         * 双开模式下多头持仓的ADL队列估算分[0...4]
         */
        var LONG: Int = 0

        /**
         * 双开模式下空头持仓的ADL队列估算分[0...4]
         */
        var SHORT: Int = 0

        /**
         * HEDGE 仅作为指示出现, 请忽略数值[0...4]
         */
        var HEDGE: Int = 0

        /**
         * 单开模式下持仓的ADL队列估算分[0...4]
         */
        var BOTH: Int = 0


        override fun toString(): String {
            return "AdlQuantileItem(LONG=$LONG, SHORT=$SHORT, HEDGE=$HEDGE, BOTH=$BOTH)"
        }
    }

    override fun toString(): String {
        return "AdlQuantileDto(symbol=$symbol, adlQuantile=$adlQuantile)"
    }


}