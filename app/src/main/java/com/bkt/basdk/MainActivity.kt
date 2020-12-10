package com.bkt.basdk

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.service.DepthService
import com.bkt.contract.ba.service.PairService
import com.xxf.arch.XXF

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val view: View = findViewById(R.id.test);
        view.setOnClickListener {
            BaClient.instance.getService(DepthService::class.java).getDepth("BTCUSDT")
                    .doOnError{
                        XXF.getLogger().d("============>depth err:" + it);
                    }
                    .`as`(XXF.bindLifecycle(this))
                    .subscribe{
                        XXF.getLogger().d("============>depth:" + it);
                    }
        }
   /*     BaClient.instance.getService(PairService::class.java).getPairs(ContractType.USDT)
                .`as`(XXF.bindLifecycle(this))
                .subscribe {
                    XXF.getLogger().d("============>yes:" + it.size);
                }

        BaClient.instance.getService(PairService::class.java).getPairs("BTCUSDT")
                .`as`(XXF.bindLifecycle(this))
                .subscribe {
                    XXF.getLogger().d("============>yes2:" + it);
                }*/


        /*      XXF.getApiService(UsdtContractApiService::class.java)
                      .testApi()
                      .subscribe();*/


    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        BaClient.instance.getService(PairService::class.java)
                .subPairs()
                .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                .subscribe {
                    XXF.getLogger().d("==============>it:" + it.size)
                };
        BaClient.instance.getService(PairService::class.java)
                .subPairs(ContractType.USDT)
                .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                .subscribe {
                    XXF.getLogger().d("==============>it2:" + it.size)
                };

        BaClient.instance.getService(PairService::class.java)
                .subPairs("BTCUSDT")
                .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                .subscribe {
                    XXF.getLogger().d("==============>it3:" + it.size)
                };
    }


}