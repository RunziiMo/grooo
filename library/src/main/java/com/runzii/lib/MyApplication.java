package com.runzii.lib;


import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import im.fir.sdk.FIR;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        FIR.init(this);

    }

}
