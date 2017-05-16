package com.wenym.grooo;

import android.content.Context;

import com.runzii.lib.MyApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.wenym.grooo.util.GroooAppManager;

public class GroooApplication extends MyApplication {

    public static RefWatcher getRefWatcher(Context context) {
        GroooApplication application = (GroooApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        GroooAppManager.init(this);

        //内存泄露检测
        refWatcher = LeakCanary.install(this);
    }
}
