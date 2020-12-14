package com.bkt.contract.ba.common.jsontypeadapter

import com.google.gson.stream.JsonWriter
import com.xxf.arch.json.typeadapter.bool.BooleanTypeAdapter

/**
 * @Description: bool 序列化string"true" "false"
 * @Author: XGod
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