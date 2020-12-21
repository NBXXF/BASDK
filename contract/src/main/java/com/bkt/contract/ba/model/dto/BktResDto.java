package com.bkt.contract.ba.model.dto;

/**
 * @author XGod
 * @describe 网络基础模型实现类
 * @date 2020/5/13 17:00
 * <p>
 * you can ResponseDTO<String> or ResponseDTO<List<String>！
 * you can ResponseDTO<String> or ResponseDTO<List<String>！
 * you can ResponseDTO<String> or ResponseDTO<List<String>！
 */
public class BktResDto<T> implements IBktBaseResDto<T> {
    int CODE_SUCCESS = 0;//成功
    private transient String url;
    public int status;
    public String error;
    public String path;
    public int code;
    public String msg;
    public T data;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public int getStatCode() {
        return code;
    }

    @Override
    public boolean isSuccess() {
        return CODE_SUCCESS == this.code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }

    @Override
    public T getData() {
        return data;
    }
}
