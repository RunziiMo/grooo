package com.wenym.grooo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wenym.grooo.http.model.InitBuildingData;
import com.wenym.grooo.http.model.InitBuildingsSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.Building;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;

public class GroooApplication extends Application {

    public static DisplayImageOptions options;
    public static ArrayList<Building> buildings;


    private static SharedPreferences sh;
    private static Context ct;

    public static SharedPreferences getAppSharedPreferences() {
        return sh;
    }

    public static Context getGroooApplicationContext() {
        return ct;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        options = new DisplayImageOptions.Builder().cacheOnDisk(true)
                .cacheInMemory(true).showImageOnFail(R.drawable.responsible)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        sh = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        ct = getApplicationContext();

        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration
                        .createDefault(getApplicationContext()));

        HttpUtils.init(this);

        HttpUtils.MakeAPICall(new InitBuildingData(), ct, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                InitBuildingsSuccessData buildingsSuccessData = (InitBuildingsSuccessData) object;
                buildings = buildingsSuccessData.getBuildings();
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
}
