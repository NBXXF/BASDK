package com.bkt.contract.ba.model.dto

import com.bkt.contract.ba.common.jsontypeadapter.Number_percent_auto_0_4_DOWN_FormatTypeAdapter
import com.bkt.contract.ba.service.PairService
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import com.xxf.arch.utils.NumberUtils
import java.lang.reflect.Type

/**
 * @Description: 指数价 http socket 复用一个
 * @Author: XGod
 * @CreateDate: 2020/12/11 17:16
 */
@JsonAdapter(PremiumIndexPriceDto.PremiumIndexPriceFormatTypeAdapter::class)
open class PremiumIndexPriceDto {

    internal class PremiumIndexPriceFormatTypeAdapter : JsonDeserializer<PremiumIndexPriceDto> {

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PremiumIndexPriceDto {
            val pre: PremiumIndexPriceDto = context!!.deserialize(json, PremiumIndexPriceWithoutJsonAdapterDto::class.java);
            try {
                val config = PairService.INSTANCE.getPairConfigs(pre.symbol);
                /**
                 * 接口并没有格式化
                 */
                if (config != null) {
                    if (pre.indexPrice != null) {
                        pre.indexPrice = NumberFormatObject(pre.indexPrice.origin, NumberUtils.formatRoundDown(pre.indexPrice.origin, 0, config.pricePrecision))
                    }
                    if (pre.markPrice != null) {
                        pre.markPrice = NumberFormatObject(pre.markPrice.origin, NumberUtils.formatRoundDown(pre.markPrice.origin, 0, config.pricePrecision))
                    }
                    if (pre.estimatedSettlePrice != null) {
                        pre.estimatedSettlePrice = NumberFormatObject(pre.estimatedSettlePrice.origin, NumberUtils.formatRoundDown(pre.estimatedSettlePrice.origin, 0, config.pricePrecision))
                    }
                };
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return pre;
        }
    }

    /**
     * 避免循环调用
     */
    internal class PremiumIndexPriceWithoutJsonAdapterDto : PremiumIndexPriceDto {


        constructor(symbol: String,
                    markPrice: NumberFormatObject,
                    indexPrice: NumberFormatObject,
                    estimatedSettlePrice: NumberFormatObject,
                    lastFundingRate: NumberFormatObject,
                    nextFundingTime: Long) : super(symbol,
                markPrice,
                indexPrice,
                estimatedSettlePrice,
                lastFundingRate,
                nextFundingTime) {
        }
    }
    /**
     * /fapi/v1/premiumIndex
     *
     * symbol : BTCUSDT
     * markPrice : 11793.63104562
     * indexPrice : 11781.80495970
     * lastFundingRate : 0.00038246
     * nextFundingTime : 1597392000000
     * interestRate : 0.00010000
     * time : 1597370495002
     */

    /**
     * <symbol>@markPrice
     *
     *   {
    "e": "markPriceUpdate",     // 事件类型
    "E": 1562305380000,         // 事件时间
    "s": "BTCUSDT",             // 交易对
    "p": "11794.15000000",      // 标记价格
    "i": "11784.62659091",      // 现货指数价格
    "P": "11784.25641265",      // 预估结算价,仅在结算前最后一小时有参考价值
    "r": "0.00038167",          // 资金费率
    "T": 1562306400000          // 下次资金时间
    }
     */
    /**
     * 交易对
     */
    @SerializedName("symbol", alternate = ["s"])
    val symbol: String;

    /**
     * 标记价格
     */
    @SerializedName("markPrice", alternate = ["p"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var markPrice: NumberFormatObject

    /**
     * 指数价格,
     * USDT scoket 有,USD Socket没有该字段注意!!!
     * http接口 usdt和usd都有
     */
    @SerializedName("indexPrice", alternate = ["i"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var indexPrice: NumberFormatObject

    /**
     *  预估结算价,仅在交割开始前最后一小时有意义
     */
    @SerializedName("estimatedSettlePrice", alternate = ["P"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var estimatedSettlePrice: NumberFormatObject;

    /**
     * 最近更新的资金费率
     */
    @SerializedName("lastFundingRate", alternate = ["r"])
    @JsonAdapter(Number_percent_auto_0_4_DOWN_FormatTypeAdapter::class)
    val lastFundingRate: NumberFormatObject

    /**
     * 下次资金费时间
     */
    @SerializedName("nextFundingTime", alternate = ["t"])
    val nextFundingTime: Long;

    constructor(symbol: String, markPrice: NumberFormatObject, indexPrice: NumberFormatObject, estimatedSettlePrice: NumberFormatObject, lastFundingRate: NumberFormatObject, nextFundingTime: Long) {
        this.symbol = symbol
        this.markPrice = markPrice
        this.indexPrice = indexPrice
        this.estimatedSettlePrice = estimatedSettlePrice
        this.lastFundingRate = lastFundingRate
        this.nextFundingTime = nextFundingTime
    }

    override fun toString(): String {
        return "PremiumIndexPriceDto(symbol='$symbol', markPrice=$markPrice, indexPrice=$indexPrice, estimatedSettlePrice=$estimatedSettlePrice, lastFundingRate=$lastFundingRate, nextFundingTime=$nextFundingTime)"
    }


}