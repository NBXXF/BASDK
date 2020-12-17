package com.bkt.contract.ba.model

/**
 * @Description: ba http 业务code对应描述  https://binance-docs.github.io/apidocs/testnet/cn/#10xx
 * @Author: XGod
 * @CreateDate: 2020/12/17 10:28
 */
class CodeDescModel(var code: Int, var desc: String) {
    override fun toString(): String {
        return "CodeDescModel{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}'
    }

}