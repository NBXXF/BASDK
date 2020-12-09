package com.bkt.basdk.api;

import com.xxf.arch.XXF;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Response;

/**
 * @Description: java类作用描述
 * @Author: XGod
 * @CreateDate: 2020/6/16 17:37
 */
public class MyLoggerInterceptor2 extends com.xxf.arch.http.interceptor.HttpLoggingInterceptor {
    public MyLoggerInterceptor2() {
        super(new Logger() {

            @Override
            public void log(String message) {
                XXF.getLogger().d("http:" + message);
            }
        });
        setLevel(Level.BODY);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        XXF.getLogger().d("=============>http url:" + chain.request().url());
        return super.intercept(chain);
    }
}
