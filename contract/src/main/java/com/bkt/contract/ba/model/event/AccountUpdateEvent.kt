package com.bkt.contract.ba.model.event

import com.bkt.contract.ba.enums.MarginType
import com.bkt.contract.ba.enums.PositionDirection
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter

/**
 * @Description: 账户信息(余额+持仓) 这是ba的沙雕设计
 * https://binance-docs.github.io/apidocs/testnet/cn/#balance-position
 * @Author: XGod
 * @CreateDate: 2020/12/17 16:20
 */
class AccountUpdateEvent {
    /**
     * 账户更新事件的 event type 固定为 ACCOUNT_UPDATE

    当账户信息有变动时，会推送此事件：

    仅当账户信息有变动时(包括资金、仓位、保证金模式等发生变化)，才会推送此事件；
    订单状态变化没有引起账户和持仓变化的，不会推送此事件；
    当用户某项资产发生变化时：

    资产项目"B"中仅会推送本次发生变化的资产及其余额
    其他资产不会被推送，即便资产不为0
    如果资产变化不涉及持仓变化，持仓项目"P"将仅返回空[]
    当合约某symbol的持仓或全逐仓配置发生变动时

    "P"中会推送该symbol对应的"BOTH"方向上的持仓详情
    如果是多空方向上发生持仓变动, "P"中会该symbol发生持仓变动的对应"LONG"或"SHORT"方向上的持仓详情
    该symbol上被初始化过的"LONG"或"SHORT"方向持仓, 也会被推送
    其他symbol的所有持仓信息都不会被推送，即使其持仓不为0
    "FUNDING FEE" 引起的资金余额变化，仅推送简略事件：

    当用户某全仓持仓发生"FUNDING FEE"时，事件ACCOUNT_UPDATE将只会推送相关的用户资产余额信息B(仅推送FUNDING FEE 发生相关的资产余额信息)，而不会推送任何持仓信息P。
    当用户某逐仓仓持仓发生"FUNDING FEE"时，事件ACCOUNT_UPDATE将只会推送相关的用户资产余额信息B(仅推送"FUNDING FEE"所使用的资产余额信息)，和相关的持仓信息P(仅推送这笔"FUNDING FEE"发生所在的持仓信息)，其余持仓信息不会被推送。
    字段"m"代表了事件推出的原因，包含了以下可能类型:

    DEPOSIT
    WITHDRAW
    ORDER
    FUNDING_FEE
    WITHDRAW_REJECT
    ADJUSTMENT
    INSURANCE_CLEAR
    ADMIN_DEPOSIT
    ADMIN_WITHDRAW
    MARGIN_TRANSFER
    MARGIN_TYPE_CHANGE
    ASSET_TRANSFER
    OPTIONS_PREMIUM_FEE
    OPTIONS_SETTLE_PROFIT
     */

    @SerializedName("a")
    val account: AccountChangeInfo? = null;

    class AccountChangeInfo {

        /**
         * 字段"m"代表了事件推出的原因，包含了以下可能类型:

        DEPOSIT
        WITHDRAW
        ORDER
        FUNDING_FEE
        WITHDRAW_REJECT
        ADJUSTMENT
        INSURANCE_CLEAR
        ADMIN_DEPOSIT
        ADMIN_WITHDRAW
        MARGIN_TRANSFER
        MARGIN_TYPE_CHANGE
        ASSET_TRANSFER
        OPTIONS_PREMIUM_FEE
        OPTIONS_SETTLE_PROFIT
         */
        val m:String?=null;

        @SerializedName("B")
        val balance: List<BalanceChangeInfo>? = null;


        @SerializedName("P")
        val position: List<PositionChangeInfo>? = null;

        override fun toString(): String {
            return "AccountChangeInfo(balance=$balance, position=$position)"
        }


    }

    class PositionChangeInfo {

        /**
         *  {
        "s":"BTCUSDT",            // 交易对
        "pa":"0",                 // 仓位
        "ep":"0.00000",            // 入仓价格
        "cr":"200",               // (费前)累计实现损益
        "up":"0",                     // 持仓未实现盈亏
        "mt":"isolated",              // 保证金模式
        "iw":"0.00000000",            // 若为逐仓，仓位保证金
        "ps":"BOTH"                   // 持仓方向
        }
         */
        /**
         * 交易对
         */

        /**
         * 交易对
         */
        val s: String? = null;

        /**
         * 仓位
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val pa: NumberFormatObject? = null;

        /**
         * 入仓价格
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val ep: NumberFormatObject? = null;

        /**
         * (费前)累计实现损益
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val cr: NumberFormatObject? = null;

        /**
         *  持仓未实现盈亏
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val up: NumberFormatObject? = null;

        /**
         * 保证金模式
         */
        var mt: MarginType? = null

        /**
         * 若为逐仓，仓位保证金
         */
        @JsonAdapter(Number_UNFormatTypeAdapter::class)
        val iw: NumberFormatObject? = null;

        /**
         * 持仓方向
         */
        var ps: PositionDirection? = null

        override fun toString(): String {
            return "PositionChangeInfo(s=$s, pa=$pa, ep=$ep, cr=$cr, up=$up, mt=$mt, iw=$iw, ps=$ps)"
        }
    }

    class BalanceChangeInfo {
        /**
         * 资产名称 USDT
         */
        @SerializedName("a")
        val asset: String? = null;

        /**
         * 钱包余额
         */
        @SerializedName("wb")
        val availableBalance: NumberFormatObject? = null;

        /**
         * 除去逐仓仓位保证金的钱包余额
         */
        @SerializedName("cb")
        val crossWalletBalance: NumberFormatObject? = null


        override fun toString(): String {
            return "BalanceChangeInfo(asset=$asset, availableBalance=$availableBalance, crossWalletBalance=$crossWalletBalance)"
        }


    }
}