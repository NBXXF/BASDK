package com.bkt.basdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bkt.basdk.api.ContractApiService
import com.bkt.basdk.model.HttpStatus
import com.bkt.contract.ba.model.OderFilterDto
import com.bkt.contract.ba.model.TradingPairDto
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        XXF.getApiService(ContractApiService::class.java)
                .testApi2()
                .compose(XXF.bindToErrorNotice<HttpStatus>())
                .subscribe({ httpStatus ->
                    XXF.getLogger().d("================>yes:$httpStatus")
                }) { throwable -> XXF.getLogger().d("================>no:$throwable") }


    }

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
                    "}", TradingPairDto::class.java).filters);
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
}