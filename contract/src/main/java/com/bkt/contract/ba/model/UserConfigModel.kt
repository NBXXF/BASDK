package com.bkt.contract.ba.model

import java.io.Serializable

/**
 * @Description: 用户相关配置,扩展
 * @Author: XGod
 * @CreateDate: 2020/12/23 17:32
 */
class UserConfigModel : Serializable {
    val leverage: Int;

    constructor(leverage: Int) {
        this.leverage = leverage
    }

    override fun toString(): String {
        return "UserConfigModel(leverage=$leverage)"
    }
}