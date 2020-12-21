package com.bkt.contract.ba.service

import com.bkt.contract.ba.common.AdlQuantileListToMapFunction
import com.bkt.contract.ba.common.HttpDataFunction
import com.bkt.contract.ba.enums.ContractType
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
 * @Author: XGod
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