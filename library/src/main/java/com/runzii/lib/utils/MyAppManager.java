package com.runzii.lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by runzii on 15-10-30.
 */
public abstract class MyAppManager {

    private static Context appContext = null;

    protected static void init(Context ctx) {
        appContext = ctx;
        pkgName = ctx.getPackageName();
    }

    public static Context getAppContext() {
        return appContext;
    }

    //包名
    private static String pkgName;

    public static String getPackageName() {
        return pkgName;
    }

    /**
     * 获取应用程序版本名称
     *
     * @return
     */
    public static String getVersion() {
        String version = "0.0.0";
        if (appContext == null) {
            return version;
        }
        try {
            PackageInfo packageInfo = appContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = appContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
