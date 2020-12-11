package com.bkt.contract.ba.sdk

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.service.DepthService
import com.bkt.contract.ba.service.ExportService
import com.bkt.contract.ba.service.PairService
import com.bkt.contract.ba.service.TradeService
import java.lang.RuntimeException

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
        return PairService.INSTANCE as T;
    }

}