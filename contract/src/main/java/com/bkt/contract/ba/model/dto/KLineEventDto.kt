package com.bkt.contract.ba.model.dto

import com.bkt.contract.ba.service.CommonService
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.formatobject.TimeFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_yyyy_s_MM_s_dd_HH_c_mm_FormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.impl.time.Time_yyyy_s_MM_s_dd_HH_c_mm_c_ss_FormatTypeAdapter
import java.lang.reflect.Type
import java.math.BigDecimal

/**
 * @Description: k线单条数据模型  兼容socket和http模型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/3 18:52
 */
@JsonAdapter(KLineEventDto.KLineJsonTypeAdapter::class)
class KLineEventDto {

    /**
     * <symbol>@kline_<interval>
     * {
    "t":1591261500000,      // 这根K线的起始时间
    "T":1591261559999,      // 这根K线的结束时间
    "s":"BTCUSD_200626",    // 交易对
    "i":"1m",               // K线间隔
    "f":606400,             // 这根K线期间第一笔成交ID
    "L":606430,             // 这根K线期间末一笔成交ID
    "o":"9638.9",           // 这根K线期间第一笔成交价
    "c":"9639.8",           // 这根K线期间末一笔成交价
    "h":"9639.8",           // 这根K线期间最高成交价
    "l":"9638.6",           // 这根K线期间最低成交价
    "v":"156",              // 这根K线期间成交量
    "n":31,                 // 这根K线期间成交笔数
    "x":false,              // 这根K线是否完结(是否已经开始下一根K线)
    "q":"1.61836886",       // 这根K线期间成交额(标的数量)
    "V":"73",               // 主动买入的成交量
    "Q":"0.75731156",       // 主动买入的成交额(标的数量)
    "B":"0"                 // 忽略此参数
    }

    /dapi/v1/klines

    [
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
    class KLineJsonTypeAdapter : JsonSerializer<KLineEventDto>, JsonDeserializer<KLineEventDto> {
        override fun serialize(src: KLineEventDto?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return context!!.serialize(src);
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): KLineEventDto {
            if (json!!.isJsonArray) {
                val jsonArray = json!!.asJsonArray;

                val time = jsonArray.get(0).asLong;
                val openPrice = BigDecimal(jsonArray.get(1).asString);
                val highPrice = BigDecimal(jsonArray.get(2).asString);
                val lowPrice = BigDecimal(jsonArray.get(3).asString);
                val closePrice = BigDecimal(jsonArray.get(4).asString);
                val volume = BigDecimal(jsonArray.get(5).asString);

                /**
                 * http 过来没有symbol字段
                 */
                return KLineEventDto("",
                        NumberFormatObject(closePrice, closePrice.toPlainString()),
                        NumberFormatObject(openPrice, openPrice.toPlainString()),
                        NumberFormatObject(highPrice, highPrice.toPlainString()),
                        NumberFormatObject(lowPrice, lowPrice.toPlainString()),
                        NumberFormatObject(volume, volume.toPlainString()),
                        TimeFormatObject(time, Time_yyyy_s_MM_s_dd_HH_c_mm_FormatTypeAdapter().format(time)),
                        CommonService.INSTANCE.getRiseFallAmountFormatObject(closePrice, openPrice),
                        CommonService.INSTANCE.getRiseFallRangeFormatObject(closePrice, openPrice)
                );
            }
            val k: KLineEventDto = context!!.deserialize(json, typeOfT);
            if (k != null) {
                /**
                 * 手动计算
                 */
                k.riseFallAmount = CommonService.INSTANCE.getRiseFallAmountFormatObject(k.closePrice.origin, k.openPrice.origin);
                k.riseFallRange = CommonService.INSTANCE.getRiseFallRangeFormatObject(k.closePrice.origin, k.openPrice.origin);
            }
            return k;
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
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val closePrice: NumberFormatObject;

    /**
     * 24小时内第一次成交的价格
     */
    @SerializedName("o")
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val openPrice: NumberFormatObject;

    /**
     * 24小时最高价
     */
    @SerializedName("h")
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val highPrice: NumberFormatObject;

    /**
     * 24小时最低价
     */
    @SerializedName("l")
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val lowPrice: NumberFormatObject;

    /**
     * 24小时成交量
     */
    @SerializedName("v")
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val volume: NumberFormatObject;

    /**
     * 时间
     */
    @SerializedName("t")
    @JsonAdapter(Time_yyyy_s_MM_s_dd_HH_c_mm_FormatTypeAdapter::class)
    val time: TimeFormatObject;


    /**
     * 涨跌量或者涨跌额 本地字段 http和socket没有
     */
    var riseFallAmount: NumberFormatObject
        private set

    /**
     * 涨幅  本地字段 http和socket没有
     */
    var riseFallRange: NumberFormatObject
        private set

    constructor(symbol: String, closePrice: NumberFormatObject, openPrice: NumberFormatObject, highPrice: NumberFormatObject, lowPrice: NumberFormatObject, volume: NumberFormatObject, time: TimeFormatObject, riseFallAmount: NumberFormatObject, riseFallRange: NumberFormatObject) {
        this.symbol = symbol
        this.closePrice = closePrice
        this.openPrice = openPrice
        this.highPrice = highPrice
        this.lowPrice = lowPrice
        this.volume = volume
        this.time = time
        this.riseFallAmount = riseFallAmount
        this.riseFallRange = riseFallRange
    }

    override fun toString(): String {
        return "KLineEventDto(symbol='$symbol', closePrice=$closePrice, openPrice=$openPrice, highPrice=$highPrice, lowPrice=$lowPrice, volume=$volume, time=$time, riseFallAmount=$riseFallAmount, riseFallRange=$riseFallRange)"
    }


}