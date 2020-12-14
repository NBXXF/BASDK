package com.bkt.contract.ba.service

import com.bkt.contract.ba.model.dto.KLineEventDto
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.bkt.contract.ba.sdk.ContractProxySocketService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.CacheType
import java.util.concurrent.TimeUnit

/**
 * @Description: K线 service
 * @Author: XGod
 * @CreateDate: 2020/12/12 9:52
 */
interface KLineService : ExportService {
    companion object {
        internal val INSTANCE: KLineService by lazy {
            object : KLineService {
            }
        }
    }

    /**
     * 获取k线
     * 支持分页 支持缓存
     * @param symbol
     * @param interval  m -> 分钟; h -> 小时; d -> 天; w -> 周; M -> 月
     * 1m
     * 3m
     * 5m
     * 15m
     * 30m
     * 1h
     * 2h
     * 4h
     * 6h
     * 8h
     * 12h
     * 1d
     * 3d
     * 1w
     * 1M
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @param limit
     */
    fun getKLine(symbol: String,
                 interval: String,
                 startTime: Long?,
                 endTime: Long?,
                 limit: Int?,
                 type: CacheType = CacheType.onlyRemote,
                 cacheTime: Long = TimeUnit.MINUTES.toMillis(5)): Observable<List<KLineEventDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<List<KLineEventDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<List<KLineEventDto>> {
                        return t.getKLines(type, cacheTime, symbol, interval, startTime, endTime, limit);
                    }
                }).map(object : Function<List<KLineEventDto>, List<KLineEventDto>> {
                    override fun apply(t: List<KLineEventDto>): List<KLineEventDto> {
                        /**
                         * http 过来没有symbol字段
                         */
                        t.forEach {
                            it.symbol = symbol;
                        }
                        return t;
                    }
                })
    }

    /**
     * 订阅k线 【增量】
     * 上层 注意和http返回去重
     * ！！！下游要安全处理,否则会中断订阅
     * @param symbol
     * @param interval  m -> 分钟; h -> 小时; d -> 天; w -> 周; M -> 月
     * 1m
     * 3m
     * 5m
     * 15m
     * 30m
     * 1h
     * 2h
     * 4h
     * 6h
     * 8h
     * 12h
     * 1d
     * 3d
     * 1w
     * 1M
     */
    fun subKLine(symbol: String,
                 interval: String): Observable<KLineEventDto> {
        return BaClient.instance.getSocketService(symbol)
                .flatMap(object : Function<ContractProxySocketService, ObservableSource<KLineEventDto>> {
                    override fun apply(t: ContractProxySocketService): ObservableSource<KLineEventDto> {
                        return t.subKLine(symbol, interval);
                    }
                });
    }
}