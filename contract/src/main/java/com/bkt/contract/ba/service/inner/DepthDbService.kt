package com.bkt.contract.ba.service.inner

import com.bkt.contract.ba.common.merge.DepthEventDtoPoMergeFunction
import com.bkt.contract.ba.common.merge.PairInfoPoListMergeFunction
import com.bkt.contract.ba.model.po.DepthEventDtoPo
import com.bkt.contract.ba.model.po.DepthEventDtoPo_
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
 * @Description: 深度数据库服务
 * @Author: XGod
 * @CreateDate: 2020/12/10 18:24
 */
internal object DepthDbService {
    /**
     * 无缝插入更新
     */
    fun insertOrUpdate(depth: DepthEventDtoPo): Observable<DepthEventDtoPo> {
        return Observable
                .fromCallable(object : Callable<DepthEventDtoPo> {
                    override fun call(): DepthEventDtoPo {
                        XXFObjectBoxUtils.put(ObjectBoxFactory.getBoxStore().boxFor(DepthEventDtoPo::class.java), depth, DepthEventDtoPoMergeFunction());
                        return depth;
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 订阅变化
     */
    fun subChange(symbol: String): Observable<DepthEventDtoPo> {
        val build: Query<DepthEventDtoPo> = ObjectBoxFactory.getBoxStore().boxFor(DepthEventDtoPo::class.java)
                .query()
                .equal(DepthEventDtoPo_.symbol, symbol)
                .build()
        return RxQuery.observableChange<DepthEventDtoPo>(build)
                .filter {
                    !it.isEmpty()
                }.map {
                    it.get(0);
                };
    }
}