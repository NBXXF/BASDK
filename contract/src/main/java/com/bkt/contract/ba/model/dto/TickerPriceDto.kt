package com.bkt.contract.ba.model.dto

import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_UNFormatTypeAdapter

/**
 * @Description: 最新价模型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/18 9:55
 */
class TickerPriceDto {
    /**
     * USDT 返回结果:
     * {
    "symbol": "LTCBTC",       // 交易对
    "price": "4.00000200",    // 价格
    "time": 1589437530011   // 撮合引擎时间
    }

    USD 返回结果:
    {
    "symbol": "BTCUSD_200626",  // 交易对
    "ps": "BTCUSD",             // 标的交易对
    "price": "9647.8",          // 价格
    "time": 1591257246176       // 时间
    }
    ]
     */
    /**
     * 交易对
     */
    val symbol: String? = null;

    /**
     * 价格
     */
    @JsonAdapter(Number_UNFormatTypeAdapter::class)
    val price: NumberFormatObject? = null;

    /**
     * 撮合引擎时间
     */
    val time: Long = 0;


    override fun toString(): String {
        return "TickerPriceDto(symbol=$symbol, price=$price, time=$time)"
    }


}