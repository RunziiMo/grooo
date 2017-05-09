package net.azstudio.groooseller.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.model.business.DeliveryOrder;
import net.azstudio.groooseller.model.business.FoodOrder;

import java.util.List;


/**
 * Created by runzii on 15-10-30.
 */
public class AppManager {

    private static List<FoodOrder> takeouts;
    private static List<DeliveryOrder> deliveries;

    private static ShopInfo shopInfo = null;

    private static Context appContext = null;

    public static void init(Context ctx) {
        pkgName = ctx.getPackageName();
        appContext = ctx;
    }

    public static ShopInfo getShopInfo() {
        if (shopInfo == null) {
            shopInfo = AppPreferences.get().getShopInfo();
            if (shopInfo == null) {
                shopInfo = new ShopInfo();
            }
        }
        return shopInfo;
    }

    public static void setShopInfo(ShopInfo user) {
        AppPreferences.get().setShopInfo(user);
        shopInfo = user;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        AppManager.appContext = appContext;
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
