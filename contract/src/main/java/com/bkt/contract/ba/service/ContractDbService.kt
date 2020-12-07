package com.bkt.contract.ba.service

import com.bkt.contract.ba.common.PairInfoPoListMergeFunction
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.model.po.PairInfoPo_
import com.xxf.database.xxf.objectbox.RxQuery
import com.xxf.database.xxf.objectbox.XXFObjectBoxUtils
import io.objectbox.BoxStore
import io.objectbox.query.Query
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

/**
 * @Description: contract db service
 * @Author: XGod
 * @CreateDate: 2020/12/7 10:23
 */
interface ContractDbService {

    fun getBoxStore(): BoxStore? {
        return null;
    }

    fun insertOrUpdate(pairInfo: PairInfoPo): Observable<PairInfoPo> {
        return this.insertOrUpdate(listOf(pairInfo)).map { pairInfo };
    }

    fun insertOrUpdate(list: List<PairInfoPo>): Observable<List<PairInfoPo>> {
        return Observable
                .fromCallable(object : Callable<List<PairInfoPo>> {
                    override fun call(): List<PairInfoPo> {
                        XXFObjectBoxUtils.put(getBoxStore()!!.boxFor(PairInfoPo::class.java), list, PairInfoPoListMergeFunction());
                        return list;
                    }
                }).subscribeOn(Schedulers.io());
    }

    fun subPairsChange(): Observable<List<PairInfoPo>> {
        val build: Query<PairInfoPo> = getBoxStore()!!.boxFor(PairInfoPo::class.java)
                .query()
                .order(PairInfoPo_.index)
                .build()
        return RxQuery.observableChange<PairInfoPo>(build);
    }

    fun getPairs(): Observable<List<PairInfoPo>> {
        return Observable
                .fromCallable(Callable {
                    getBoxStore()!!.boxFor(PairInfoPo::class.java).query().build().find();
                }).subscribeOn(Schedulers.io());
    }
}