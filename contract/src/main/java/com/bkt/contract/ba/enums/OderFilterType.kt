package com.bkt.contract.ba.enums

import com.google.gson.annotations.SerializedName

/**
 * @Description: 下单限制类型
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/2 11:28
 */
enum class OderFilterType(val value: String) {
    /**
     * PRICE_FILTER 价格过滤器
    /exchangeInfo 响应中的格式:

    {
    "filterType": "PRICE_FILTER",
    "minPrice": "0.00000100",
    "maxPrice": "100000.00000000",
    "tickSize": "0.00000100"
    }
    价格过滤器用于检测order订单中price参数的合法性

    minPrice 定义了 price/stopPrice 允许的最小值
    maxPrice 定义了 price/stopPrice 允许的最大值。
    tickSize 定义了 price/stopPrice 的步进间隔，即price必须等于minPrice+(tickSize的整数倍) 以上每一项均可为0，为0时代表这一项不再做限制。
    逻辑伪代码如下：

    price >= minPrice
    price <= maxPrice
    (price-minPrice) % tickSize == 0
     */
    @SerializedName("PRICE_FILTER")
    PRICE_FILTER("PRICE_FILTER"),


    /**
     * LOT_SIZE 订单尺寸
    /exchangeInfo 响应中的格式:*

    {
    "filterType": "LOT_SIZE",
    "minQty": "0.00100000",
    "maxQty": "100000.00000000",
    "stepSize": "0.00100000"
    }
    lots是拍卖术语，这个过滤器对订单中的quantity也就是数量参数进行合法性检查。包含三个部分：

    minQty 表示 quantity 允许的最小值.
    maxQty 表示 quantity 允许的最大值
    stepSize 表示 quantity允许的步进值。
    逻辑伪代码如下：

    quantity >= minQty
    quantity <= maxQty
    (quantity-minQty) % stepSize == 0
     */
    @SerializedName("LOT_SIZE")
    LOT_SIZE("LOT_SIZE"),


    /**
     * MARKET_LOT_SIZE 市价订单尺寸
    参考LOT_SIZE，区别仅在于对市价单还是限价单生效
     */
    @SerializedName("MARKET_LOT_SIZE")
    MARKET_LOT_SIZE("MARKET_LOT_SIZE"),


    /**
     * MAX_NUM_ORDERS 最多订单数
    /exchangeInfo 响应中的格式:

    {
    "filterType": "MAX_NUM_ORDERS",
    "limit": 200
    }
    定义了某个交易对最多允许的挂单数量(不包括已关闭的订单)

    普通订单与条件订单均计算在内
     */
    @SerializedName("MAX_NUM_ORDERS")
    MAX_NUM_ORDERS("MAX_NUM_ORDERS"),


    /**
     * MAX_NUM_ALGO_ORDERS 最多条件订单数
    /exchangeInfo format:

    {
    "filterType": "MAX_NUM_ALGO_ORDERS",
    "limit": 100
    }
    定义了某个交易对最多允许的条件订单的挂单数量(不包括已关闭的订单)。

    条件订单目前包括STOP, STOP_MARKET, TAKE_PROFIT, TAKE_PROFIT_MARKET, 和 TRAILING_STOP_MARKET
     */
    @SerializedName("MAX_NUM_ALGO_ORDERS")
    MAX_NUM_ALGO_ORDERS("MAX_NUM_ALGO_ORDERS"),


    /**
     * PERCENT_PRICE 价格振幅过滤器
    /exchangeInfo 响应中的格式:

    {
    "filterType": "PERCENT_PRICE",
    "multiplierUp": "1.1500",
    "multiplierDown": "0.8500",
    "multiplierDecimal": 4
    }
    PERCENT_PRICE 定义了基于标记价格计算的挂单价格的可接受区间.

    挂单价格必须同时满足以下条件：

    买单: price <= markPrice * multiplierUp
    卖单: price >= markPrice * multiplierDown
     */
    @SerializedName("PERCENT_PRICE")
    PERCENT_PRICE("PERCENT_PRICE")
}