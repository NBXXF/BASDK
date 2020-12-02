package com.bkt.contract.ba.model

import com.bkt.contract.ba.enums.OderFilterType
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import java.io.Serializable
import java.lang.reflect.Type
import java.math.BigDecimal

/**
 * @Description: 交易对限制
 * @Author: XGod
 * @CreateDate: 2020/12/2 11:14
 */
@JsonAdapter(OderFilterDto.SerializerDeserializer::class)
open class OderFilterDto : Serializable {
    inner class SerializerDeserializer : JsonSerializer<OderFilterDto>, JsonDeserializer<OderFilterDto> {
        override fun serialize(src: OderFilterDto?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return context!!.serialize(src, typeOfSrc);
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): OderFilterDto {
            val deserialize = context!!.deserialize<TypeFilter>(json, TypeFilter::class.java);
            val typeFilter: OderFilterDto? = when (deserialize.filterType) {
                OderFilterType.PRICE_FILTER -> context!!.deserialize<OderPriceFilterDto>(json, OderPriceFilterDto::class.java);
                OderFilterType.LOT_SIZE -> context!!.deserialize<OderPriceFilterDto>(json, OderLotSizeFilterDto::class.java);
                OderFilterType.MARKET_LOT_SIZE -> context!!.deserialize<OderPriceFilterDto>(json, OderMarketLotSizeFilterDto::class.java);
                OderFilterType.MAX_NUM_ORDERS -> context!!.deserialize<OderPriceFilterDto>(json, OderMaxNumFilterDto::class.java);
                OderFilterType.MAX_NUM_ALGO_ORDERS -> context!!.deserialize<OderPriceFilterDto>(json, OderMaxNumAlgoFilterDto::class.java);
                OderFilterType.PERCENT_PRICE -> context!!.deserialize<OderPriceFilterDto>(json, OderPercentPriceFilterDto::class.java);
                else -> throw JsonParseException("no support type" + deserialize.filterType);
            }
            return typeFilter!!;
        }
    }

    final inner class TypeFilter : OderFilterDto() {
    }
    /**
     * 所有类型对应的所有属性
     * {
     * "filterType": "PRICE_FILTER", // 价格限制
     * "maxPrice": "300", // 价格上限, 最大价格
     * "minPrice": "0.0001", // 价格下限, 最小价格
     * "tickSize": "0.0001" // 步进间隔
     * },
     * {
     * "filterType": "LOT_SIZE", // 数量限制
     * "maxQty": "10000000", // 数量上限, 最大数量
     * "minQty": "1", // 数量下限, 最小数量
     * "stepSize": "1" // 允许的步进值
     * },
     * {
     * "filterType": "MARKET_LOT_SIZE", // 市价订单数量限制
     * "maxQty": "590119", // 数量上限, 最大数量
     * "minQty": "1", // 数量下限, 最小数量
     * "stepSize": "1" // 允许的步进值
     * },
     * {
     * "filterType": "MAX_NUM_ORDERS", // 最多订单数限制
     * "limit": 200
     * },
     * {
     * "filterType": "MAX_NUM_ALGO_ORDERS", // 最多条件订单数限制
     * "limit": 100
     * },
     * {
     * "filterType": "PERCENT_PRICE", // 价格比限制
     * "multiplierUp": "1.1500", // 价格上限百分比
     * "multiplierDown": "0.8500", // 价格下限百分比
     * "multiplierDecimal": 4
     * }
     */
    /**
     * 下单限制配置
     */
    val filterType: OderFilterType? = null

    /**
     * "filterType": "PRICE_FILTER", // 价格限制
     */
    open class OderPriceFilterDto : OderFilterDto(), Serializable {
        /**
         * 价格上限, 最大价格
         */
        val maxPrice: BigDecimal? = null

        /**
         * 价格下限, 最小价格
         */
        val minPrice: BigDecimal? = null

        /**
         * 步进间隔
         */
        val tickSize: BigDecimal? = null

        override fun toString(): String {
            return "OderPriceFilterDto(maxPrice=$maxPrice, minPrice=$minPrice, tickSize=$tickSize)"
        }
    }

    /**
     * "filterType": "LOT_SIZE", // 数量限制
     */
    open class OderLotSizeFilterDto : OderFilterDto(), Serializable {
        /**
         * 数量上限, 最大数量
         */
        val maxQty: BigDecimal? = null

        /**
         * 数量下限, 最小数量
         */
        val minQty: BigDecimal? = null

        /**
         * 允许的步进值
         */
        val stepSize: BigDecimal? = null

        override fun toString(): String {
            return "OderLotSizeFilterDto(maxQty=$maxQty, minQty=$minQty, stepSize=$stepSize)"
        }
    }

    /**
     * "filterType": "MARKET_LOT_SIZE", // 市价订单数量限制
     */
    open class OderMarketLotSizeFilterDto : OderLotSizeFilterDto(), Serializable

    /**
     * "filterType": "MAX_NUM_ORDERS", // 最多订单数限制
     */
    open class OderMaxNumFilterDto : OderFilterDto() {
        /**
         * 定义了某个交易对最多允许的挂单数量(不包括已关闭的订单)
         */
        val limit = 0
        override fun toString(): String {
            return "OderMaxNumFilterDto(limit=$limit)"
        }
    }

    /**
     * "filterType": "MAX_NUM_ALGO_ORDERS", // 最多条件订单数限制
     */
    open class OderMaxNumAlgoFilterDto : OderMaxNumFilterDto(), Serializable

    /**
     *  "filterType": "PERCENT_PRICE", // 价格比限制
     */
    open class OderPercentPriceFilterDto : OderFilterDto(), Serializable {
        /**
         * 价格上限百分比
         */
        val multiplierUp: BigDecimal? = null

        /**
         * 价格下限百分比
         */
        val multiplierDown: BigDecimal? = null

        /**
         * 小数位数
         */
        val multiplierDecimal = 0

        override fun toString(): String {
            return "OderPercentPriceFilterDto(multiplierUp=$multiplierUp, multiplierDown=$multiplierDown, multiplierDecimal=$multiplierDecimal)"
        }
    }
}