package com.wenym.grooo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by runzii on 15-10-30.
 */
public class GroooAppManager {

    private static Context appContext = null;


    public static void init(Context ctx) {
        appContext = ctx;
        pkgName = ctx.getPackageName();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        GroooAppManager.appContext = appContext;
    }

    //包名
    private static String pkgName = null;

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
