package com.wenym.grooo;

import com.runzii.lib.MyApplication;
import com.wenym.grooo.util.GroooAppManager;

public class GroooApplication extends MyApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        GroooAppManager.init(this);

    }
}
