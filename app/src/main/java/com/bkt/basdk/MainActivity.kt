package com.bkt.basdk

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.service.PairService
import com.xxf.arch.XXF

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaClient.instance.getService(PairService::class.java).getPairs(ContractType.USDT)
                .doOnError {
                    XXF.getLogger().d("============>yes no:" + it);
                }
                .subscribe {
                    XXF.getLogger().d("============>yes:" + it.size);
                }

        BaClient.instance.getService(PairService::class.java).getPairs("BTCUSDT")
                .doOnError {
                    XXF.getLogger().d("============>yes no2:" + it);
                }
                .subscribe {
                    XXF.getLogger().d("============>yes2:" + it);
                }


        /*      XXF.getApiService(UsdtContractApiService::class.java)
                      .testApi()
                      .subscribe();*/
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()

        BaClient.instance.getService(PairService::class.java)
                .subPairs()
                .subscribe {
                    it.get(0).copy();
                    XXF.getLogger().d("==============>it:" + it)
                };
    }


}