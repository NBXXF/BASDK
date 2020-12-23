package com.bkt.contract.ba.service.inner

import com.bkt.contract.ba.common.merge.PairTradePoMergeFunction
import com.bkt.contract.ba.model.po.*
import com.bkt.contract.ba.sdk.ObjectBoxFactory
import com.xxf.database.xxf.objectbox.RxQuery
import com.xxf.database.xxf.objectbox.XXFObjectBoxUtils
import io.objectbox.query.Query
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

/**
 * @Description: 深度数据库服务
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/10 18:24
 */
internal object TradeDbService {
    /**
     * 无缝插入更新
     */
    fun insertOrUpdate(pairTrade: PairTradePo): Observable<PairTradePo> {
        return Observable
                .fromCallable(object : Callable<PairTradePo> {
                    override fun call(): PairTradePo {
                        XXFObjectBoxUtils.put(ObjectBoxFactory.getBoxStore().boxFor(PairTradePo::class.java), pairTrade, PairTradePoMergeFunction());
                        return pairTrade;
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 订阅变化
     */
    fun subChange(symbol: String): Observable<PairTradePo> {
        val build: Query<PairTradePo> = ObjectBoxFactory.getBoxStore().boxFor(PairTradePo::class.java)
                .query()
                .equal(PairTradePo_.symbol, symbol)
                .build()
        return RxQuery.observableChange<PairTradePo>(build)
                .filter {
                    !it.isEmpty()
                }.map {
                    it.get(0);
                };
    }
}