package com.bkt.contract.ba.model.po

import android.text.TextUtils
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.bkt.contract.ba.service.CommonService
import com.xxf.arch.json.JsonUtils
import com.xxf.database.xxf.objectbox.id.IdUtils
import io.objectbox.annotation.*
import io.objectbox.converter.PropertyConverter
import java.io.Serializable

/**
 * @Description: 交易对 信息(配置+价格涨幅信息)
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/3 19:37
 */
@Entity
open class PairInfoPo : Serializable {

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
     * 合约类型 USDT/USD
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

    constructor(index: Int, symbol: String?, config: PairConfigDto?, contractType: String, ticker: TickerEventDto?) {
        this.index = index
        this.symbol = symbol
        this.config = config
        this.contractType = contractType
        this.ticker = ticker
    }

    fun copy(): PairInfoPo {
        return PairInfoPo(this.index, this.symbol, this.config, this.contractType, this.ticker);
    }

    internal class CoinConfigPoConverter : PropertyConverter<PairConfigDto?, String?> {
        override fun convertToEntityProperty(databaseValue: String?): PairConfigDto? {
            return if (TextUtils.isEmpty(databaseValue)) {
                null
            } else JsonUtils.toBean<PairConfigDto>(databaseValue, PairConfigDto::class.java)
        }

        override fun convertToDatabaseValue(entityProperty: PairConfigDto?): String? {
            return JsonUtils.toJsonString(entityProperty)
        }
    }

    internal class TickerPoConverter : PropertyConverter<TickerEventDto?, String?> {
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