package com.bkt.basdk;

import android.app.Application;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.view.loading.DefaultProgressHUDImpl;

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
    }
}
