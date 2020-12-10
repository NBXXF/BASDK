package com.bkt.contract.ba.model.dto

import com.google.gson.annotations.SerializedName
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
    val closePrice: BigDecimal? = null;

    /**
     * 24小时内第一次成交的价格
     */
    @SerializedName("openPrice", alternate = ["o"])
    val openPrice: BigDecimal? = null;

    /**
     * 24小时最高价
     */
    @SerializedName("highPrice", alternate = ["h"])
    val highPrice: BigDecimal? = null;

    /**
     * 24小时最低价
     */
    @SerializedName("lowPrice", alternate = ["l"])
    val lowPrice: BigDecimal? = null;

    /**
     * 24小时成交量
     */
    @SerializedName("volume", alternate = ["v"])
    val volume: BigDecimal? = null;

    /**
     * 24小时成交额
     */
    @SerializedName("quoteVolume", alternate = ["q"])
    val quoteVolume: BigDecimal? = null;


    override fun toString(): String {
        return "TickerEventDto(symbol=$symbol, closePrice=$closePrice, openPrice=$openPrice, highPrice=$highPrice, lowPrice=$lowPrice, volume=$volume, quoteVolume=$quoteVolume)"
    }

}