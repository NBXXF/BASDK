package com.bkt.contract.ba.service

import com.bkt.contract.ba.model.dto.PremiumIndexPriceDto
import com.bkt.contract.ba.model.event.IndexPriceEvent
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.bkt.contract.ba.sdk.ContractProxySocketService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.CacheType
import java.util.concurrent.TimeUnit

/**
 * @Description: 价格和资金汇率服务
 * @Author: XGod
 * @CreateDate: 2020/12/11 19:49
 */
interface PriceService : ExportService {
    companion object {
        internal val INSTANCE: PriceService by lazy {
            object : PriceService {
            }
        }
    }

    /**
     * 获取资金汇率和指数价格
     */
    fun getPremiumIndex(
            symbol: String,
            pair: String?,
            type: CacheType = CacheType.onlyRemote,
            cacheTime: Long = TimeUnit.MINUTES.toMillis(5)
    ): Observable<PremiumIndexPriceDto> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<PremiumIndexPriceDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<PremiumIndexPriceDto> {
                        return t.getPremiumIndex(type, cacheTime, symbol, pair);
                    }
                })
    }


    /**
     * 订阅市价 和汇率结算
     */
    fun subMarkPrice(symbol: String): Observable<PremiumIndexPriceDto> {
        return BaClient.instance.getSocketService(symbol)
                .flatMap(object : Function<ContractProxySocketService, ObservableSource<PremiumIndexPriceDto>> {
                    override fun apply(t: ContractProxySocketService): ObservableSource<PremiumIndexPriceDto> {
                        return t.subMarkPrice(symbol);
                    }
                });
    }

    /**
     * 订阅指数价
     */
    @Deprecated("过时  请用subMarkPrice")
    fun subIndexPrice(symbol: String): Observable<IndexPriceEvent> {
        return BaClient.instance.getSocketService(symbol)
                .flatMap(object : Function<ContractProxySocketService, ObservableSource<IndexPriceEvent>> {
                    override fun apply(t: ContractProxySocketService): ObservableSource<IndexPriceEvent> {
                        return t.subIndexPrice(symbol);
                    }
                });
    }

}