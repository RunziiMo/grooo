package com.wenym.grooo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.GetBaiduPictureSuccessData;
import com.wenym.grooo.http.model.GetOrderData;
import com.wenym.grooo.http.model.GetOrderSuccessData;
import com.wenym.grooo.http.model.InitBuildingData;
import com.wenym.grooo.http.model.InitBuildingsSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.AppUser;
import com.wenym.grooo.model.Building;
import com.wenym.grooo.model.DeliveryOrder;
import com.wenym.grooo.model.FoodOrder;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runzii on 15-10-30.
 */
public class GroooAppManager {

    private static DisplayImageOptions options;
    private static ArrayList<Building> buildings;
    private static List<FoodOrder> takeouts;
    private static List<DeliveryOrder> deliveries;

    private static AppUser appUser = null;

    private static Context appContext = null;

    public static void init(Context ctx) {
        appContext = ctx;
        options = new DisplayImageOptions.Builder().cacheOnDisk(true)
                .cacheInMemory(true).showImageOnFail(R.drawable.responsible)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public static void initUserData(InitCallback callback) {
        GroooAppManager.callback = callback;
        options = new DisplayImageOptions.Builder().cacheOnDisk(true)
                .cacheInMemory(true).showImageOnFail(R.drawable.responsible)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        appUser = PreferencesUtil.getInstance().getAppUser();
        initBuildings(appContext);
    }

    private static InitCallback callback;

    public static interface InitCallback {

        void onSuccess();
    }

    public synchronized static void initOrders(Context ctx) {

        HttpUtils.MakeAPICall(new GetOrderData(), ctx, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                GetOrderSuccessData data = (GetOrderSuccessData) object;

                if (data.getRestaurant().size() != 0) {
                    takeouts = data.getRestaurant();
                }
                if (data.getDelivery().size() != 0) {
                    deliveries = data.getDelivery();
                }
                callback.onSuccess();
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("orders " + statusCode);
            }
        });

    }

    private static void initBuildings(final Context ctx) {
        HttpUtils.MakeAPICall(new InitBuildingData(), ctx, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                InitBuildingsSuccessData buildingsSuccessData = (InitBuildingsSuccessData) object;

                if (buildingsSuccessData.getBuildings().size() != 0) {
                    PreferencesUtil.getInstance().setBuildings(buildingsSuccessData);
                    buildings = buildingsSuccessData.getBuildings();
                } else {
                    buildings = PreferencesUtil.getInstance().getBuildings();
                }

                if (appUser != null) {
                    initOrders(ctx);
                }
            }

            @Override
            public void onFailed() {
                Toasts.show("没有获取到建筑");
            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("buildings " + statusCode);
            }
        });
    }


    public static AppUser getAppUser() {
        if (appUser == null) {
            appUser = PreferencesUtil.getInstance().getAppUser();
        }
        return appUser;
    }

    public static void setAppUser(AppUser user) {
        appUser = user;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        GroooAppManager.appContext = appContext;
    }

    public static DisplayImageOptions getOptions() {
        return options;
    }

    public static ArrayList<Building> getBuildings() {
        if (buildings == null || buildings.size() == 0) {
            initBuildings(appContext);
            return PreferencesUtil.getInstance().getBuildings();
        } else {
            return buildings;
        }
    }

    public static List<FoodOrder> getTakeouts() {
        if (takeouts == null) {
            initOrders(appContext);
            return null;
        }
        return takeouts;
    }

    public static List<DeliveryOrder> getDeliveries() {
        if (deliveries == null) {
            initOrders(appContext);
            return null;
        }
        return deliveries;
    }

    //包名
    private static String pkgName = "com.wenym.grooo";

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
}
