package com.bkt.contract.ba.service

import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.model.dto.LeverageBracketDto
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.sdk.ContractProxyApiService
import com.xxf.arch.json.datastructure.ListOrSingle
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import org.jetbrains.annotations.Contract

/**
 * @Description: 用户service
 * @Author: XGod
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
     * 获取杠杆倍数配置
     * 从小到大(最大杠杆倍数)
     */
    fun getLeverageBrackets(symbol: String): Observable<List<LeverageBracketDto.BracketsBean>> {
        return BaClient.instance.getApiService(symbol)
                .flatMap(object : Function<ContractProxyApiService, ObservableSource<List<LeverageBracketDto.BracketsBean>>> {
                    override fun apply(t: ContractProxyApiService): ObservableSource<List<LeverageBracketDto.BracketsBean>> {
                        return t.getLeverageBracket(symbol, CommonService.INSTANCE.convertPair(symbol), null, System.currentTimeMillis())
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
}