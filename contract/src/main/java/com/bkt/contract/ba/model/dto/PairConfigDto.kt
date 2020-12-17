package com.bkt.contract.ba.model.dto

import android.text.TextUtils
import com.bkt.contract.ba.enums.*
import java.io.Serializable
import java.lang.Exception

/**
 * @Description: 交易对配置
 * @Author: XGod
 * @CreateDate: 2020/12/2 11:03
 */
class PairConfigDto : Serializable {
    /**
     * eg.BLZUSDT 交易对
     */
    val symbol: String? = null

    /**
     *  标的交易对
     */
    val pair: String? = null
        get() {
            if (TextUtils.isEmpty(field)) {
                return baseAsset + quoteAsset
            }
            return field;
        };

    /**
     * eg.TRADING 交易对状态
     */
    val status: String? = null

    /**
     * eg.2.5000 请忽略
     */
    val maintMarginPercent: String? = null

    /**
     * eg.5.0000 请忽略
     */
    val requiredMarginPercent: String? = null

    /**
     * eg.BLZ  标的资产
     */
    val baseAsset: String? = null

    /**
     * : "USDT", // 报价资产
     */
    val quoteAsset: String? = null

    /**
     * : "USDT", // 保证金资产
     */
    val marginAsset: String? = null

    /**
     * : 5,  // 价格小数点位数
     */
    val pricePrecision: Int = 0

    /**
     * : 0,  // 数量小数点位数
     */
    val quantityPrecision: Int = 0

    /**
     * : 8,  // 标的资产精度
     */
    val baseAssetPrecision: Int = 0

    /**
     * : 8,  // 报价资产精度
     */
    val quotePrecision: Int = 0

    /**
     * "COIN",
     */
    val underlyingType: String? = null

    /**
     * 0,
     */
    val settlePlan: Int = 0

    /**
     * : "0.15", // 开启"priceProtect"的条件订单的触发阈值
     */
    val triggerProtect: String? = null

    /**
     * ["STORAGE"],
     */
    val underlyingSubType: List<String>? = null

    /**
     * 下单限制
     */
    val filters: List<OderFilterDto>? = null

    /**
     * 订单类型
     */
    val orderType: List<OrderType>? = null

    /**
     * 有效方式
     */
    val timeInForce: List<TimeInForce>? = null


    /**
     * ba类型
     */
    val contractType: BaContractType? = null;

    /**
     * 按USDT 和USD 进行划分
     */
    var contractClassifyType: ContractType? = null;

    /**
     * 合约乘数 只有 币本位合约USD有
     */
    val contractSize: Int = 0;


    /**
     * 获取下单配置
     */
    fun getFilter(filterType: OderFilterType): OderFilterDto? {
        try {
            return filters?.first { it.filterType == filterType }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    override fun toString(): String {
        return "PairConfigDto(symbol=$symbol, status=$status, maintMarginPercent=$maintMarginPercent, requiredMarginPercent=$requiredMarginPercent, baseAsset=$baseAsset, quoteAsset=$quoteAsset, marginAsset=$marginAsset, pricePrecision=$pricePrecision, quantityPrecision=$quantityPrecision, baseAssetPrecision=$baseAssetPrecision, quotePrecision=$quotePrecision, underlyingType=$underlyingType, settlePlan=$settlePlan, triggerProtect=$triggerProtect, underlyingSubType=$underlyingSubType, filters=$filters, orderType=$orderType, timeInForce=$timeInForce, contractType=$contractType, contractSize=$contractSize)"
    }


}