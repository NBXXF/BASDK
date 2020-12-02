package com.bkt.basdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bkt.basdk.api.ContractApiService;
import com.google.gson.JsonObject;
import com.xxf.arch.XXF;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XXF.getApiService(ContractApiService.class)
                .testApi()
                .compose(XXF.<JsonObject>bindToErrorNotice())
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(JsonObject jsonObject) throws Exception {
                        XXF.getLogger().d("================>yes:" + jsonObject);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        XXF.getLogger().d("================>no:" + throwable);
                    }
                });
    }
}