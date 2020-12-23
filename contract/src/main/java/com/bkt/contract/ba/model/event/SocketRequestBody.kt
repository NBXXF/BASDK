package com.bkt.contract.ba.model.event

import com.bkt.contract.ba.enums.Method

/**
 * @Description: 请求体
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/10 10:50
 */
class SocketRequestBody {
    companion object {

        fun subscribeBody(params: List<String>): SocketRequestBody {
            return SocketRequestBody(Method.SUBSCRIBE, params);
        }

        fun unSubscribeBody(params: List<String>): SocketRequestBody {
            return SocketRequestBody(Method.UNSUBSCRIBE, params);
        }
    }

    val method: Method;
    val params: List<String>;
    val id: Long;

    private constructor(method: Method, params: List<String>, id: Long) {
        this.method = method
        this.params = params
        this.id = id
    }

    private constructor(method: Method, params: List<String>) {
        this.method = method
        this.params = params
        this.id = System.currentTimeMillis();
    }

    override fun toString(): String {
        return "SocketRequestBody(method=$method, params=$params, id=$id)"
    }


}