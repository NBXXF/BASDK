package com.bkt.basdk;

import android.app.Application;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.bkt.basdk.api.UsdContractApiService;
import com.bkt.basdk.api.UsdContractSocketService;
import com.bkt.basdk.api.UsdtContractApiService;
import com.bkt.basdk.api.UsdtContractSocketService;
import com.bkt.contract.ba.enums.ContractType;
import com.bkt.contract.ba.sdk.BaClient;
import com.bkt.contract.ba.sdk.ContractProxyApiService;
import com.bkt.contract.ba.sdk.ContractProxySocketService;
import com.xxf.arch.XXF;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.view.loading.DefaultProgressHUDImpl;

import org.jetbrains.annotations.NotNull;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @Description: TODO @XGode
 * @Author: XGod
 * @CreateDate: 2020/12/2 10:16
 */
public class BaseApplication extends Application {
    private static volatile BaseApplication INSTANCE;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("=============>", "", throwable);
            }
        });
        XXF.init(new XXF.Builder(this, new ProgressHUDFactory.ProgressHUDProvider() {
            @Override
            public ProgressHUD onCreateProgressHUD(LifecycleOwner lifecycleOwner) {
                if (lifecycleOwner instanceof FragmentActivity) {
                    return new DefaultProgressHUDImpl((FragmentActivity) lifecycleOwner);
                } else if (lifecycleOwner instanceof Fragment) {
                    return new DefaultProgressHUDImpl(((Fragment) lifecycleOwner).getContext());
                }
                return null;
            }
        }).setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtils.showToast("error:" + throwable, ToastUtils.ToastType.ERROR);
            }
        }));
        BaClient.Companion.getInstance().init(new BaClient.Initializer() {
            @NotNull
            @Override
            public ContractProxyApiService getApiService(@NotNull ContractType type) {
                if (type == ContractType.USD) {
                    return XXF.getApiService(UsdContractApiService.class);
                } else {
                    return XXF.getApiService(UsdtContractApiService.class);
                }
            }

            @NotNull
            @Override
            public ContractProxySocketService getSocketService(@NotNull ContractType type) {
                if (type == ContractType.USD) {
                    return UsdContractSocketService.Companion.getInstance();
                } else {
                    return UsdtContractSocketService.Companion.getInstance();
                }
            }
        });
    }
}
