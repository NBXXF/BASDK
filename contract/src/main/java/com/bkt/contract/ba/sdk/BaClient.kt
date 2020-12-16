package com.bkt.contract.ba.sdk

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.TradeEventDto
import com.bkt.contract.ba.service.*
import com.bkt.contract.ba.service.PriceService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.lang.RuntimeException
import java.util.concurrent.Callable

/**
 * @Description: BA 客户端
 * @Author: XGod
 * @CreateDate: 2020/12/3 20:47
 */
class BaClient private constructor() {

    companion object {
        @JvmStatic
        val instance: BaClient by lazy {
            BaClient();
        }
    }

    interface Initializer {
        fun getApiService(type: ContractType): ContractProxyApiService;
        fun getSocketService(type: ContractType): ContractProxySocketService;
    }

    internal var initializer: Initializer? = null
        get() {
            if (field == null) {
                throw RuntimeException("BaClient must init");
            }
            return field;
        }

    /**
     * 初始化
     */
    fun init(initializer: Initializer) {
        this.initializer = initializer;
    }

    /**
     * 对外暴露服务
     */
    fun <T : ExportService> getService(clazz: Class<T>): T {
        if (clazz == PairService::class.java) {
            return PairService.INSTANCE as T;
        }
        if (clazz == DepthService::class.java) {
            return DepthService.INSTANCE as T;
        }
        if (clazz == TradeService::class.java) {
            return TradeService.INSTANCE as T;
        }
        if (clazz == PriceService::class.java) {
            return PriceService.INSTANCE as T;
        }
        if (clazz == KLineService::class.java) {
            return KLineService.INSTANCE as T;
        }
        if (clazz == CommonService::class.java) {
            return CommonService.INSTANCE as T;
        }
        if (clazz == OrderService::class.java) {
            return OrderService.INSTANCE as T;
        }
        if (clazz == UserService::class.java) {
            return UserService.INSTANCE as T;
        }
        return PairService.INSTANCE as T;
    }

    /**
     * 获取api service
     */
    internal fun getApiService(symbol: String, type: ContractType? = null): Observable<ContractProxyApiService> {
        if (type == null) {
            return getService(PairService::class.java)
                    .getPairType(symbol)
                    .map(object : Function<ContractType, ContractProxyApiService> {
                        override fun apply(t: ContractType): ContractProxyApiService {
                            return BaClient.instance.initializer!!.getApiService(t);
                        }
                    });
        }
        return Observable.fromCallable(object : Callable<ContractProxyApiService> {
            override fun call(): ContractProxyApiService {
                return BaClient.instance.initializer!!.getApiService(type!!);
            }
        })
    }

    /**
     * 获取socket service
     */
    internal fun getSocketService(symbol: String, type: ContractType? = null): Observable<ContractProxySocketService> {
        if (type == null) {
            return getService(PairService::class.java)
                    .getPairType(symbol)
                    .map(object : Function<ContractType, ContractProxySocketService> {
                        override fun apply(t: ContractType): ContractProxySocketService {
                            return BaClient.instance.initializer!!.getSocketService(t);
                        }
                    });
        }
        return Observable.fromCallable(object : Callable<ContractProxySocketService> {
            override fun call(): ContractProxySocketService {
                return BaClient.instance.initializer!!.getSocketService(type!!);
            }
        })
    }

}