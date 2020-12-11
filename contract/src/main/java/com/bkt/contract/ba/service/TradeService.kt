package com.bkt.contract.ba.service

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.TradeEventDto
import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.bkt.contract.ba.sdk.BaClient
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.CacheType
import java.util.concurrent.TimeUnit

/**
 * @Description: 成交service
 * @Author: XGod
 * @CreateDate: 2020/12/11 10:58
 */
interface TradeService : ExportService {

    companion object {
        internal val INSTANCE: TradeService by lazy {
            object : TradeService {
            }
        }
    }

    /**
     * 获取近期成交
     * 默认50条
     */
    fun getTrades(symbol: String,
                  cacheType: CacheType = CacheType.firstCache,
                  cacheTime: Long = TimeUnit.MINUTES.toMillis(5)): Observable<List<TradeEventDto>> {
        return PairService.INSTANCE
                .getPairType(symbol)
                .flatMap(object : Function<ContractType, ObservableSource<List<TradeEventDto>>> {
                    override fun apply(t: ContractType): ObservableSource<List<TradeEventDto>> {
                        return BaClient.instance.initializer!!.getApiService(t)
                                .getTrades(cacheType, cacheTime, symbol, null, null, 50)
                                .map(object : Function<List<TradeEventDto>, List<TradeEventDto>> {
                                    override fun apply(t: List<TradeEventDto>): List<TradeEventDto> {
                                        return t.filter {
                                            it.price.toDouble() > 0 && it.qty.toDouble() > 0;
                                        };
                                    }
                                });
                    }
                });
    }

    /**
     * 订阅成交  全量
     */
    fun subTrades(): Observable<List<TradeEventDto>> {
        return Observable.empty();
    }
}