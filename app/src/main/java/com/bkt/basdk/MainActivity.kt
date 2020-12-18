package com.bkt.basdk

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.service.CommonService
import com.bkt.contract.ba.service.PairService
import com.bkt.contract.ba.service.PriceService
import com.xxf.arch.XXF
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view: View = findViewById(R.id.test);
        view.setOnClickListener {
            CodeDescUtil.test();

            val s: Boolean = true;
            var str: String = s.toString();
            XXF.getLogger().d("================>s:" + str)
            val s2: Boolean = true;
            XXF.getLogger().d("================>s:" + s2.toString());

            /* BaClient.instance.getService(CommonService::class.java).getServerTime()
                     .`as`(XXF.bindLifecycle(this))
                     .subscribe {
                         XXF.getLogger().d("================>serverTime:" + it);
                     }*/
            val contractMultipliers = BaClient.instance.getService(CommonService::class.java).getContractMultipliers();
            XXF.getLogger().d("============>contractMultipliers:" + contractMultipliers);
            BaClient.instance.getService(PairService::class.java).getPairs()
                    .observeOn(AndroidSchedulers.mainThread())
                    .`as`(XXF.bindLifecycle(this))
                    .subscribe {
                        XXF.getLogger().d("============>getPairs.......");
                    }

            BaClient.instance.getService(PriceService::class.java).getTickerPrice(ContractType.USDT)
                    .observeOn(AndroidSchedulers.mainThread())
                    .`as`(XXF.bindLifecycle(this))
                    .subscribe {
                        XXF.getLogger().d("============>tickerPrice:" + it);
                    }
            /*   BaClient.instance.getService(TradeService::class.java).getTrades("BTCUSDT", CacheType.firstCache, TimeUnit.MINUTES.toMillis(5))
                       .doOnError {
                           XXF.getLogger().d("============>depth err:" + it);
                       }
                       .`as`(XXF.bindLifecycle(this))
                       .subscribe {
                           XXF.getLogger().d("============>depth:" + it);
                       }*/


            /*      BaClient.instance.getService(KLineService::class.java)
                          .getKLine("BTCUSDT","1w",System.currentTimeMillis()-TimeUnit.DAYS.toMillis(1),System.currentTimeMillis(),500)
                          .`as`(XXF.bindLifecycle(this))
                          .subscribe{
                              XXF.getLogger().d("============>Kline http:"+it);
                          }*/
            /*  BaClient.instance.getService(PriceService::class.java).getPremiumIndex("BTCUSDT", null)
                      .`as`(XXF.bindLifecycle(this))
                      .subscribe {
                          XXF.getLogger().d("============>yes2:" + it);
                      }*/

            /*   BaClient.instance.getService(PriceService::class.java).getPremiumIndex("BTCUSDT", null)
                       .`as`(XXF.bindLifecycle(this))
                       .subscribe {
                           XXF.getLogger().d("============>IndexPrice:" + it);
                       }*/

            /*   BaClient.instance.getService(UserService::class.java).getLeverageBrackets(ContractType.USDT)
                       .`as`(XXF.bindLifecycle(this))
                       .subscribe {
                           XXF.getLogger().d("============>LeverageBrackets:" + it);
                       }*/

            BaClient.instance.getService(CommonService::class.java).getAdlQuantileByType(ContractType.USDT, null)
                    .`as`(XXF.bindLifecycle(this))
                    .subscribe {
                        XXF.getLogger().d("============>getAdlQuantileByType:" + it);
                    }

            BaClient.instance.getService(CommonService::class.java).getHttpCodeDesc(true)
                    .`as`(XXF.bindLifecycle(this))
                    .subscribe {
                        XXF.getLogger().d("=============>httpCode:" + it);
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
        /*    BaClient.instance.getService(PairService::class.java)
                    .subPairs(ContractType.USD)
                    .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                    .subscribe {
                        XXF.getLogger().d("==============>it:" + it)
                    };*/
        /*      BaClient.instance.getService(PairService::class.java)
                      .subPairs()
                      .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                      .subscribe {
                          XXF.getLogger().d("==============>it:" + it)
                      };*/
        /*  BaClient.instance.getService(PairService::class.java)
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
                 };*/

        /*         BaClient.instance.getService(DepthService::class.java)
                         .subDepth("BTCUSDT")
                         .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                         .subscribe {
                             XXF.getLogger().d("==============>depth socket:" + it.asks.size)
                         };*/

        /*    BaClient.instance.getService(PairService::class.java)
                    .subPairs("BTCUSDT", applyDispose = false)
                    .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                    .subscribe {
                        XXF.getLogger().d("==============>it3:" + it)
                    };*/
        /*      BaClient.instance.getService(TradeService::class.java)
                      .subTrades("BTCUSDT")
                      .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                      .subscribe {
                          XXF.getLogger().d("==============>trade socket:" + it);
                      }*/
        /*
                  BehaviorSubject.create<Long>()
                          .doOnDispose {
                              XXF.getLogger().d("==============>yes doOnDispose");
                          }
                          .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                          .subscribe();*/


        /*      BehaviorSubject.create<Long>().switchIfEmpty(Observable.just(1))
                      .`as`(XXF.bindLifecycle(this))
                      .subscribe {
                          XXF.getLogger().d("==============>yes:" + it);
                      }*/

        BaClient.instance.getService(CommonService::class.java)
                .subAdlQuantileByType(ContractType.USDT, null)
                .`as`(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                .subscribe {
                    XXF.getLogger().d("==============>subAdlQuantileByType:" + it);
                }


        Cat(Person()).say();
    }


    interface Animal {
        fun say();
    }

    class Person : Animal {
        override fun say() {
            System.out.println("==========>is person");
        }
    }

    class Cat(p: Animal) : Animal by p {
        override fun say() {
            System.out.println("============>is cat")
        }
    }


}