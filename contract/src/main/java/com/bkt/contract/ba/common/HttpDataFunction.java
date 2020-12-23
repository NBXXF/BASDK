package com.bkt.contract.ba.common;

import com.bkt.contract.ba.model.dto.IBktBaseResDto;
import com.xxf.arch.http.ResponseException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @Description: TODO @XGode
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/21 11:34
 */
public class HttpDataFunction<T> implements Function<IBktBaseResDto<T>, T> {
    private T defaultValue;

    public HttpDataFunction() {
    }

    /**
     * 先来个简单方案
     *
     * @param defaultValue 默认值,适合服务器返回为null
     */
    public HttpDataFunction(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public T apply(@NonNull IBktBaseResDto<T> tIBktResponseDTO) throws Exception {
        if (!tIBktResponseDTO.isSuccess()) {
            throw new ResponseException(tIBktResponseDTO.getStatCode(), tIBktResponseDTO.getErrorMsg(), tIBktResponseDTO);
        }
        T data = tIBktResponseDTO.getData();
        if (data == null
                && defaultValue != null) {
            return defaultValue;
        } else if (data == null) {
            throw new ResponseException(-1, "data field is null!", tIBktResponseDTO);
        }
        return data;
    }
}