package com.bkt.contract.ba.model.po

import com.bkt.contract.ba.model.dto.PairConfigDto
import com.bkt.contract.ba.model.dto.TradeEventDto
import com.xxf.arch.json.JsonUtils
import com.xxf.database.xxf.objectbox.id.IdUtils
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

/**
 * @Description: 交易对对应的 成交 po
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/11 14:14
 */
@Entity
class PairTradePo {
    /**
     * 主键
     */
    @Id(assignable = true)
    var _id: Long = 0
        get() = IdUtils.generateId(symbol);

    /**
     * 交易对名称
     */
    var symbol: String;


    /**
     * 交易对对应的历史成交
     */
    @Convert(converter = TradeEventListConverter::class, dbType = String::class)
    var trades: List<TradeEventDto>

    constructor(symbol: String, trades: List<TradeEventDto>) {
        this.symbol = symbol
        this.trades = trades
    }

    override fun toString(): String {
        return "TradePo(symbol='$symbol', trades=$trades)"
    }

    internal class TradeEventListConverter : PropertyConverter<List<TradeEventDto>, String> {
        override fun convertToDatabaseValue(entityProperty: List<TradeEventDto>?): String {
            return JsonUtils.toJsonString(entityProperty);
        }

        override fun convertToEntityProperty(databaseValue: String?): List<TradeEventDto> {
            return JsonUtils.toBeanList(databaseValue, TradeEventDto::class.java);
        }
    }
}