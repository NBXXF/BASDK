package com.bkt.contract.ba.service

import com.bkt.contract.ba.common.AdlQuantileListToMapFunction
import com.bkt.contract.ba.common.HttpDataFunction
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.enums.PositionDirection
import com.bkt.contract.ba.model.CodeDescModel
import com.bkt.contract.ba.model.dto.AdlQuantileDto
import com.bkt.contract.ba.model.dto.ServerTimeDto
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.datastructure.ListOrSingle
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.arch.json.typeadapter.format.impl.number.Number_percent_auto_2_2_DOWN_Signed_FormatTypeAdapter
import com.xxf.arch.utils.NumberUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * @Description: 公共服务
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/12 16:37
 */
interface CommonService : ExportService {

    companion object {
        internal val INSTANCE: CommonService by lazy {
            object : CommonService {
            }
        }
    }

    /**
     * 计算涨跌额
     */
    fun getRiseFallAmount(closePrice: BigDecimal, openPrice: BigDecimal): BigDecimal {
        return NumberUtils.subtract(closePrice, openPrice);
    }

    /**
     * 计算涨跌额 格式化好的对象
     */
    fun getRiseFallAmountFormatObject(closePrice: BigDecimal, openPrice: BigDecimal): NumberFormatObject {
        val riseFallAmount = getRiseFallAmount(closePrice, openPrice);
        return NumberFormatObject(riseFallAmount, riseFallAmount.toPlainString());
    }

    /**
     * 计算涨跌幅
     */
    fun getRiseFallRange(closePrice: BigDecimal, openPrice: BigDecimal): BigDecimal {
        return NumberUtils.divide(NumberUtils.subtract(closePrice, openPrice), openPrice, max(closePrice.scale(), openPrice.scale()));
    }

    /**
     * 计算涨跌幅 格式化好的对象
     */
    fun getRiseFallRangeFormatObject(closePrice: BigDecimal, openPrice: BigDecimal): NumberFormatObject {
        val riseFallRange = getRiseFallRange(closePrice, openPrice);
        return NumberFormatObject(riseFallRange, Number_percent_auto_2_2_DOWN_Signed_FormatTypeAdapter().format(riseFallRange));
    }


