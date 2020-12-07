package com.bkt.contract.ba.sdk

import android.util.Pair
import com.bkt.contract.ba.model.dto.DepthEventDto
import com.bkt.contract.ba.model.dto.ExchangeInfoDto
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.service.ContractDbService
import com.bkt.contract.ba.service.ContractProxyApiService
import com.bkt.contract.ba.service.ContractProxySocketService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import retrofit2.CacheType

/**
 * @Description: BA 客户端
 * @Author: XGod
 * @CreateDate: 2020/12/3 20:47
 */
interface BaClient {
    val api: ContractProxyApiService;
    val socket: ContractProxySocketService;
    val db: ContractDbService;


    /**
     * 订阅交易对变化
     * 第一步:网络数据
     * 第二步:订阅socket
     * 第三步:合成后监听数据库
     */
    fun subPair(): Observable<List<PairInfoPo>> {
        val apiData = Observable
                .zip(
                        api.getExchangeInfo(CacheType.onlyRemote),
                        api.getTicker24hr(CacheType.onlyRemote),
                        object : BiFunction<ExchangeInfoDto, List<TickerEventDto>, Pair<List<PairConfigDto>, List<TickerEventDto>>> {
                            override fun apply(t1: ExchangeInfoDto, t2: List<TickerEventDto>): Pair<List<PairConfigDto>, List<TickerEventDto>> {
                                return Pair.create(t1.symbols, t2);
                            }
                        }).flatMap(object : io.reactivex.functions.Function<Pair<List<PairConfigDto>, List<TickerEventDto>>, ObservableSource<List<PairInfoPo>>> {
                    override fun apply(t: Pair<List<PairConfigDto>, List<TickerEventDto>>): ObservableSource<List<PairInfoPo>> {
                        val tickerMap: MutableMap<String, TickerEventDto> = mutableMapOf();
                        for (ticker in t.second) {
                            ticker.symbol?.let { tickerMap.put(it, ticker) };
                        }
                        val list: MutableList<PairInfoPo> = mutableListOf<PairInfoPo>();
                        for ((index: Int, value: PairConfigDto) in t.first.withIndex()) {
                            val pairInfoPo = PairInfoPo(value.symbol);
                            pairInfoPo.config = value;
                            pairInfoPo.index = index.toLong();
                            pairInfoPo.ticker = tickerMap.get(value.symbol);
                            list.add(pairInfoPo);
                        }
                        return db.insertOrUpdate(list)
                                .flatMap(object : io.reactivex.functions.Function<List<PairInfoPo>, ObservableSource<List<PairInfoPo>>> {
                                    override fun apply(t: List<PairInfoPo>): ObservableSource<List<PairInfoPo>> {
                                        return Observable.empty();
                                    }
                                });
                    }
                }).onErrorResumeNext(Observable.empty());

        val socketData = socket.subTicker()
                .flatMap(object : io.reactivex.functions.Function<TickerEventDto, ObservableSource<List<PairInfoPo>>> {
                    override fun apply(t: TickerEventDto): ObservableSource<List<PairInfoPo>> {
                        val pairInfoPo = PairInfoPo(t.symbol);
                        pairInfoPo.ticker = t;
                        return db.insertOrUpdate(pairInfoPo)
                                .flatMap(object : io.reactivex.functions.Function<PairInfoPo, ObservableSource<List<PairInfoPo>>> {
                                    override fun apply(t: PairInfoPo): ObservableSource<List<PairInfoPo>> {
                                        return Observable.empty();
                                    }
                                });
                    }
                });
        val dbChange = db.subPairsChange();


        return Observable.concat(apiData, socketData, dbChange);
    }


    /**
     * 订阅深度变化
     */
    fun subDepth(symbol: String): Observable<DepthEventDto> {
        val apiData = api.getDepth(CacheType.onlyRemote, symbol, 50)
                .onErrorResumeNext(Observable.empty());
        val socketData = socket.subDepth(symbol);
        return Observable.concat(apiData, socketData);
    }
}