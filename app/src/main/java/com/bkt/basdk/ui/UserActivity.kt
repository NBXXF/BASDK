package com.bkt.basdk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bkt.basdk.R
import com.bkt.contract.ba.enums.ContractType
import com.bkt.contract.ba.sdk.BaClient
import com.bkt.contract.ba.service.UserService
import com.xxf.arch.XXF
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_user.*

/**
 * @Description:
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/18 14:39
 */
class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user);
        initView();
    }

    private fun initView() {
        createListenKey.setOnClickListener {
            BaClient.instance.getService(UserService::class.java)
                    .createListenKey(ContractType.USDT)
                    .observeOn(AndroidSchedulers.mainThread())
                    .`as`(XXF.bindLifecycle(this))
                    .subscribe {
                        listenKeyTv.text = it.listenKey;
                    };
            BaClient.instance.getService(UserService::class.java)
                    .createListenKey(ContractType.USD)
                    .observeOn(AndroidSchedulers.mainThread())
                    .`as`(XXF.bindLifecycle(this))
                    .subscribe {
                        listenKeyTv.text = it.listenKey;
                    };
        }
    }
}