package com.bkt.contract.ba.service

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.TradeEventDto
import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.bkt.contract.ba.sdk.ContractProxySocketService
import com.bkt.contract.ba.service.inner.TradeDbService
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
     *  max item=20
     */
    fun getTrades(symbol: String,
                  cacheType: CacheType = CacheType.firstCache,
                  cacheTime: Long = TimeUnit.MINUTES.toMillis(5)): Observable<List<TradeEventDto>> {
        return BaClient.instance
                .getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<List<TradeEventDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<List<TradeEventDto>> {
                        return t.getTrades(cacheType, cacheTime, symbol, null, null, 20)
                                .map(object : Function<List<TradeEventDto>, List<TradeEventDto>> {
                                    override fun apply(t: List<TradeEventDto>): List<TradeEventDto> {
                                        return t.sortedByDescending { it.time.origin };
                                    }
                                });
                    }
                });
    }

    /**
     * 订阅成交  全量 max item=20
     */
    fun subTrades(symbol: String): Observable<List<TradeEventDto>> {
        return Observable.merge(BaClient.instance
                .getSocketService(symbol)
                .flatMap(object : Function<ContractProxySocketService, ObservableSource<List<TradeEventDto>>> {
                    override fun apply(t: ContractProxySocketService): ObservableSource<List<TradeEventDto>> {
                        return t.subTrade(symbol)
                                .flatMap(object : Function<TradeEventDto, ObservableSource<List<TradeEventDto>>> {
                                    override fun apply(t: TradeEventDto): ObservableSource<List<TradeEventDto>> {
                                        return Observable.empty();
                                    }
                                });
                    }
                }),
                TradeDbService.subChange(symbol).map { it.trades }
        );
    }
}