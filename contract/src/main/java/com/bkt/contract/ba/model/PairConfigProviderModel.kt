package com.bkt.contract.ba.model

import androidx.annotation.Nullable
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.bkt.contract.ba.service.PairService
import io.reactivex.annotations.CheckReturnValue

/**
 * @Description: 提供配置属性获取
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/16 17:39
 */
interface PairConfigProviderModel {
    /**
     * Symbol字段提供
     */
    fun provideSymbol(): String?;

    @CheckReturnValue


    @Nullable
    fun getPairConfig(): PairConfigDto? {
        try {
            return PairService.INSTANCE.getPairConfig(provideSymbol());
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null;
    }
}