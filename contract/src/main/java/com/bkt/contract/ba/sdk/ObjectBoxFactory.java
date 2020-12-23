package com.bkt.contract.ba.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;

import androidx.annotation.NonNull;

import com.bkt.contract.ba.model.po.MyObjectBox;
import com.xxf.arch.XXF;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.objectbox.BoxStore;

/**
 * @Description: objectBox
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/7/16 17:34
 */
public class ObjectBoxFactory {
    private static final String DB_NAME_FORMAT = "contact_web_socket_db_ba_%s_%s";

    private static volatile Map<String, BoxStore> boxStoreMap = new ConcurrentHashMap<>();

    /**
     * 区分登录未登录
     *
     * @return
     */
    public static synchronized BoxStore getBoxStore() {
        BoxStore boxStore = null;
        String dbName = getDbName();
        try {
            boxStore = boxStoreMap.get(dbName);
            if (boxStore == null) {
                boxStoreMap.put(dbName, boxStore = buildBox(generateDbFile(dbName)));
            }
        } catch (Exception e) {
            try {
                /**
                 * fix https://github.com/objectbox/objectbox-java/issues/610
                 */
                BoxStore.deleteAllFiles(generateDbFile(dbName));
                boxStoreMap.put(dbName, boxStore = buildBox(generateDbFile(dbName)));
            } catch (Exception retryEx) {
                retryEx.printStackTrace();
            }
        }
        return boxStore;
    }

    private static String getDbName() {
        try {
            PackageInfo packageInfo = XXF.getApplication().getPackageManager().getPackageInfo(XXF.getApplication().getPackageName(), 0);
            return String.format(DB_NAME_FORMAT, packageInfo.versionName, packageInfo.versionCode, XXF.getUserInfoProvider().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format(DB_NAME_FORMAT, "", "", XXF.getUserInfoProvider().getUserId());
    }

    private static synchronized File generateDbFile(String dbName) {
        return new File(XXF.getApplication().getCacheDir(), dbName);
    }

    /**
     * 构建数据库
     *
     * @param objectStoreDirectory
     * @return
     * @throws io.objectbox.exception.DbException
     */
    private static synchronized BoxStore buildBox(@NonNull File objectStoreDirectory) throws io.objectbox.exception.DbException {
        return MyObjectBox.builder().directory(objectStoreDirectory)
                .build();
    }


}
