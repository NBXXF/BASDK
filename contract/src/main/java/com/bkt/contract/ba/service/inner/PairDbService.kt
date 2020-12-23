package com.bkt.contract.ba.service.inner

import com.bkt.contract.ba.common.merge.PairInfoPoListMergeFunction
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.model.po.PairInfoPo_
import com.bkt.contract.ba.sdk.ObjectBoxFactory
import com.xxf.database.xxf.objectbox.RxQuery
import com.xxf.database.xxf.objectbox.XXFObjectBoxUtils
import io.objectbox.query.Query
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap

/**
 * @Description: 交易对db service
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/9 15:14
 */
internal object PairDbService {
    /**
     * 业务频繁使用  这里用内存缓存
     */
    private val pairConfigSubject: BehaviorSubject<ConcurrentHashMap<String, PairConfigDto>> = BehaviorSubject.createDefault(ConcurrentHashMap());


    /**
     * 获取内存中的配置信息
     */
    fun getPairConfig(): Observable<ConcurrentHashMap<String, PairConfigDto>> {
        if (pairConfigSubject.value?.isEmpty()!!) {
            return queryAll()
                    .flatMap(object : Function<List<PairInfoPo>, ObservableSource<ConcurrentHashMap<String, PairConfigDto>>> {
                        override fun apply(t: List<PairInfoPo>): ObservableSource<ConcurrentHashMap<String, PairConfigDto>> {
                            t.forEach {
                                if (it.config != null && it.symbol != null) {
                                    pairConfigSubject.value!!.put(it.symbol!!, it.config!!)
                                }
                            }
                            return pairConfigSubject;
                        }
                    })
        }
        return pairConfigSubject;
    }

    /**
     * 无缝插入更新
     */
    fun insertOrUpdate(pairInfo: PairInfoPo): Observable<PairInfoPo> {
        return this.insertOrUpdate(listOf(pairInfo)).map { pairInfo };
    }

    /**
     * 无缝插入更新
     */
    fun insertOrUpdate(list: List<PairInfoPo>): Observable<List<PairInfoPo>> {
        return Observable
                .fromCallable(object : Callable<List<PairInfoPo>> {
                    override fun call(): List<PairInfoPo> {
                        XXFObjectBoxUtils.put(ObjectBoxFactory.getBoxStore().boxFor(PairInfoPo::class.java), list, PairInfoPoListMergeFunction());
                        syncMemory(list);
                        return list;
                    }
                }).subscribeOn(Schedulers.io());
    }


    /**
     * 查询所有交易对
     */
    fun queryAll(): Observable<List<PairInfoPo>> {
        return Observable
                .fromCallable(object : Callable<List<PairInfoPo>> {
                    override fun call(): List<PairInfoPo> {
                        return ObjectBoxFactory.getBoxStore()
                                .boxFor(PairInfoPo::class.java)
                                .query()
                                .order(PairInfoPo_.index)
                                .build().find();
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     * 查询单个交易对
     */
    fun query(symbol: String): Observable<PairInfoPo> {
        return Observable
                .defer(object : Callable<ObservableSource<PairInfoPo>> {
                    override fun call(): ObservableSource<PairInfoPo> {
                        val findFirst = ObjectBoxFactory.getBoxStore()
                                .boxFor(PairInfoPo::class.java)
                                .query()
                                .equal(PairInfoPo_.symbol, symbol)
                                .build().findFirst();
                        if (findFirst == null) {
                            return Observable.empty();
                        }
                        return Observable.just(findFirst);
                    }

                }).subscribeOn(Schedulers.io());
    }


    /**
     * 订阅所有交易对变化
     */
    fun subChange(): Observable<List<PairInfoPo>> {
        val build: Query<PairInfoPo> = ObjectBoxFactory.getBoxStore().boxFor(PairInfoPo::class.java)
                .query()
                .order(PairInfoPo_.index)
                .build()
        return RxQuery.observableChange<PairInfoPo>(build);
    }

    /**
     * 订阅指定类型的交易对变化
     */
    fun subChange(type: ContractType): Observable<List<PairInfoPo>> {
        val build: Query<PairInfoPo> = ObjectBoxFactory.getBoxStore().boxFor(PairInfoPo::class.java)
                .query()
                .equal(PairInfoPo_.contractType, type.value)
                .order(PairInfoPo_.index)
                .build()
        return RxQuery.observableChange<PairInfoPo>(build);
    }

    /**
     * 订阅指定交易对变化
     */
    fun subChange(vararg symbols: String): Observable<List<PairInfoPo>> {
        val build: Query<PairInfoPo> = ObjectBoxFactory.getBoxStore().boxFor(PairInfoPo::class.java)
                .query()
                .`in`(PairInfoPo_.symbol, symbols)
                .order(PairInfoPo_.index)
                .build()
        return RxQuery.observableChange<PairInfoPo>(build);
    }

    /**
     * 同步内存
     */
    private fun syncMemory(list: List<PairInfoPo>?) {
        try {
            if (list != null && !list.isEmpty()) {
                list.map {
                    it.symbol?.let { it1 -> it.config?.let { it2 -> pairConfigSubject.value?.put(it1, it2) } };
                }
                pairConfigSubject.onNext(pairConfigSubject.value!!);
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}