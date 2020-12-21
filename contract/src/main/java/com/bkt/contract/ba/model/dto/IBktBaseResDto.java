package com.bkt.contract.ba.model.dto;

import java.io.Serializable;

/**
 * @author XGod
 * @describe 网络基础模型
 * @date 2020/5/13 16:46
 */
public interface IBktBaseResDto<T> extends Serializable {
    /**
     * 设置url
     *
     * @param url
     * @return
     */
    void setUrl(String url);

    /**
     * 获取访问的url
     * @return
     */
    String getUrl();

    /**
     * 网络状态码
     *
     * @return
     */
    int getStatCode();

    /**
     * e
     * 是否成功
     *
     * @return
     */
    boolean isSuccess();

    /**
     * 错误信息
     *
     * @return
     */
    String getErrorMsg();

    /**
     * 获取数据
     *
     * @return
     */
    T getData();
}
