package com.bkt.contract.ba.model.po

import android.text.TextUtils
import com.bkt.contract.ba.enums.ContractType
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

    /**
     * 主键
     */
    @Id(assignable = true)
    var _id: Long = 0
        get() = IdUtils.generateId(symbol);

    /**
     * 合约顺序索引
     */
    @Index(type = IndexType.VALUE)
    var index: Int = 0


    /**
     * 交易对名称
     */
    var symbol: String? = null;


    /**
     * 配置
     */
    @Convert(converter = CoinConfigPoConverter::class, dbType = String::class)
    var config: PairConfigDto? = null;


    /**
     * 合约类型
     */
    var contractType: String = ContractType.USDT.value;


    /**
     * 实时价格
     */
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

    override fun toString(): String {
        return "PairInfoPo(index=$index, symbol=$symbol, config=$config, contractType='$contractType', ticker=$ticker)"
    }
}