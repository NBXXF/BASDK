package com.bkt.contract.ba.common.jsontypeadapter

import com.google.gson.stream.JsonWriter
import com.xxf.arch.json.typeadapter.bool.BooleanTypeAdapter

/**
 * @Description: bool 序列化string"true" "false"
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/14 14:33
 */
class BoolToStringTypeAdapter : BooleanTypeAdapter() {
    override fun write(out: JsonWriter?, value: Boolean?) {
        if (value == null) {
            out!!.nullValue()
        } else {
            out!!.value(value.toString())
        }
    }
}