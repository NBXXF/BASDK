package com.bkt.contract.ba.service

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.OrderInfoDto
import com.bkt.contract.ba.model.dto.OrderRequestDto
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.MapTypeToken
import com.xxf.arch.json.datastructure.ListOrSingle
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import retrofit2.http.GET
import retrofit2.http.Query

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

    /**
     * 查询当前挂单 (USER_DATA)
     * @param orderId  系统订单号
     * @param origClientOrderId  用户自定义的订单号
     * @param recvWindow
     * @param timestamp
     */
    fun getOpenOrder(symbol: String, orderId: String?, origClientOrderId: String?, recvWindow: Long?, timestamp: Long): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getOpenOrder(symbol, orderId, origClientOrderId, recvWindow, timestamp);
                    }
                });
    }

    /**
     * 查看当前全部挂单 (USER_DATA)
     *
     *@param symbol
     */
    fun getOpenOrders(symbol: String, recvWindow: Long?, timestamp: Long): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getOpenOrders(symbol, recvWindow, timestamp);
                    }
                });
    }

    /**
     * 查看当前全部挂单 (USER_DATA)
     * 最多返回 40条  注意不是完全返回所有数据
     */
    fun getOpenOrders(type: ContractType, recvWindow: Long?, timestamp: Long): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.initializer?.getApiService(type)!!.getOpenOrders(null, recvWindow, timestamp);
    }

    /**
     * 查询所有订单(包括历史订单) (USER_DATA)
     *
     * 请注意，如果订单满足如下条件，不会被查询到：
     * 订单的最终状态为 CANCELED 或者 EXPIRED, 并且
     * 订单没有任何的成交记录, 并且
     * 订单生成时间 + 30天 < 当前时间
     *
     * @param orderId 只返回此orderID及之后的订单，缺省返回最近的订单
     * @param limit  默认值:500 最大值:1000
     * @param startTime
     * @param endTime
     * @param lim
     */
    fun getAllOrders(
            symbol: String,
            orderId: String?,
            startTime: Long?,
            endTime: Long?,
            limit: Int?,
            recvWindow: Long?,
            timestamp: Long
    ): Observable<ListOrSingle<OrderInfoDto>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<OrderInfoDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<OrderInfoDto>> {
                        return t.getAllOrders(symbol, orderId, startTime, endTime, limit, recvWindow, timestamp);
                    }
                });
    }
}