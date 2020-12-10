package com.bkt.contract.ba.service

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.DepthEventDto
import com.bkt.contract.ba.sdk.BaClient
import com.xxf.arch.XXF
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.CacheType
import java.util.concurrent.TimeUnit

/**
 * @Description: 深度服务
 * @Author: XGod
 * @CreateDate: 2020/12/10 15:45
 */
interface DepthService : ExportService {

    companion object {
        internal val INSTANCE: DepthService by lazy {
            object : DepthService {
            }
        }
    }

    /**
     *  获取深度
     *  @param symbol 交易对名称
     *  @param cacheType 缓存类型
     *  @param cacheTime 缓存时间
     */
    fun getDepth(symbol: String,
                 cacheType: CacheType = CacheType.firstCache,
                 cacheTime: Long = TimeUnit.MINUTES.toMillis(5)): Observable<DepthEventDto> {
        return PairService.INSTANCE
                .getPairType(symbol)
                .flatMap(object : Function<ContractType, ObservableSource<DepthEventDto>> {
                    override fun apply(t: ContractType): ObservableSource<DepthEventDto> {
                        return BaClient.instance.initializer!!.getApiService(t).getDepth(cacheType, cacheTime, symbol, 50);
                    }
                });
    }

    /**
     * 订阅深度变化
     *  @param symbol 交易对名称
     */
    fun subDepth(symbol: String): Observable<DepthEventDto> {
        return PairService.INSTANCE
                .getPairType(symbol)
                .flatMap(object : Function<ContractType, ObservableSource<DepthEventDto>> {
                    override fun apply(t: ContractType): ObservableSource<DepthEventDto> {
                        return BaClient.instance.initializer!!.getSocketService(t).subDepth(symbol);
                    }
                });
    }
}