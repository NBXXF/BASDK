package com.bkt.contract.ba.model.dto

import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter

/**
 * @Description: 币种余额  注意 模型取USDT和USD交集的字段
 * @Author: XGod
 * @CreateDate: 2020/12/16 14:19
 */
class CoinBalanceDto {
    /**
     *
     * v2/balance
     *
     * USDT 返回结构如下：
     * {
     * "accountAlias": "SgsR",    // 账户唯一识别码
     * "asset": "USDT",        // 资产
     * "balance": "122607.35137903",   // 总余额
     * "crossWalletBalance": "23.72469206", // 全仓余额
     * "crossUnPnl": "0.00000000"  // 全仓持仓未实现盈亏
     * "availableBalance": "23.72469206",       // 可用余额
     * "maxWithdrawAmount": "23.72469206"     // 最大可转出余额
     * }
     *
     * USD 返回结构如下:
     * {
     * "accountAlias": "SgsR",    // 账户唯一识别码
     * "asset": "BTC",     // 资产
     * "balance": "0.00250000",    // 账户余额
     * "withdrawAvailable": "0.00250000", // 最大可提款金额,同`GET /dapi/account`中"maxWithdrawAmount"
     * "crossWalletBalance": "0.00241969", // 全仓账户余额
     * "crossUnPnl": "0.00000000", // 全仓持仓未实现盈亏
     * "availableBalance": "0.00241969",    // 可用下单余额
     * "updateTime": 1592468353979
     * }
     */
    /**
     * 注意 模型取USDT和USD交集的字段
     */
    /**
     * 账户唯一识别码
     */
    var accountAlias: String? = null

    /**
     * 资产 eg."BTC"
     * 特别注意！！：
     * USDT合约 返回的是USDT
     * 对于 USD合约返回的是 BTC
     */
    var asset: String? = null

    /**
     * 账户余额
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var balance: NumberFormatObject? = null

    /**
     * 全仓账户余额
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var crossWalletBalance: NumberFormatObject? = null

    /**
     * 全仓持仓未实现盈亏
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var crossUnPnl: NumberFormatObject? = null

    /**
     * 可用下单余额
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    var availableBalance: NumberFormatObject? = null


    override fun toString(): String {
        return "CoinBalanceDto(accountAlias=$accountAlias, asset=$asset, balance=$balance, crossWalletBalance=$crossWalletBalance, crossUnPnl=$crossUnPnl, availableBalance=$availableBalance)"
    }


}