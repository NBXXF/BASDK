package com.bkt.contract.ba.sdk

import android.util.Pair
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.DepthEventDto
import com.bkt.contract.ba.model.dto.ExchangeInfoDto
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.bkt.contract.ba.model.dto.TickerEventDto
import com.bkt.contract.ba.model.po.PairInfoPo
import com.bkt.contract.ba.service.ExportService
import com.bkt.contract.ba.service.PairService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import retrofit2.CacheType
import java.lang.RuntimeException

/**
 * @Description: BA 客户端
 * @Author: XGod
 * @CreateDate: 2020/12/3 20:47
 */
class BaClient private constructor() {
    private val pairService by lazy {
        PairServiceImpl();
    }

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
        if (clazz is PairService) {
            return pairService as T;
        }
        return pairService as T;
    }

    internal class PairServiceImpl : PairService {

    }
}