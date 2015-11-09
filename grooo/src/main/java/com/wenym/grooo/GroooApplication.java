package com.wenym.grooo;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.utils.GroooAppManager;

public class GroooApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        GroooAppManager.init(this);

        HttpUtils.init(this);

        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration
                        .createDefault(getApplicationContext()));

    }
}
