package com.bkt.contract.ba.model.event

import com.bkt.contract.ba.enums.OrderEventType
import com.bkt.contract.ba.model.dto.OrderInfoDto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

/**
 * @Description: 订单状态更新
 * 当有新订单创建、订单有新成交或者新的状态变化时会推送此类事件 event type统一为 ORDER_TRADE_UPDATE
 *
 * https://binance-docs.github.io/apidocs/testnet/cn/#060a012f0b
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/17 15:13
 */
@JsonAdapter(OrderUpdateEvent.OrderUpdateJsonAdapter::class)
open class OrderUpdateEvent : BaseSEvent() {

    /**
     * 订单 动作 eg.新增 或者删除等等,具体参考枚举
     */
    var orderEventType: OrderEventType? = null;

    @SerializedName("o")
    var order: OrderInfoDto? = null;

    inner class OrderUpdateJsonAdapter : JsonDeserializer<OrderUpdateEvent> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): OrderUpdateEvent {
            if (json!!.isJsonObject) {
                val oderJson = json.asJsonObject.get("o");
                if (oderJson.isJsonObject) {
                    val orderEvent = oderJson.asJsonObject.get("x").asString;
                    json.asJsonObject.addProperty("orderEventType", orderEvent);
                }
            }
            return context!!.deserialize(json, WithoutJsonAdapterDto::class.java);
        }
    }

    inner class WithoutJsonAdapterDto : OrderUpdateEvent() {

    }

    override fun toString(): String {
        return "OrderUpdateEvent(orderEventType=$orderEventType, order=$order)"
    }


}