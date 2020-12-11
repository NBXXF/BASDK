package com.bkt.contract.ba.model.dto

import java.math.BigDecimal

/**
 * @Description: 指数价
 * @Author: XGod
 * @CreateDate: 2020/12/11 17:16
 */
class PremiumIndexPriceDto {
    /**
     * symbol : BTCUSDT
     * markPrice : 11793.63104562
     * indexPrice : 11781.80495970
     * lastFundingRate : 0.00038246
     * nextFundingTime : 1597392000000
     * interestRate : 0.00010000
     * time : 1597370495002
     */
    /**
     * 交易对
     */
    val symbol: String;

    /**
     * 标记价格
     */
    val markPrice: BigDecimal

    /**
     * 指数价格
     */
    val indexPrice: BigDecimal

    /**
     * 最近更新的资金费率
     */
    val lastFundingRate: BigDecimal

    /**
     * 下次资金费时间
     */
    val nextFundingTime: Long;

    /**
     * 标的资产基础利率
     */
    val interestRate: BigDecimal;

    /**
     *  更新时间
     */
    val time: Long;

    constructor(symbol: String, markPrice: BigDecimal, indexPrice: BigDecimal, lastFundingRate: BigDecimal, nextFundingTime: Long, interestRate: BigDecimal, time: Long) {
        this.symbol = symbol
        this.markPrice = markPrice
        this.indexPrice = indexPrice
        this.lastFundingRate = lastFundingRate
        this.nextFundingTime = nextFundingTime
        this.interestRate = interestRate
        this.time = time
    }


    override fun toString(): String {
        return "PremiumIndexPriceDto(symbol=$symbol, markPrice=$markPrice, indexPrice=$indexPrice, lastFundingRate=$lastFundingRate, nextFundingTime=$nextFundingTime, interestRate=$interestRate, time=$time)"
    }

}