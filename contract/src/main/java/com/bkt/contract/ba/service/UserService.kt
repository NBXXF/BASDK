package com.bkt.contract.ba.service

import com.bkt.contract.ba.common.HttpDataFunction
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.UserConfigModel
import com.bkt.contract.ba.model.dto.*
import com.bkt.contract.ba.model.event.AccountUpdateEvent
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.google.gson.JsonObject
import com.xxf.arch.json.datastructure.ListOrSingle
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import retrofit2.CacheType
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * @Description: 用户service
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/16 11:37
 */
interface UserService : ExportService {
    companion object {
        internal val INSTANCE: UserService by lazy {
            object : UserService {
            }
        }
    }

    /**
     * 创建一个新的user data stream,返回值为一个listenKey,即websocket订阅的stream名称。如果该帐户具有有效的listenKey,则将返回该listenKey并将其有效期延长60分钟。
     */
    fun createListenKey(type: ContractType): Observable<ListenKeyDto> {
        return BaClient.instance.initializer!!.getApiService(type)
                .createListenKey()
                .map(HttpDataFunction())
    }

    /**
     * 有效期延长至本次调用后60分钟
     */
    fun lengthenListenKey(type: ContractType): Observable<JsonObject> {
        return BaClient.instance.initializer!!.getApiService(type)
                .lengthenListenKey()
                .map(HttpDataFunction())
    }

    /**
     * 关闭listenKey 关闭某账户数据流
     */
    fun deleteListenKey(type: ContractType): Observable<JsonObject> {
        return BaClient.instance.initializer!!.getApiService(type)
                .deleteListenKey()
                .map(HttpDataFunction());
    }

