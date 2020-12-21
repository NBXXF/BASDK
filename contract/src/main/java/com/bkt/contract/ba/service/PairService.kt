package com.bkt.contract.ba.service

import android.text.TextUtils
import androidx.annotation.Nullable
import com.bkt.contract.ba.common.HttpDataFunction
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.*
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.bkt.contract.ba.service.inner.PairDbService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.functions.Function
import retrofit2.CacheType
import retrofit2.http.Cache
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


/**
 * @Description: 交易对service
 * @Author: XGod
 * @CreateDate: 2020/12/9 14:52
 */
interface PairService : ExportService {
    companion object {
        internal val INSTANCE: PairService by lazy {
            object : PairService {
                init {
                    /**
                     * 同步一下 交易对配置
                     */
                    innerSyncPair().retry(3).subscribe();
                }
            }
        }

        /**
         * 同步交易对
         */
        internal fun innerSyncPair(): Observable<List<PairInfoPo>> {
            val usdtApiService = BaClient.instance.initializer!!.getApiService(ContractType.USDT);
            val currencyApiService = BaClient.instance.initializer!!.getApiService(ContractType.USD);
            return Observable
                    .zip(
                            usdtApiService.getExchangeInfo(CacheType.onlyRemote).map(HttpDataFunction()),
                            usdtApiService.getTicker24hr(CacheType.onlyRemote).map(HttpDataFunction()),
                            currencyApiService.getExchangeInfo(CacheType.onlyRemote).map(HttpDataFunction()),
                            currencyApiService.getTicker24hr(CacheType.onlyRemote).map(HttpDataFunction()),
                            object : io.reactivex.functions.Function4<ExchangeInfoDto, List<TickerEventDto>, ExchangeInfoDto, List<TickerEventDto>, List<PairInfoPo>> {
                                override fun apply(t1: ExchangeInfoDto, t2: List<TickerEventDto>, t3: ExchangeInfoDto, t4: List<TickerEventDto>): List<PairInfoPo> {
                                    val pairConfigList: MutableList<PairConfigDto> = mutableListOf<PairConfigDto>();
                                    t1.symbols?.let { pairConfigList.addAll(it) };
                                    t3.symbols?.let { pairConfigList.addAll(it) };

                                    val tickerMap: MutableMap<String, TickerEventDto> = mutableMapOf();
                                    for (ticker in t2) {
                                        ticker.symbol?.let { tickerMap.put(it, ticker) };
                                    }
                                    for (ticker in t4) {
                                        ticker.symbol?.let { tickerMap.put(it, ticker) };
                                    }

                                    val pairPosList: MutableList<PairInfoPo> = mutableListOf<PairInfoPo>();
                                    for (value: PairConfigDto in t1.symbols!!) {
                                        val pairInfoPo = PairInfoPo(value.symbol);
                                        value.contractClassifyType = ContractType.USDT;
                                        pairInfoPo.config = value;
                                        pairInfoPo.index = pairPosList.size;
                                        pairInfoPo.contractType = ContractType.USDT.value;
                                        pairInfoPo.ticker = tickerMap.get(value.symbol);
                                        pairPosList.add(pairInfoPo);
                                    }

                                    for (value: PairConfigDto in t3.symbols!!) {
                                        value.contractClassifyType = ContractType.USD;
                                        val pairInfoPo = PairInfoPo(value.symbol);
                                        pairInfoPo.config = value;
                                        pairInfoPo.index = pairPosList.size;
                                        pairInfoPo.contractType = ContractType.USD.value;
                                        pairInfoPo.ticker = tickerMap.get(value.symbol);
                                        pairPosList.add(pairInfoPo);
                                    }
                                    return pairPosList;
                                }
                            }
                    )
                    .flatMap(object : Function<List<PairInfoPo>, ObservableSource<List<PairInfoPo>>> {
                        override fun apply(t: List<PairInfoPo>): ObservableSource<List<PairInfoPo>> {
                            return PairDbService.insertOrUpdate(t);
                        }
                    });
        }
    }

    /**
     * 获取所有交易对
     */
    fun getPairs(): Observable<List<PairInfoPo>> {
        return PairDbService.queryAll()
                .flatMap(object : Function<List<PairInfoPo>, ObservableSource<List<PairInfoPo>>> {
                    override fun apply(p: List<PairInfoPo>): ObservableSource<List<PairInfoPo>> {
                        if (p.isEmpty()) {
                            return innerSyncPair();
                        }
                        return Observable.just(p);
                    }
                })
                .switchIfEmpty(innerSyncPair())
                .onErrorResumeNext(innerSyncPair());
    }


    /**
     * 获取分类交易对
     */
    fun getPairs(type: ContractType): Observable<List<PairInfoPo>> {
        if (type == null) {
            return Observable.empty();
        }
        return getPairs()
                .map(object : Function<List<PairInfoPo>, List<PairInfoPo>> {
                    override fun apply(t: List<PairInfoPo>): List<PairInfoPo> {
                        val pairPosList: MutableList<PairInfoPo> = mutableListOf<PairInfoPo>();
                        for (item: PairInfoPo in t) {
                            if (TextUtils.equals(item.contractType, type.value)) {
                                pairPosList.add(item);
                            }
                        }
                        return pairPosList;
                    }
                })
    }

