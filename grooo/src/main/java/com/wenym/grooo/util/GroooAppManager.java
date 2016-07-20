package com.wenym.grooo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.runzii.lib.constant.MyActions;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.model.ecnomy.Order;

import java.util.List;

/**
 * Created by runzii on 15-10-30.
 */
public class GroooAppManager {

    private static Address addresses;
    private static List<Order> orders;
    private static String authToken;

    private static Profile profile;

    private static Context appContext = null;


    public static void init(Context ctx) {
        appContext = ctx;
        pkgName = ctx.getPackageName();
        profile = AppPreferences.get().getProfile();
        addresses = AppPreferences.get().getAddress();
        authToken = AppPreferences.get().getAuth();
    }

    public static Profile getProfile() {
        return profile;
    }

    public static String getAuthToken() {
        if (TextUtils.isEmpty(authToken)) {
            NetworkWrapper.get().getAuthToken(AppPreferences.get().getAuthUser())
                    .subscribe(authToken1 -> {
                        setAuthToken(authToken1.getToken());
                    }, throwable -> {
                        appContext.sendBroadcast(new Intent(MyActions.ACTION_LOGOUT));
                        throwable.printStackTrace();
                    });
        }
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        GroooAppManager.authToken = authToken;
        AppPreferences.get().setAuth(authToken);
    }

    public static void setProfile(Profile user) {
        AppPreferences.get().setProfile(user);
        profile = user;
    }

    public static Address getAddresses() {
        return addresses;
    }

    public static void setAddresses(Address addresses) {
        AppPreferences.get().setAddress(addresses);
        GroooAppManager.addresses = addresses;
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
