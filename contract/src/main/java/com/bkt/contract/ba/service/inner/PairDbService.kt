package com.bkt.contract.ba.service.inner

import com.bkt.contract.ba.common.merge.PairInfoPoListMergeFunction
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.model.po.PairInfoPo_
import com.bkt.contract.ba.sdk.ObjectBoxFactory
import com.xxf.database.xxf.objectbox.RxQuery
import com.xxf.database.xxf.objectbox.XXFObjectBoxUtils
import io.objectbox.query.Query
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

/**
 * @Description: 交易对db service
 * @Author: XGod
 * @CreateDate: 2020/12/9 15:14
 */
internal object PairDbService  {
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
}