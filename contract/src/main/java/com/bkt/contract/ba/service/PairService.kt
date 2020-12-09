package com.bkt.contract.ba.service

import android.text.TextUtils
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.ExchangeInfoDto
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.service.inner.PairDbService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.CacheType


/**
 * @Description: 交易对service
 * @Author: XGod
 * @CreateDate: 2020/12/9 14:52
 */
interface PairService : ExportService {
    /**
     * 获取所有交易对
     */
    fun getPairs(): Observable<List<PairInfoPo>> {
        return PairDbService.INSTANCE.queryAll()
                .flatMap(object : Function<List<PairInfoPo>, ObservableSource<List<PairInfoPo>>> {
                    override fun apply(p: List<PairInfoPo>): ObservableSource<List<PairInfoPo>> {
                        if (p.isEmpty()) {
                            return syncPair();
                        }
                        return Observable.just(p);
                    }
                })
                .switchIfEmpty(syncPair())
                .onErrorResumeNext(syncPair());
    }

    /**
     * 同步交易对
     */
    private fun syncPair(): Observable<List<PairInfoPo>> {
        val usdtApiService = BaClient.instance.initializer!!.getApiService(ContractType.USDT);
        val currencyApiService = BaClient.instance.initializer!!.getApiService(ContractType.USD);
        return Observable
                .zip(
                        usdtApiService.getExchangeInfo(CacheType.onlyRemote),
                        usdtApiService.getTicker24hr(CacheType.onlyRemote),
                        currencyApiService.getExchangeInfo(CacheType.onlyRemote),
                        currencyApiService.getTicker24hr(CacheType.onlyRemote),
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
                                    pairInfoPo.config = value;
                                    pairInfoPo.index = pairPosList.size;
                                    pairInfoPo.contractType = ContractType.USDT.value;
                                    pairInfoPo.ticker = tickerMap.get(value.symbol);
                                    pairPosList.add(pairInfoPo);
                                }

                                for (value: PairConfigDto in t3.symbols!!) {
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
                        return PairDbService.INSTANCE.insertOrUpdate(t);
                    }
                });
    }


    /**
     * 获取分类交易对
     */
    fun getPairs(type: ContractType): Observable<List<PairInfoPo>> {
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
     * 获取指定交易对
     * 为空 返回Observable.empty()
     */
    fun getPair(symbol: String): Observable<PairInfoPo> {
        return getPairs()
                .flatMap(object : Function<List<PairInfoPo>, ObservableSource<PairInfoPo>> {
                    override fun apply(t: List<PairInfoPo>): ObservableSource<PairInfoPo> {
                        for (item: PairInfoPo in t) {
                            if (TextUtils.equals(item.contractType, symbol)) {
                                return Observable.just(item);
                            }
                        }
                        return Observable.empty();
                    }
                });
    }

    /**
     * 订阅所有交易对变化
     * ！！！下游要安全处理,否则会中断订阅
     */
    fun subPairs(): Observable<List<PairInfoPo>> {
        return PairDbService.INSTANCE.subChange();
    }

    /**
     * 订阅指定类型交易对
     * ！！！下游要安全处理,否则会中断订阅
     */
    fun subPairs(type: ContractType): Observable<List<PairInfoPo>> {
        return getPairs(type)
                .flatMap(object : Function<List<PairInfoPo>, Observable<List<PairInfoPo>>> {
                    override fun apply(t: List<PairInfoPo>): Observable<List<PairInfoPo>> {
                        val pairNameList: MutableList<String> = mutableListOf();
                        for (item: PairInfoPo in t) {
                            item.symbol?.let { pairNameList.add(it) };
                        }
                        if (pairNameList.isEmpty()) {
                            return Observable.empty();
                        }
                        val toTypedArray = pairNameList.toTypedArray();
                        return PairDbService.INSTANCE.subChange(*toTypedArray);
                    }
                });
    }

    /**
     * 订阅指定交易对的交易对变化
     * ！！！下游要安全处理,否则会中断订阅
     */
    fun subPairs(vararg pairs: String): Observable<List<PairInfoPo>> {
        if (pairs == null || pairs.isEmpty()) {
            return Observable.empty();
        }
        return PairDbService.INSTANCE.subChange(*pairs);
    }
}