    /**
     * 获取指定交易名称名称的交易对信息列表
     */
    fun getPairs(vararg symbols: String): Observable<List<PairInfoPo>> {
        if (symbols == null || symbols.isEmpty()) {
            return Observable.empty();
        }
        return getPairs()
                .map(object : Function<List<PairInfoPo>, List<PairInfoPo>> {
                    override fun apply(t: List<PairInfoPo>): List<PairInfoPo> {
                        val pairPosList: MutableList<PairInfoPo> = mutableListOf<PairInfoPo>();
                        for (item: PairInfoPo in t) {
                            if (symbols.contains(item.symbol)) {
                                pairPosList.add(item);
                            }
                        }
                        return pairPosList;
                    }
                })
    }


    /**
     * 获取指定交易对
     * 为空 返回Observable.empty()
     */
    fun getPair(symbol: String): Observable<PairInfoPo> {
        return getPairs()
                .flatMap(object : Function<List<PairInfoPo>, ObservableSource<PairInfoPo>> {
                    override fun apply(t: List<PairInfoPo>): ObservableSource<PairInfoPo> {
                        for (item: PairInfoPo in t) {
                            if (TextUtils.equals(item.symbol, symbol)) {
                                return Observable.just(item);
                            }
                        }
                        return Observable.empty();
                    }
                });
    }

    /**
     * 获取交易对类型
     */
    fun getPairType(symbol: String): Observable<ContractType> {
        return getPair(symbol)
                .map {
                    ContractType.from(it.contractType);
                }
    }

    /**
     * 获取交易对配置 有内存缓存加速 快
     */
    fun getPairConfigs(): Map<String, PairConfigDto> {
        try {
            return PairDbService.getPairConfig().blockingFirst();
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return mutableMapOf();
    }

    /**
     * 获取交易对配置信息  有内存缓存加速 快
     */
    @CheckReturnValue
    @Nullable
    fun getPairConfig(symbol: String?): PairConfigDto? {
        try {
            return getPairConfigs().get(symbol);
        } catch (e: Throwable) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 订阅所有交易对变化
     * ！！！下游要安全处理,否则会中断订阅
     *  @param applyDispose 是否跟随取消socket
     */
    fun subPairs(applyDispose: Boolean = true): Observable<List<PairInfoPo>> {
        return Observable.merge(
                BaClient.instance.initializer!!.getSocketService(ContractType.USDT).subTicker(applyDispose)
                        .flatMap(object : Function<List<TickerEventDto>, Observable<List<PairInfoPo>>> {
                            override fun apply(t: List<TickerEventDto>): Observable<List<PairInfoPo>> {
                                return Observable.empty();
                            }
                        }),
                BaClient.instance.initializer!!.getSocketService(ContractType.USD).subTicker(applyDispose)
                        .flatMap(object : Function<List<TickerEventDto>, Observable<List<PairInfoPo>>> {
                            override fun apply(t: List<TickerEventDto>): Observable<List<PairInfoPo>> {
                                return Observable.empty();
                            }
                        }),
                PairDbService.subChange());
    }

    /**
     * 订阅指定类型交易对
     * ！！！下游要安全处理,否则会中断订阅
     * @param type 合约类型
     * @param applyDispose 是否跟随取消socket
     */
    fun subPairs(type: ContractType, applyDispose: Boolean = true): Observable<List<PairInfoPo>> {
        if (type == null) {
            return Observable.empty();
        }
        return Observable.merge(
                BaClient.instance.initializer!!.getSocketService(type).subTicker(applyDispose)
                        .flatMap(object : Function<List<TickerEventDto>, Observable<List<PairInfoPo>>> {
                            override fun apply(t: List<TickerEventDto>): Observable<List<PairInfoPo>> {
                                return Observable.empty();
                            }
                        }),
                PairDbService.subChange(type));
    }

    /**
     * 订阅指定交易对的交易对变化
     * ！！！下游要安全处理,否则会中断订阅
     * 可能包含usdt 和usd
     * @param symbols
     * @param applyDispose 是否跟随取消socket
     */
    fun subPairs(vararg symbols: String, applyDispose: Boolean = true): Observable<List<PairInfoPo>> {
        if (symbols == null || symbols.isEmpty()) {
            return Observable.empty();
        }
        return Observable.merge(
                BaClient.instance.initializer!!.getSocketService(ContractType.USDT).subTicker(applyDispose)
                        .flatMap(object : Function<List<TickerEventDto>, Observable<List<PairInfoPo>>> {
                            override fun apply(t: List<TickerEventDto>): Observable<List<PairInfoPo>> {
                                return Observable.empty();
                            }
                        }),
                BaClient.instance.initializer!!.getSocketService(ContractType.USD).subTicker(applyDispose)
                        .flatMap(object : Function<List<TickerEventDto>, Observable<List<PairInfoPo>>> {
                            override fun apply(t: List<TickerEventDto>): Observable<List<PairInfoPo>> {
                                return Observable.empty();
                            }
                        }),
                PairDbService.subChange(*symbols)
        );
    }

    /**
     * 获取最新价
     */
    fun getTicker24hr(symbol: String, type: CacheType = CacheType.onlyRemote, cacheTime: Long = TimeUnit.MINUTES.toMillis(5)): Observable<TickerEventDto> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<TickerEventDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<TickerEventDto> {
                        return t.getTicker24hr(type, cacheTime, symbol)
                                .map(HttpDataFunction())
                    }
                })
    }
}