    /**
     * 获取杠杆倍数配置
     * 从小到大(最大杠杆倍数)
     */
    fun getLeverageBrackets(symbol: String): Observable<List<LeverageBracketDto.BracketsBean>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<List<LeverageBracketDto.BracketsBean>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<List<LeverageBracketDto.BracketsBean>> {
                        return t.getLeverageBracket(symbol, CommonService.INSTANCE.convertPair(symbol), null, System.currentTimeMillis())
                                .map(HttpDataFunction())
                                .map { it.get(0).brackets?.sortedBy { it.initialLeverage } };
                    }
                });
    }

    /**
     * 获取杠杆倍数配置
     * 特别注意[pair,value] key是pair
     * 从小到大(最大杠杆倍数)
     */
    fun getLeverageBrackets(type: ContractType): Observable<Map<String, List<LeverageBracketDto.BracketsBean>>> {
        return BaClient.instance.initializer!!.getApiService(type).getLeverageBracket(null, null, null, System.currentTimeMillis())
                .map(HttpDataFunction())
                .map(object : Function<ListOrSingle<LeverageBracketDto>, Map<String, List<LeverageBracketDto.BracketsBean>>> {
                    override fun apply(t: ListOrSingle<LeverageBracketDto>): Map<String, List<LeverageBracketDto.BracketsBean>> {
                        val map: MutableMap<String, List<LeverageBracketDto.BracketsBean>> = mutableMapOf();
                        t.forEach {
                            if (it.pair != null && it.brackets != null) {
                                map.put(it.pair!!, it.brackets.sortedBy { it.initialLeverage })
                            }
                        }
                        return map;
                    }
                });
    }

    /**
     * 获取账户余额
     * @param type
     * @param recvWindow
     */
    fun getBalanceByType(type: ContractType, recvWindow: Long?): Observable<ListOrSingle<CoinBalanceDto>> {
        return BaClient.instance.initializer!!.getApiService(type)
                .getBalance(if (type == ContractType.USDT) "v2" else "v1", recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction());
    }

    /**
     * 查询单个币种的余额
     */
    fun getBalance(symbol: String, recvWindow: Long?): Observable<CoinBalanceDto> {
        return PairService.INSTANCE
                .getPairType(symbol)
                .flatMap(object : Function<ContractType, ObservableSource<CoinBalanceDto>> {
                    override fun apply(t: ContractType): ObservableSource<CoinBalanceDto> {
                        return getBalanceToMap(t, recvWindow)
                                .flatMap(object : Function<Map<String, CoinBalanceDto>, ObservableSource<CoinBalanceDto>> {
                                    override fun apply(t: Map<String, CoinBalanceDto>): ObservableSource<CoinBalanceDto> {
                                        val pairConfig = PairService.INSTANCE.getPairConfig(symbol);
                                        var balance = t.get(pairConfig?.baseAsset);
                                        if (balance == null) {
                                            balance = t.get(pairConfig?.quoteAsset);
                                        }
                                        if (balance == null) {
                                            return Observable.empty();
                                        } else {
                                            return Observable.just(balance);
                                        }
                                    }
                                });
                    }
                })
    }

    /**
     * 获取账户余额
     * 转换成map[key=asset,value] eg. 方便业务层获取
     * @param type
     * @param recvWindow
     */
    fun getBalanceToMap(type: ContractType, recvWindow: Long?): Observable<Map<String, CoinBalanceDto>> {
        return BaClient.instance.initializer!!.getApiService(type)
                .getBalance(if (type == ContractType.USDT) "v2" else "v1", recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction())
                .map(object : Function<ListOrSingle<CoinBalanceDto>, Map<String, CoinBalanceDto>> {
                    override fun apply(t: ListOrSingle<CoinBalanceDto>): Map<String, CoinBalanceDto> {
                        val map: LinkedHashMap<String, CoinBalanceDto> = LinkedHashMap();
                        t.forEach {
                            it.asset?.let { it1 -> map.put(it1, it) };
                        }
                        return map;
                    }
                });
    }

    /**
     * 获取所有账户余额 包含USDT和USD
     * 转换成map[key=asset,value] eg. 方便业务层获取
     */
    fun getAllBalanceToMap(recvWindow: Long?): Observable<Map<String, CoinBalanceDto>> {
        return Observable.zip(
                getBalanceToMap(ContractType.USDT, recvWindow),
                getBalanceToMap(ContractType.USD, recvWindow),
                object : BiFunction<Map<String, CoinBalanceDto>, Map<String, CoinBalanceDto>, Map<String, CoinBalanceDto>> {
                    override fun apply(t1: Map<String, CoinBalanceDto>, t2: Map<String, CoinBalanceDto>): Map<String, CoinBalanceDto> {
                        val map: LinkedHashMap<String, CoinBalanceDto> = LinkedHashMap();
                        map.putAll(t1);
                        map.putAll(t2);
                        return map;
                    }
                })
    }

    /**
     * 调整杠杆
     * @param leverage 目标杠杆倍数：1 到 125 整数
     */
    fun changeLeverage(symbol: String,
                       leverage: Int,
                       recvWindow: Long?): Observable<ChangeLeverageResDto> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<ChangeLeverageResDto>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<ChangeLeverageResDto> {
                        return t.changeLeverage(symbol, leverage, recvWindow, System.currentTimeMillis())
                                .map(HttpDataFunction())
                    }
                });
    }

    /**
     * 获取账户信息
     * 对于单向持仓模式，"positions"仅会展示"BOTH"方向的持仓
     * 对于双向持仓模式，"positions"会展示所有"BOTH", "LONG", 和"SHORT"方向的持仓
     */
    fun getAccount(type: ContractType,
                   recvWindow: Long?,
                   cacheType: CacheType = CacheType.onlyRemote,
                   cacheTime: Long = TimeUnit.MINUTES.toMillis(5)
    ): Observable<AccountInfoDto> {
        return BaClient.instance
                .initializer!!.getApiService(type)
                .getAccount(if (type == ContractType.USDT) "v2" else "v1", cacheType, cacheTime, recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction());
    }

    /**
     * 从账户信息中获取资产
     * [key="asset",value]
     */
    fun getUserAssets(type: ContractType,
                      recvWindow: Long?,
                      cacheType: CacheType = CacheType.onlyRemote,
                      cacheTime: Long = TimeUnit.MINUTES.toMillis(5)): Observable<Map<String, AccountInfoDto.BalanceDetailsDto>> {
        return getAccount(type, recvWindow, cacheType, cacheTime)
                .map(object : Function<AccountInfoDto, Map<String, AccountInfoDto.BalanceDetailsDto>> {
                    override fun apply(t: AccountInfoDto): Map<String, AccountInfoDto.BalanceDetailsDto> {
                        val map: LinkedHashMap<String, AccountInfoDto.BalanceDetailsDto> = linkedMapOf();
                        t.assets?.forEach {
                            it.asset?.let { it1 -> map.put(it1, it) };
                        }
                        return map;
                    }
                })
    }

    /**
     * 获取用户配置
     * [key=symbol,value]
     */
    fun getUserConfig(type: ContractType,
                      recvWindow: Long?,
                      cacheType: CacheType = CacheType.onlyRemote,
                      cacheTime: Long = TimeUnit.MINUTES.toMillis(5)): Observable<Map<String, UserConfigModel>> {
        return getAccount(type, recvWindow, cacheType, cacheTime)
                .map(object : Function<AccountInfoDto, Map<String, UserConfigModel>> {
                    override fun apply(t: AccountInfoDto): Map<String, UserConfigModel> {
                        var configMap: MutableMap<String, UserConfigModel> = mutableMapOf();
                        t.positions?.forEach {
                            it.symbol?.let { it1 -> configMap.put(it1, UserConfigModel(it.leverage)) };
                        };
                        return configMap;
                    }
                });
    }

    /**
     * Balance和Position更新推送
     * 币安傻屌设计
     */
    fun subAccountUpdate(type: ContractType): Observable<AccountUpdateEvent.AccountChangeInfo> {
        return BaClient.instance.initializer!!.getSocketService(type)
                .subAccountUpdate();
    }

    /**
     * 获取持仓模式
     */
    fun getPositionSideDual(type: ContractType, recvWindow: Long?): Observable<PositionSideDualDto> {
        return BaClient.instance.initializer!!
                .getApiService(type)
                .getPositionSideDual(recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction());
    }


    /**
     *更改持仓模式
     * @param dualSidePosition  "true": 双向持仓模式；"false": 单向持仓模式
     */
    fun changePositionSideDual(
            type: ContractType,
            dualSidePosition: Boolean,
            recvWindow: Long?): Observable<BaResultDto> {
        return BaClient.instance.initializer!!
                .getApiService(type)
                .changePositionSideDual(dualSidePosition.toString(), recvWindow, System.currentTimeMillis())
                .map(HttpDataFunction());
    }

}