package com.bkt.basdk

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bkt.basdk.api.ContractApiService
import com.bkt.basdk.model.HttpStatus
import com.bkt.contract.ba.model.dto.OderFilterDto
import com.bkt.contract.ba.model.dto.PairConfigDto
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val sub: Subject<Any> = PublishSubject.create<Any>().toSerialized();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        XXF.getApiService(ContractApiService::class.java)
                .testApi2()
                .compose(XXF.bindToErrorNotice<HttpStatus>())
                .subscribe({ httpStatus ->
                    XXF.getLogger().d("================>yes:$httpStatus")
                }) { throwable -> XXF.getLogger().d("================>no:$throwable") }

        Observable.interval(2, TimeUnit.SECONDS)
                .subscribe {
                    System.out.println("==================>next:" + Thread.currentThread().name)
                    sub.onNext(it);
                }

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe {
                    System.out.println("==================>next2:" + Thread.currentThread().name)
                    sub.onNext("_o:" + it);
                }
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        try {
            System.out.println("==============>1:" + JsonUtils.toBean("{\n" +
                    "  \"filterType\": \"PRICE_FILTER\",\n" +
                    "  \"maxPrice\": \"300\",\n" +
                    "  \"minPrice\": \"0.0001\",\n" +
                    "  \"tickSize\": \"0.0001\"\n" +
                    "}", OderFilterDto::class.java));
            System.out.println("==============>2:" + JsonUtils.toBean("{\n" +
                    "  \"filters\": [\n" +
                    "    {\n" +
                    "      \"filterType\": \"LOT_SIZE\",\n" +
                    "      \"minQty\": \"0.00100000\",\n" +
                    "      \"maxQty\": \"100000.00000000\",\n" +
                    "      \"stepSize\": \"0.00100000\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"filterType\": \"MARKET_LOT_SIZE\",\n" +
                    "      \"maxQty\": \"590119\",\n" +
                    "      \"minQty\": \"1\",\n" +
                    "      \"stepSize\": \"1\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"filterType\": \"PERCENT_PRICE\",\n" +
                    "      \"multiplierUp\": \"1.1500\",\n" +
                    "      \"multiplierDown\": \"0.8500\",\n" +
                    "      \"multiplierDecimal\": 4\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}", PairConfigDto::class.java).filters);
        } catch (e: Exception) {
            e.printStackTrace();
        }

        Observable.concat(sub
                .doOnNext {
                    System.out.println("==================>yes: onnext:" + it)
                }
                .flatMap {
                    Observable.empty<Any>()
                }, Observable.interval(1, TimeUnit.SECONDS).map { "_m$it" })
                .compose(XXF.bindToLifecycle(this))
                .doOnDispose {
                    System.out.println("==================>doOnDispose:")
                }
                .doOnSubscribe {
                    System.out.println("==================>doOnSubscribe:" + it)
                }
                .doOnComplete {
                    System.out.println("==================>doOnComplete:")
                }
                .subscribe {
                    System.out.println("==================>yes:" + it + " thread:" + Thread.currentThread().name)
                }

        sub.compose(XXF.bindToLifecycle(this))
                .doOnDispose {
                    System.out.println("==================>doOnDispose2:")
                }
                .doOnSubscribe {
                    System.out.println("==================>doOnSubscribe:2" + it)
                }
                .doOnComplete {
                    System.out.println("==================>doOnComplete2:")
                }
                .subscribe {
                    System.out.println("==================>yes2:" + it + " thread:" + Thread.currentThread().name)
                }

        val s = Function<Int, Boolean> {
            it % 2 == 0;
        }
        System.out.println("================>ssss:" + s.apply(3));

        val list: MutableList<Int> = mutableListOf(1, 2, 4);
        list.swap(0, 1);
        System.out.println("================>list:" + list);//================>list:[2, 1, 4]

        val stringList: MutableList<String> = mutableListOf("1", "2", "4");
        /*      stringList.swap(0, 1);*/
        stringList.swap2(0, 1);
        System.out.println("================>list2:" + stringList);//================>list:[2, 1, 4]

        Test.test();
        Test.test();
    }

    object Test {

        fun test() {
            println("==================>hello" + this.hashCode())
        }
    }

    fun Test.test() {
        print("=================>hello 扩展");
    }

    fun <T> MutableList<T>.swap2(index: Int, index2: Int) {
        val any = this[index];
        this[index] = this[index2];
        this[index2] = any;
    }


    /*   fun MutableList<Int>.swap(index: Int, index2: Int) {
           val any = this[index];
           this[index] = this[index2];
           this[index2] = any;
       }*/
}