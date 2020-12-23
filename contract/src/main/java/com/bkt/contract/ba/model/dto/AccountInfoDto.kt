package com.bkt.contract.ba.model.dto

import com.bkt.contract.ba.enums.PositionDirection
import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter

/**
 * @Description: 账户详细信息 v1/account (HMAC SHA256)
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/18 15:12
 */
class AccountInfoDto {

    /**
     * // 资产内容
     */
    val assets: List<BalanceDetailsDto>? = null;

    /**
     * // 头寸？
     */
    val positions: List<PositionDetailsDto>? = null;

    /**
     * 是否可以入金
     */
    val canDeposit: Boolean = false;

    /**
     * 是否可以交易
     */
    val canTrade: Boolean = false;

    /**
     *  是否可以出金
     */
    val canWithdraw: Boolean = false;

    /**
     * 手续费等级
     */
    val feeTier: Int = 0;


    val updateTime: Long = 0;

    /**
     * 余额详细信息
     */
    class BalanceDetailsDto {
        /**
         * {
         * "asset": "BTC",  // 资产名
         * "walletBalance": "0.00241969",  // 账户余额
         * "unrealizedProfit": "0.00000000",  // 全部持仓未实现盈亏
         * "marginBalance": "0.00241969",  // 保证金余额
         * "maintMargin": "0.00000000",    // 维持保证金
         * "initialMargin": "0.00000000",  // 当前所需起始保证金(按最新标标记价格)
         * "positionInitialMargin": "0.00000000",  // 当前所需持仓起始保证金(按最新标标记价格)
         * "openOrderInitialMargin": "0.00000000",  // 当前所需挂单起始保证金(按最新标标记价格)
         * "maxWithdrawAmount": "0.00241969",  // 最大可提款金额
         * "crossWalletBalance": "0.00241969",  // 可用于全仓的账户余额
         * "crossUnPnl": "0.00000000",  // 所有全仓持仓的未实现盈亏
         * "availableBalance": "0.00241969"  // 可用下单余额
         * }
         *
         */
        /**
         * 资产名
         */
        var asset: String? = null

        /**
         * 账户余额
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var walletbalance: NumberFormatObject? = null

        /**
         * 全部持仓未实现盈亏
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var unrealizedprofit: NumberFormatObject? = null

        /**
         * 保证金余额
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var marginbalance: NumberFormatObject? = null

        /**
         * 维持保证金
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var maintmargin: NumberFormatObject? = null

        /**
         * 当前所需起始保证金(按最新标标记价格)
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var initialmargin: NumberFormatObject? = null

        /**
         * 当前所需持仓起始保证金(按最新标标记价格)
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var positioninitialmargin: NumberFormatObject? = null

        /**
         * 当前所需挂单起始保证金(按最新标标记价格)
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var openorderinitialmargin: NumberFormatObject? = null

        /**
         * 最大可提款金额
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var maxwithdrawamount: NumberFormatObject? = null

        /**
         * 可用于全仓的账户余额
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var crosswalletbalance: NumberFormatObject? = null

        /**
         * 所有全仓持仓的未实现盈亏
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var crossunpnl: NumberFormatObject? = null

        /**
         * 可用下单余额
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        var availablebalance: NumberFormatObject? = null


        override fun toString(): String {
            return "BalanceDetailsDto(asset=$asset, walletbalance=$walletbalance, unrealizedprofit=$unrealizedprofit, marginbalance=$marginbalance, maintmargin=$maintmargin, initialmargin=$initialmargin, positioninitialmargin=$positioninitialmargin, openorderinitialmargin=$openorderinitialmargin, maxwithdrawamount=$maxwithdrawamount, crosswalletbalance=$crosswalletbalance, crossunpnl=$crossunpnl, availablebalance=$availablebalance)"
        }


    }

    /**
     * 头寸？咋理解？像是持仓
     */
    class PositionDetailsDto {
        /**
         *  "symbol": "BTCUSD_201225",  // 交易对
        "initialMargin": "0",   // 当前所需起始保证金(按最新标标记价格)
        "maintMargin": "0", // 持仓维持保证金
        "unrealizedProfit": "0.00000000",  // 持仓未实现盈亏
        "positionInitialMargin": "0",  // 当前所需持仓起始保证金(按最新标标记价格)
        "openOrderInitialMargin": "0",  // 当前所需挂单起始保证金(按最新标标记价格)
        "leverage": "125",  // 杠杆倍率
        "isolated": false,  // 是否是逐仓模式
        "positionSide": "BOTH", // 持仓方向
        "entryPrice": "0.0",    // 平均持仓成本
        "maxQty": "50"  // 当前杠杆下最大可开仓数(标的数量)
         */

        /**
         * 交易对
         */
        val symbol: String? = null;

        /**
         * 当前所需起始保证金(按最新标标记价格)
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val initialMargin: NumberFormatObject? = null;

        /**
         * 持仓维持保证金
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val maintMargin: NumberFormatObject? = null;


        /**
         * 持仓未实现盈亏
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val unrealizedProfit: NumberFormatObject? = null;


        /**
         * 当前所需持仓起始保证金(按最新标标记价格)
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val positionInitialMargin: NumberFormatObject? = null;


        /**
         * 当前所需挂单起始保证金(按最新标标记价格)
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val openOrderInitialMargin: NumberFormatObject? = null;

        /**
         * 杠杆倍率
         */
        val leverage: Int = 0;


        /**
         * 是否是逐仓模式
         */
        val isolated: Boolean = false;


        /**
         * 持仓方向
         */
        val positionSide: PositionDirection? = null;


        /**
         *  平均持仓成本
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val entryPrice: NumberFormatObject? = null;


        /**
         * 当前杠杆下最大可开仓数(标的数量)
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val maxQty: NumberFormatObject? = null;
        override fun toString(): String {
            return "PositionDetailsDto(symbol=$symbol, initialMargin=$initialMargin, maintMargin=$maintMargin, unrealizedProfit=$unrealizedProfit, positionInitialMargin=$positionInitialMargin, openOrderInitialMargin=$openOrderInitialMargin, leverage=$leverage, isolated=$isolated, positionSide=$positionSide, entryPrice=$entryPrice, maxQty=$maxQty)"
        }


    }

    override fun toString(): String {
        return "AccountInfoDto(assets=$assets, positions=$positions, canDeposit=$canDeposit, canTrade=$canTrade, canWithdraw=$canWithdraw, feeTier=$feeTier, updateTime=$updateTime)"
    }


}