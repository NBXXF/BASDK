package com.bkt.contract.ba.common

import com.bkt.contract.ba.model.dto.AdlQuantileDto
import com.bkt.contract.ba.model.dto.AdlQuantileDto.AdlQuantileItem
import com.xxf.arch.json.datastructure.ListOrSingle
import io.reactivex.functions.Function
import kotlin.collections.LinkedHashMap

/**
 * @Description: 转换AdlQuantileItem 为[key=symbol,value]
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/16 18:42
 */
class AdlQuantileListToMapFunction : Function<ListOrSingle<AdlQuantileDto>, LinkedHashMap<String, AdlQuantileItem>> {
    override fun apply(t: ListOrSingle<AdlQuantileDto>): LinkedHashMap<String, AdlQuantileItem> {
        val map: LinkedHashMap<String, AdlQuantileItem> = LinkedHashMap();
        t.forEach {
            if (it.symbol != null && it.adlQuantile != null) {
                map.put(it.symbol!!, it.adlQuantile!!);
            }
        }
        return map;
    }


}