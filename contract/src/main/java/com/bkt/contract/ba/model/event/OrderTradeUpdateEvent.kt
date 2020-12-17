package com.bkt.contract.ba.model.event

import com.bkt.contract.ba.model.dto.OrderInfoDto
import com.google.gson.annotations.SerializedName

/**
 * @Description: 订单状态更新
 * 当有新订单创建、订单有新成交或者新的状态变化时会推送此类事件 event type统一为 ORDER_TRADE_UPDATE
 *
 * https://binance-docs.github.io/apidocs/testnet/cn/#060a012f0b
 * @Author: XGod
 * @CreateDate: 2020/12/17 15:13
 */
class OrderTradeUpdateEvent : BaseSEvent() {

    @SerializedName("o")
    val o: OrderInfoDto? = null;
}