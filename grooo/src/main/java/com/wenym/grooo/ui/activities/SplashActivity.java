package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivitySplashBinding;
import com.wenym.grooo.util.GroooAppManager;


/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    private static final int sleepTime = 3000;

    @Override
    protected boolean isDisplayHomeAsUp() {
        return false;
    }

    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return true;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);



//        if (AppPreferences.get().isFirstUse()) {
//            AppPreferences.get().setFirstUse(false);
//            startActivity(new Intent(this, IntroduceActivity.class));
//            finish();
//        }

        Glide.with(this).load(R.drawable.grooo_spalash)
                .into(bind.spalashBackground);

        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2500);
        bind.splashRoot.startAnimation(animation);

//        versionText.setText("GROOO " + GroooAppManager.getVersion());

        new Thread(() -> {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
            if (GroooAppManager.getProfile() != null) {
                //进入主页面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, GroooLoginActivity.class));
                finish();
            }
        }).start();
    }

}
