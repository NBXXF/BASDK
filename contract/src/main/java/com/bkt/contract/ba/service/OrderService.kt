package com.bkt.contract.ba.service

import com.bkt.contract.ba.model.dto.OrderInfoDto
import com.bkt.contract.ba.model.dto.OrderRequestDto
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.MapTypeToken
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * @Description: 订单下单service
 * @Author: XGod
 * @CreateDate: 2020/12/14 11:27
 */
interface OrderService : ExportService {
    companion object {
        internal val INSTANCE: OrderService by lazy {
            object : OrderService {
            }
        }
    }

    /**
     * 下单
     * @param order
     */
    fun createOrder(order: OrderRequestDto): Observable<OrderInfoDto> {
        return BaClient.instance.getApiService(order.symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<OrderInfoDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<OrderInfoDto> {
                        return t.createOrder(JsonUtils.toMap(JsonUtils.toJsonString(order), MapTypeToken<String, Any>()));
                    }
                });
    }
}