    /**
     * 计算仓位价值
     */
    fun calculatePositionValue(symbol: String, positionSize: Number, marketPrice: Number): NumberFormatObject? {
        try {
            val pairConfig = PairService.INSTANCE.getPairConfig(symbol);
            var positionValueDecimal: BigDecimal? = null;
            if (pairConfig?.contractClassifyType == ContractType.USDT) {
                positionValueDecimal = NumberUtils.multiply(positionSize, marketPrice);
            } else {
                positionValueDecimal = NumberUtils.divide(NumberUtils.multiply(positionSize, pairConfig?.contractSize), marketPrice);
            }
            return NumberFormatObject(positionValueDecimal, NumberUtils.formatRoundDown(positionSize, 0, pairConfig?.pricePrecision
                    ?: 2));
        } catch (e: Throwable) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算预估强平价
     * WB:  isolatedMargin - unRealizedProfit + input(输入的保证金)
    L :  positionAmt绝对值
    //杠杆分层标准
    MMR_L: maintMarginRatio
    cum_L: cum
    CM： contractSize（symbol里面拿）
    EP_L: entryPrice



    if env == .usdt {
    if positonModel.positionSide == .long {
    let tmp1 = WB + cum_L - L * EP_L
    let tmp2 = L * MMR_L - L
    LP = tmp1 / tmp2
    } else {
    let tmp1 = WB + cum_L + L * EP_L
    let tmp2 = L * MMR_L + L
    LP = tmp1 / tmp2
    }

    } else {
    if positonModel.positionSide == .long {
    let tmp1 = L * MMR_L + L
    let tmp2 = (WB + cum_L) / CM + L / EP_L
    LP = tmp1 / tmp2
    } else {
    let tmp1 = L * MMR_L - L
    let tmp2 = (WB + cum_L) / CM - L / EP_L
    LP = tmp1 / tmp2
    }
    }
     */
    fun calculatePredictClosePrice(symbol: String,
                                   positionSide: PositionDirection,
                                   inputMargin: BigDecimal,
                                   isolatedWallet: BigDecimal,
                                   positionAmt: BigDecimal,
                                   entryPrice: BigDecimal,
                                   maintMarginRatio: BigDecimal,
                                   cum: BigDecimal): NumberFormatObject? {
        try {
            val pairConfig = PairService.INSTANCE.getPairConfig(symbol);
            val WB = isolatedWallet + inputMargin;
            val L = positionAmt.abs();
            val MMR_L = maintMarginRatio;
            val cum_L = cum;
            val CM = BigDecimal(pairConfig?.contractSize!!);
            val EP_L = entryPrice;
            var LP: BigDecimal = BigDecimal(0);

            if (pairConfig?.contractClassifyType == ContractType.USDT) {
                if (positionSide == PositionDirection.LONG) {
                    val tmp1 = WB + cum_L - L * EP_L
                    val tmp2 = L * MMR_L - L
                    LP = tmp1 / tmp2
                } else {
                    val tmp1 = WB + cum_L + L * EP_L
                    val tmp2 = L * MMR_L + L
                    LP = tmp1 / tmp2
                }
            } else {
                if (positionSide == PositionDirection.LONG) {
                    val tmp1 = L * MMR_L + L
                    val tmp2 = (WB + cum_L) / CM + L / EP_L
                    LP = tmp1 / tmp2
                } else {
                    val tmp1 = L * MMR_L - L
                    val tmp2 = (WB + cum_L) / CM - L / EP_L
                    LP = tmp1 / tmp2
                }
            }
            return NumberFormatObject(LP, NumberUtils.formatRoundDown(LP, 0, pairConfig.pricePrecision));
        } catch (e: Throwable) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算回报率==收益率
     * 未实现盈亏 = 持仓数量 * 开仓方向 * (标记价格 - 开仓价格)
    回报率% = 未实现盈亏 USDT / 起始保证金 = ( ( 标记价格 - 开仓价格 ) * 开仓方向 * 持仓数量 ) / （持仓数量* 合约乘数 * 标记价格* 起始保证金率）
     *起始保证金率 = 1 / 杠杆倍数
    开仓方向: 为买单为 1；卖单为 -1
    币本位:
    未实现盈亏 = 持仓数量 * 合约乘数 * 开仓方向 * (1 / 开仓价格 - 1 / 标记价格)
    回报率%= 未实现盈亏 * 标记价格 / [绝对值(持仓数量) * 合约乘数 * 起始保证金率]
     */
    fun calculateEarningRate(symbol: String,
                             positionSide: PositionDirection,
                             entryPrice: BigDecimal,
                             markPrice: BigDecimal,
                             positionAmt: BigDecimal,
                             leverage: Int,
                             unRealizedProfit: BigDecimal): NumberFormatObject? {
        try {
            val pairConfig = PairService.INSTANCE.getPairConfig(symbol);
            var earningRateDecimal: BigDecimal? = null;
            if (pairConfig?.contractClassifyType == ContractType.USDT) {
                earningRateDecimal = ((markPrice - entryPrice) * positionAmt * (if (positionSide == PositionDirection.LONG) 1.toBigDecimal() else -1.toBigDecimal())) / (positionAmt * markPrice * (BigDecimal(1.0 / leverage)))
            } else {
                earningRateDecimal = (unRealizedProfit * markPrice) / (positionAmt.abs() * pairConfig?.contractSize!!.toBigDecimal() * (1.0 / leverage).toBigDecimal());
            }
            return NumberFormatObject(earningRateDecimal, Number_percent_auto_2_2_DOWN_Signed_FormatTypeAdapter().format(earningRateDecimal));
        } catch (e: Throwable) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * symbol 转pair
     */
    fun convertPair(symbol: String?): String? {
        if (symbol != null) {
            if (symbol.contains("-")) {
                return symbol.split("-").get(0);
            } else if (symbol.contains("/")) {
                return symbol.split("/").get(0);
            }
        }
        return symbol;
    }

    /**
     * 获取服务器时间戳
     */
    fun getServerTime(): Observable<Long> {
        return BaClient.instance.initializer!!.getApiService(ContractType.USDT)
                .getServerTime()
                .map(HttpDataFunction())
                .onErrorResumeNext(object : Function<Throwable, ObservableSource<ServerTimeDto>> {
                    override fun apply(t: Throwable): ObservableSource<ServerTimeDto> {
                        return BaClient.instance.initializer!!.getApiService(ContractType.USD).getServerTime()
                                .map(HttpDataFunction());
                    }
                })
                .map { it.serverTime }
    }

    /**
     * 获取所有合约乘数[symbol:合约乘数]
     * 注意！！ 只有USD 币本位有合约乘数一说  USDT取到的是0
     */
    fun getContractMultipliers(): Map<String, Int> {
        return PairService.INSTANCE.getPairConfigs()
                .mapValues {
                    it.value.contractSize;
                }
    }


    private fun getFromAssets(fileName: String?): String? {
        try {
            val inputReader = InputStreamReader(XXF.getApplication().resources.assets.open(fileName!!))
            val bufReader = BufferedReader(inputReader)
            var line: String? = ""
            var Result: String? = ""
            while (bufReader.readLine().also { line = it } != null) Result += line
            return Result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取ba 状态码 对应的描述
     * 为防止后端只给了code msg=null
     * 为了国际化
     */
    fun getHttpCodeDesc(isChinaLocal: Boolean): Observable<Map<Int, String>> {
        return Observable
                .fromCallable(object : Callable<String?> {
                    override fun call(): String? {
                        return getFromAssets(if (isChinaLocal) "ba_code_list.json" else "ba_code_list.json_en");
                    }
                }).map(object : Function<String?, Map<Int, String>> {
                    override fun apply(t: String): Map<Int, String> {
                        val toBeanList = JsonUtils.toBeanList(JSONArray(t).toString(), CodeDescModel::class.java);
                        val map: MutableMap<Int, String> = mutableMapOf();
                        toBeanList.forEach {
                            map.put(it.code, it.desc);
                        }
                        return map;
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 持仓ADL队列估算
     * [key=symbol,value]
     */
    fun getAdlQuantile(symbol: String,
                       recvWindow: Long?)
            : Observable<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ListOrSingle<AdlQuantileDto>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ListOrSingle<AdlQuantileDto>> {
                        return t.getAdlQuantile(symbol, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction());
                    }
                }).map(AdlQuantileListToMapFunction());
    }

    /**
     * 按类型获取持仓ADL队列估算
     * [key=symbol,value]
     */
    fun getAdlQuantileByType(type: ContractType, recvWindow: Long?)
            : Observable<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>> {
        return BaClient.instance.initializer?.getApiService(type)!!
                .getAdlQuantile(null, recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction())
                .map(AdlQuantileListToMapFunction());
    }

    /**
     * 订阅持仓ADL队列估算
     * [key=symbol,value]
     */
    fun subAdlQuantile(symbol: String,
                       recvWindow: Long?): Observable<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>> {
        /**
         * 每30秒更新数据
         */
        return Observable.interval(1, 30 + 1, TimeUnit.SECONDS)
                .flatMap(object : Function<Long, ObservableSource<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>>> {
                    override fun apply(t: Long): ObservableSource<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>> {
                        return getAdlQuantile(symbol, recvWindow)
                                .onErrorResumeNext(Observable.empty());
                    }
                });
    }

    /**
     * 按类型订阅持仓ADL队列估算
     * [key=symbol,value]
     */
    fun subAdlQuantileByType(type: ContractType,
                             recvWindow: Long?): Observable<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>> {
        /**
         * 每30秒更新数据
         */
        return Observable.interval(1, 30 + 1, TimeUnit.SECONDS)
                .flatMap(object : Function<Long, ObservableSource<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>>> {
                    override fun apply(t: Long): ObservableSource<LinkedHashMap<String, AdlQuantileDto.AdlQuantileItem>> {
                        return getAdlQuantileByType(type, recvWindow)
                                .onErrorResumeNext(Observable.empty());
                    }
                });
    }

}