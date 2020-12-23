package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: 收益类型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/15 15:00
 */
enum class IncomeType(val value: String) {

    /**
     * 公开文档只有这几种
     * 收益类型 "TRANSFER"，"WELCOME_BONUS", "REALIZED_PNL"，"FUNDING_FEE", "COMMISSION", and "INSURANCE_CLEAR" "DELIVERED_SETTELMENT"
     *
     * 而ba前端代码太多种
     * 从前端代码拷贝而来
     *     <li role="option" id="ALL" title="ALL" class="bnb-active-option css-14exitd">所有</li>
     *     <li role="option" id="TRANSFER" title="TRANSFER" class=" css-14exitd">转账</li>
     *     <li role="option" id="COIN_SWAP" title="COIN_SWAP" class=" css-14exitd">资产转换划转</li>
     *     <li role="option" id="REALIZED_PNL" title="REALIZED_PNL" class=" css-14exitd">实现盈亏</li>
     *     <li role="option" id="FUNDING_FEE" title="FUNDING_FEE" class=" css-14exitd">资金费率</li>
     *     <li role="option" id="COMMISSION" title="COMMISSION" class=" css-14exitd">手续费</li>
     *     <li role="option" id="INSURANCE_CLEAR" title="INSURANCE_CLEAR" class=" css-14exitd">爆仓清算</li>
     *     <li role="option" id="OPTIONS_SETTLE_PROFIT" title="OPTIONS_SETTLE_PROFIT" class=" css-14exitd">期权行权收益</li>
     *     <li role="option" id="REFERRAL_KICKBACK" title="REFERRAL_KICKBACK" class=" css-14exitd">推荐人返佣</li>
     *     <li role="option" id="COMMISSION_REBATE" title="COMMISSION_REBATE" class=" css-14exitd">被推荐人返佣</li>
     *     <li role="option" id="CROSS_COLLATERAL_TRANSFER" title="CROSS_COLLATERAL_TRANSFER" class=" css-14exitd">CC转账</li>
     *     <li role="option" id="OPTIONS_PREMIUM_FEE" title="OPTIONS_PREMIUM_FEE" class=" css-14exitd">期权购置手续费</li></ul>
     */
    /**
     * 转账
     */
    @SerializedName("TRANSFER")
    TRANSFER("TRANSFER"),


    /**
     * TODO 中文意思
     */
    @SerializedName("WELCOME_BONUS")
    WELCOME_BONUS("WELCOME_BONUS"),


    /**
     * 实现盈亏
     */
    @SerializedName("REALIZED_PNL")
    REALIZED_PNL("REALIZED_PNL"),


    /**
     * 资金费率
     */
    @SerializedName("FUNDING_FEE")
    FUNDING_FE("FUNDING_FEE"),


    /**
     * 手续费
     */
    @SerializedName("COMMISSION")
    COMMISSION("COMMISSION"),

    /**
     * 爆仓清算
     */
    @SerializedName("INSURANCE_CLEAR")
    INSURANCE_CLEAR("INSURANCE_CLEAR"),

    /**
     * 期权行权收益
     */
    @SerializedName("OPTIONS_SETTLE_PROFIT")
    OPTIONS_SETTLE_PROFIT("OPTIONS_SETTLE_PROFIT"),


    /**
     * 推荐人返佣
     */
    @SerializedName("REFERRAL_KICKBACK")
    REFERRAL_KICKBACK("REFERRAL_KICKBACK"),


    /**
     * 被推荐人返佣
     */
    @SerializedName("COMMISSION_REBATE")
    COMMISSION_REBATE("COMMISSION_REBATE"),


    /**
     * CC转账
     */
    @SerializedName("CROSS_COLLATERAL_TRANSFER")
    CROSS_COLLATERAL_TRANSFER("CROSS_COLLATERAL_TRANSFER"),


    /**
     * 期权购置手续费
     */
    @SerializedName("OPTIONS_PREMIUM_FEE")
    OPTIONS_PREMIUM_FEE("OPTIONS_PREMIUM_FEE"),


    /**
     * TODO 中文意思
     */
    @SerializedName("DELIVERED_SETTELMENT")
    DELIVERED_SETTELMENT("DELIVERED_SETTELMENT")
}