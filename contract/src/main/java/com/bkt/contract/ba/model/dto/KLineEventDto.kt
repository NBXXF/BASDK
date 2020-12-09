package com.bkt.contract.ba.model.dto

import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.math.BigDecimal

/**
 * @Description: k线单条数据模型  兼容socket和http模型
 * @Author: XGod
 * @CreateDate: 2020/12/3 18:52
 */
@JsonAdapter(KLineEventDto.KLineJsonTypeAdapter::class)
class KLineEventDto {

    class KLineJsonTypeAdapter : JsonSerializer<KLineEventDto>, JsonDeserializer<KLineEventDto> {
        override fun serialize(src: KLineEventDto?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return context!!.serialize(src);
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): KLineEventDto {
            /**
             * [
            1591258320000,      // 开盘时间
            "9640.7",           // 开盘价
            "9642.4",           // 最高价
            "9640.6",           // 最低价
            "9642.0",           // 收盘价(当前K线未结束的即为最新价)
            "206",              // 成交量
            1591258379999,      // 收盘时间
            "2.13660389",       // 成交额(标的数量)
            48,                 // 成交笔数
            "119",              // 主动买入成交量
            "1.23424865",      // 主动买入成交额(标的数量)
            "0"                 // 请忽略该参数
            ]
             */
            if (json!!.isJsonArray) {
                val jsonArray = json!!.asJsonArray;

                val time = jsonArray.get(0).asLong;
                val openPrice = BigDecimal(jsonArray.get(1).asString);
                val highPrice = BigDecimal(jsonArray.get(2).asString);
                val lowPrice = BigDecimal(jsonArray.get(3).asString);
                val closePrice = BigDecimal(jsonArray.get(4).asString);
                val volume = BigDecimal(jsonArray.get(5).asString);
                return KLineEventDto("", closePrice, openPrice, highPrice, lowPrice, volume, time);
            }
            return context!!.deserialize(json, typeOfT);
        }
    }

    /**
     * 交易对名称
     */
    @SerializedName("s")
    var symbol: String;

    /**
     * 最近一次成交价
     */
    @SerializedName("c")
    val closePrice: BigDecimal;

    /**
     * 24小时内第一次成交的价格
     */
    @SerializedName("o")
    val openPrice: BigDecimal;

    /**
     * 24小时最高价
     */
    @SerializedName("h")
    val highPrice: BigDecimal;

    /**
     * 24小时最低价
     */
    @SerializedName("l")
    val lowPrice: BigDecimal;

    /**
     * 24小时成交量
     */
    @SerializedName("v")
    val volume: BigDecimal;

    /**
     * 时间
     */
    @SerializedName("t")
    val time: Long;

    constructor(symbol: String,
                closePrice: BigDecimal,
                openPrice: BigDecimal,
                highPrice: BigDecimal,
                lowPrice: BigDecimal,
                volume: BigDecimal,
                time: Long) {
        this.symbol = symbol
        this.closePrice = closePrice
        this.openPrice = openPrice
        this.highPrice = highPrice
        this.lowPrice = lowPrice
        this.volume = volume
        this.time = time
    }

    override fun toString(): String {
        return "KLineEventDto(symbol=$symbol, closePrice=$closePrice, openPrice=$openPrice, highPrice=$highPrice, lowPrice=$lowPrice, volume=$volume, time=$time)"
    }

}