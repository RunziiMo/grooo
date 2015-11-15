package com.wenym.grooo;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.testin.agent.TestinAgent;
import com.testin.agent.TestinAgentConfig;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.utils.GroooAppManager;

import cn.jpush.android.api.JPushInterface;

public class GroooApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        GroooAppManager.init(this);

        HttpUtils.init(this);

        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration
                        .createDefault(getApplicationContext()));

        TestinAgent.init(this);
        TestinAgent.setLocalDebug(false);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }
}
