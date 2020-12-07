package com.bkt.contract.ba.model.dto

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.math.BigDecimal

/**
 * @Description: 深度  socket和http 复用一个模型
 * @Author: XGod
 * @CreateDate: 2020/12/3 17:13
 */
class DepthEventDto {

    /**
     *  买单
     */
    @SerializedName("bids", alternate = ["b"])
    val bids: List<BookItem>? = null;

    /**
     *  卖单
     *  [
    "4.00000000",     // 价格
    "431.00000000"    // 数量
    ]
    转换json  {@link BookItem} 方便业务层调用
     */
    @SerializedName("asks", alternate = ["a"])
    val asks: List<BookItem>? = null;

    class BookItemTypeAdapter : JsonSerializer<List<BookItem>>, JsonDeserializer<List<BookItem>> {
        override fun serialize(src: List<BookItem>?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            if (src != null) {
                var jsonArray: JsonArray = JsonArray();
                for (item in src) {
                    var itemJson: JsonArray = JsonArray();
                    itemJson.add(item.price)
                    itemJson.add(item.amount)
                    jsonArray.add(itemJson);
                }
                return context!!.serialize(jsonArray);
            }
            return context!!.serialize(JsonArray());
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): List<BookItem> {
            val list = mutableListOf<BookItem>()
            try {
                if (json!!.isJsonArray) {
                    val itemJson: JsonArray = json.asJsonArray;
                    list.add(BookItem(BigDecimal(itemJson.get(0).asString), BigDecimal(itemJson.get(1).asString)));
                }
            } catch (e: Throwable) {
                throw JsonParseException(e);
            }
            return list;
        }

    }

    /**
     * [
    "4.00000000",     // 价格
    "431.00000000"    // 数量
    ]
     */
    class BookItem(val price: BigDecimal, val amount: BigDecimal) {
    }
}