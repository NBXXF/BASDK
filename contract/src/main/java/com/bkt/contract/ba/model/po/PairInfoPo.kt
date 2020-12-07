package com.bkt.contract.ba.model.po

import android.text.TextUtils
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.xxf.arch.json.JsonUtils
import com.xxf.database.xxf.objectbox.id.IdUtils
import io.objectbox.annotation.*
import io.objectbox.converter.PropertyConverter

/**
 * @Description: 交易对 信息(配置+价格涨幅信息)
 * @Author: XGod
 * @CreateDate: 2020/12/3 19:37
 */
@Entity
class PairInfoPo {

    @Id(assignable = true)
    var _id: Long = 0
        get() = IdUtils.generateId(symbol);

    @Index(type = IndexType.VALUE)
    var index: Long = 0


    var symbol: String? = null;


    @Convert(converter = CoinConfigPoConverter::class, dbType = String::class)
    var config: PairConfigDto? = null;


    @Convert(converter = TickerPoConverter::class, dbType = String::class)
    var ticker: TickerEventDto? = null;

    constructor(symbol: String?) {
        this.symbol = symbol
    }


    class CoinConfigPoConverter : PropertyConverter<PairConfigDto?, String?> {
        override fun convertToEntityProperty(databaseValue: String?): PairConfigDto? {
            return if (TextUtils.isEmpty(databaseValue)) {
                null
            } else JsonUtils.toBean<PairConfigDto>(databaseValue, PairConfigDto::class.java)
        }

        override fun convertToDatabaseValue(entityProperty: PairConfigDto?): String? {
            return JsonUtils.toJsonString(entityProperty)
        }
    }

    class TickerPoConverter : PropertyConverter<TickerEventDto?, String?> {
        override fun convertToEntityProperty(databaseValue: String?): TickerEventDto? {
            return if (TextUtils.isEmpty(databaseValue)) {
                null
            } else JsonUtils.toBean<TickerEventDto>(databaseValue, TickerEventDto::class.java)
        }

        override fun convertToDatabaseValue(entityProperty: TickerEventDto?): String? {
            return JsonUtils.toJsonString(entityProperty)
        }
    }
}