package com.bkt.contract.ba.model.dto

import com.bkt.contract.ba.common.jsontypeadapter.BoolToStringTypeAdapter
import com.bkt.contract.ba.enums.*
import com.google.gson.annotations.JsonAdapter

/**
 * @Description: 下单请求模型
 * @Author: XGod
 * @CreateDate: 2020/12/14 14:03
 *
 * 接口参考:https://binance-docs.github.io/apidocs/testnet/cn/#trade-2
 */
class OrderRequestDto {

    companion object {
        //TODO 构建工厂方法 简化应用层传递参数
    }

    /**
     * 交易对
     */
    val symbol: String;

    /**
     * 买卖方向 SELL, BUY
     */
    val side: Side;

    /**
     * 持仓方向，单向持仓模式下非必填，默认且仅可填BOTH;在双向持仓模式下必填,且仅可选择 LONG 或 SHORT
     */
    val positionSide: PositionDirection;

    /**
     * 订单类型 LIMIT, MARKET, STOP, TAKE_PROFIT, STOP_MARKET, TAKE_PROFIT_MARKET, TRAILING_STOP_MARKET
     */
    val type: OrderType;

    /**
     * true, false; 非双开模式下默认false；双开模式下不接受此参数； 使用closePosition不支持此参数。
     */
    @JsonAdapter(BoolToStringTypeAdapter::class)
    var reduceOnly: Boolean? = null;

    /**
     * 下单数量,使用closePosition不支持此参数。
     */
    var quantity: String? = null;

    /**
     * 委托价格
     */
    var price: String? = null;

    /**
     * 用户自定义的订单号，不可以重复出现在挂单中。如空缺系统会自动赋值
     */
    var newClientOrderId: String? = null;

    /**
     * 触发价, 仅 STOP, STOP_MARKET, TAKE_PROFIT, TAKE_PROFIT_MARKET 需要此参数
     */
    var stopPrice: String? = null;

    /**
     * true, false；触发后全部平仓，仅支持STOP_MARKET和TAKE_PROFIT_MARKET；不与quantity合用；自带只平仓效果，不与reduceOnly 合用
     */
    @JsonAdapter(BoolToStringTypeAdapter::class)
    var closePosition: Boolean? = null;

    /**
     * 追踪止损激活价格，仅TRAILING_STOP_MARKET 需要此参数, 默认为下单当前市场价格(支持不同workingType)
     */
    var activationPrice: String? = null;

    /**
     * 追踪止损回调比例，可取值范围[0.1, 5],其中 1代表1% ,仅TRAILING_STOP_MARKET 需要此参数
     */
    var callbackRate: String? = null;

    /**
     * 有效方法
     */
    var timeInForce: TimeInForce? = null;


    /**
     * 	stopPrice 触发类型: MARK_PRICE(标记价格), CONTRACT_PRICE(合约最新价). 默认 CONTRACT_PRICE
     */
    var workingType: WorkingType? = null;

    /**
     * 条件单触发保护："true","false", 默认"false". 仅 STOP, STOP_MARKET, TAKE_PROFIT, TAKE_PROFIT_MARKET 需要此参数
     */
    @JsonAdapter(BoolToStringTypeAdapter::class)
    var priceProtect: Boolean? = null;

    /**
     * "ACK", "RESULT", 默认 "ACK"
     */
    var newOrderRespType: NewOrderRespType? = null;

    var recvWindow: Long? = null;

    val timestamp: Long;

    constructor(symbol: String, side: Side, positionSide: PositionDirection, type: OrderType, timestamp: Long) {
        this.symbol = symbol
        this.side = side
        this.positionSide = positionSide
        this.type = type
        this.timestamp = timestamp
    }
}