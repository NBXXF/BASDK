package com.bkt.basdk

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bkt.basdk.api.UsdtContractApiService
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.service.PairService
import com.xxf.arch.XXF
import okhttp3.HttpUrl

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaClient.instance.getService(PairService::class.java).getPairs(ContractType.USDT)
                .doOnError{
                    XXF.getLogger().d("============>yes no:"+it);
                }
                .subscribe{
                    XXF.getLogger().d("============>yes:"+it.size);
                }

 /*      XXF.getApiService(UsdtContractApiService::class.java)
               .testApi()
               .subscribe();*/
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
    }


}