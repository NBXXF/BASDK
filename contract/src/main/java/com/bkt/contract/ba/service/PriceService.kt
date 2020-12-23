package com.bkt.contract.ba.service

import com.bkt.contract.ba.common.HttpDataFunction
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.AdlQuantileDto
import com.bkt.contract.ba.model.dto.PremiumIndexPriceDto
import com.bkt.contract.ba.model.dto.TickerPriceDto
import com.bkt.contract.ba.model.event.IndexPriceEvent
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.bkt.contract.ba.sdk.ContractProxySocketService
import com.xxf.arch.json.datastructure.ListOrSingle
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.CacheType
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * @Description: 价格和资金汇率服务
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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
                        return t.getPremiumIndex(type, cacheTime, symbol, pair)
                                .map(HttpDataFunction())
                                .map {
                            it.get(0)
                        };
                    }
                })
    }


    /**
     * 订阅市价 和汇率结算 (注意:USD 并不包含指数价)
     * 如果只关心 指数价变化 请用 {@link #subIndexPrice(symbol: String)}
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
     *
     * 框架已经趋同 只调这个function 便可订阅指数价变化
     */
    fun subIndexPrice(symbol: String): Observable<IndexPriceEvent> {
        return PairService.INSTANCE.getPairType(symbol)
                .flatMap(object : Function<ContractType, ObservableSource<IndexPriceEvent>> {
                    override fun apply(t: ContractType): ObservableSource<IndexPriceEvent> {
                        /**
                         * usdt: socket 是markPrice
                         * usd socket是IndexPrice
                         */
                        if (t == ContractType.USD) {
                            return BaClient.instance.getSocketService(symbol, type = t)
                                    .flatMap(object : Function<ContractProxySocketService, ObservableSource<IndexPriceEvent>> {
                                        override fun apply(t: ContractProxySocketService): ObservableSource<IndexPriceEvent> {
                                            return t.subIndexPrice(symbol);
                                        }
                                    });
                        }
                        return BaClient.instance.getSocketService(symbol, type = t)
                                .flatMap(object : Function<ContractProxySocketService, ObservableSource<PremiumIndexPriceDto>> {
                                    override fun apply(t: ContractProxySocketService): ObservableSource<PremiumIndexPriceDto> {
                                        return t.subMarkPrice(symbol);
                                    }
                                }).map(object : Function<PremiumIndexPriceDto, IndexPriceEvent> {
                                    override fun apply(t: PremiumIndexPriceDto): IndexPriceEvent {
                                        return IndexPriceEvent(t.symbol, t.indexPrice);
                                    }
                                });
                    }
                });
    }

    /**
     * 获取最新价 返回一个
     */
    fun getTickerPrice(symbol: String): Observable<ListOrSingle<TickerPriceDto>> {
        return BaClient.instance.getApiService(symbol, null)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<TickerPriceDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<TickerPriceDto>> {
                        return t.getTickerPrice(symbol)
                                .map(HttpDataFunction());
                    }
                });
    }

    /**
     * 按类型获取最新价 返回多个
     */
    fun getTickerPrice(type: ContractType): Observable<ListOrSingle<TickerPriceDto>> {
        return BaClient.instance.initializer!!.getApiService(type)
                .getTickerPrice(null)
                .map(HttpDataFunction());
    }
}