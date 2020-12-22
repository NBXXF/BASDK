package com.bkt.contract.ba.model.po

import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.utils.NumberUtils
import com.xxf.database.xxf.objectbox.id.IdUtils
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.io.Serializable
import java.lang.reflect.Type
import java.math.BigDecimal

/**
 * @Description: 深度  socket和http 复用一个模型
 * @Author: XGod
 * @CreateDate: 2020/12/3 17:13
 */
@Entity
open class DepthEventDtoPo : Serializable {
    /**
     * 主键
     */
    @Id(assignable = true)
    var _id: Long = 0
        get() = IdUtils.generateId(symbol);

    @SerializedName("symbol", alternate = ["s"])
    var symbol: String;

    /**
     *  买单
     */
    @SerializedName("bids", alternate = ["b"])
    @JsonAdapter(BookItemJsonTypeAdapter::class)
    @Convert(converter = BookItemCacheConverter::class, dbType = String::class)
    val bids: List<BookItem>;

    /**
     *  卖单
     *  [
    "4.00000000",     // 价格
    "431.00000000"    // 数量
    ]
    转换json  {@link BookItem} 方便业务层调用
     */
    @SerializedName("asks", alternate = ["a"])
    @JsonAdapter(BookItemJsonTypeAdapter::class)
    @Convert(converter = BookItemCacheConverter::class, dbType = String::class)
    val asks: List<BookItem>;

    constructor(symbol: String, bids: List<BookItem>, asks: List<BookItem>) {
        this.symbol = symbol
        this.bids = bids
        this.asks = asks
    }

    internal class BookItemCacheConverter : PropertyConverter<List<BookItem>, String> {
        override fun convertToDatabaseValue(entityProperty: List<BookItem>?): String {
            return JsonUtils.toJsonString(entityProperty);
        }

        override fun convertToEntityProperty(databaseValue: String?): List<BookItem> {
            return JsonUtils.toBeanList(databaseValue, BookItem::class.java)
        }
    }

    internal class BookItemJsonTypeAdapter : JsonSerializer<List<BookItem>>, JsonDeserializer<List<BookItem>> {
        override fun serialize(src: List<BookItem>?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            if (src != null) {
                /**
                 * [
                "4.00000000",     // 价格
                "431.00000000"    // 数量
                ]
                 */
                var jsonArray: JsonArray = JsonArray();
                for (item in src) {
                    var itemJson: JsonArray = JsonArray();
                    itemJson.add(item.price.origin)
                    itemJson.add(item.amount.origin)
                    jsonArray.add(itemJson);
                }
                return context!!.serialize(jsonArray);
            }
            return context!!.serialize(JsonArray());
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): List<BookItem> {
            val list = mutableListOf<BookItem>()
            try {
                /**
                 * [
                "4.00000000",     // 价格
                "431.00000000"    // 数量
                ]
                 */
                if (json!!.isJsonArray) {
                    json.asJsonArray.forEach {
                        val priceBigDecimal = BigDecimal(it.asJsonArray.get(0).asString);
                        val numberBigDecimal = BigDecimal(it.asJsonArray.get(1).asString);
                        /**
                         * 过滤价格和数量小于等于0的情况
                         */
                        if (NumberUtils.compare(priceBigDecimal, 0) > 0
                                && NumberUtils.compare(numberBigDecimal, 0) > 0) {
                            list.add(
                                    BookItem(
                                            NumberFormatObject(priceBigDecimal, priceBigDecimal.toPlainString()),
                                            NumberFormatObject(numberBigDecimal, numberBigDecimal.toPlainString())
                                    ));
                        }
                    }
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
    open class BookItem(val price: NumberFormatObject, val amount: NumberFormatObject) {
        override fun toString(): String {
            return "BookItem(price=$price, amount=$amount)"
        }
    }

    override fun toString(): String {
        return "DepthEventDtoPo(symbol='$symbol', bids=$bids, asks=$asks)"
    }


}