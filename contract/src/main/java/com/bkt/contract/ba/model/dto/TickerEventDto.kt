package com.bkt.contract.ba.model.dto

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.typeadapter.format.NumberObjectFormatTypeAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter
import com.xxf.arch.utils.NumberUtils
import java.io.Serializable
import java.math.BigDecimal

/**
 * @Description: ticker 最新价 成交等信息；event 和dto 复用一个模型
 * @Author: XGod
 * @CreateDate: 2020/12/2 16:52
 */
class TickerEventDto : Serializable {
    /**
     * {
    "symbol": "BTCUSDT",
    "priceChange": "-94.99999800",    //24小时价格变动
    "priceChangePercent": "-95.960",  //24小时价格变动百分比
    "weightedAvgPrice": "0.29628482", //加权平均价
    "lastPrice": "4.00000200",        //最近一次成交价
    "lastQty": "200.00000000",        //最近一次成交额
    "openPrice": "99.00000000",       //24小时内第一次成交的价格
    "highPrice": "100.00000000",      //24小时最高价
    "lowPrice": "0.10000000",         //24小时最低价
    "volume": "8913.30000000",        //24小时成交量
    "quoteVolume": "15.30000000",     //24小时成交额
    "openTime": 1499783499040,        //24小时内，第一笔交易的发生时间
    "closeTime": 1499869899040,       //24小时内，最后一笔交易的发生时间
    "firstId": 28385,   // 首笔成交id
    "lastId": 28460,    // 末笔成交id
    "count": 76         // 成交笔数
    }
     */

    /**
     *  {
    "e": "24hrMiniTicker",  // 事件类型
    "E": 123456789,         // 事件时间(毫秒)
    "s": "BNBUSDT",          // 交易对
    "c": "0.0025",          // 最新成交价格
    "o": "0.0010",          // 24小时前开始第一笔成交价格
    "h": "0.0025",          // 24小时内最高成交价
    "l": "0.0010",          // 24小时内最低成交加
    "v": "10000",           // 成交量
    "q": "18"               // 成交额
    }
     */


    @SerializedName("symbol", alternate = ["s"])
    val symbol: String? = null;

    /**
     * 最近一次成交价
     */
    @SerializedName("lastPrice", alternate = ["c"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val closePrice: NumberFormatObject? = null;

    /**
     * 24小时内第一次成交的价格
     */
    @SerializedName("openPrice", alternate = ["o"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val openPrice: NumberFormatObject? = null;

    /**
     * 24小时最高价
     */
    @SerializedName("highPrice", alternate = ["h"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val highPrice: NumberFormatObject? = null;

    /**
     * 24小时最低价
     */
    @SerializedName("lowPrice", alternate = ["l"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val lowPrice: NumberFormatObject? = null;

    /**
     * 24小时成交量
     */
    @SerializedName("volume", alternate = ["v"])
    @JsonAdapter(AmountKMFormatTypeAdapter::class)
    val volume: NumberFormatObject? = null;

    /**
     * 24小时成交额
     */
    @SerializedName("quoteVolume", alternate = ["q"])
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val quoteVolume: NumberFormatObject? = null;


    /**
     * 涨幅 本地字段 http和socket没有
     */
    var riseFallRange: NumberFormatObject? = null;


    /**
     *  转换大数 至k,M单位
     */
    class AmountKMFormatTypeAdapter : NumberObjectFormatTypeAdapter() {
        @Throws(Exception::class)
        override fun format(origin: BigDecimal): String {
            return if (origin.toDouble() > 1000000) {
                return NumberUtils.formatRoundDown(NumberUtils.divide(origin, 1000000), 2, 2) + "M"
            } else if (origin.toDouble() > 1000) {
                return NumberUtils.formatRoundDown(NumberUtils.divide(origin, 1000), 2, 2) + "K"
            } else {
                return origin.toPlainString();
            }
        }
    }

    override fun toString(): String {
        return "TickerEventDto(symbol=$symbol, closePrice=$closePrice, openPrice=$openPrice, highPrice=$highPrice, lowPrice=$lowPrice, volume=$volume, quoteVolume=$quoteVolume, riseFallRange=$riseFallRange)"
    }


}