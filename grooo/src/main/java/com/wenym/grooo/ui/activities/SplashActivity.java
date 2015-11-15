package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wenym.grooo.R;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.ui.base.BaseActivity;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.utils.Tools;

import butterknife.InjectView;


/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity implements ImageLoadingListener {

    @InjectView(R.id.splash_root)
    FrameLayout rootLayout;
    @InjectView(R.id.tvVersion)
    TextView versionText;
    @InjectView(R.id.welcome_hint1)
    TextView welcomehint1;
    @InjectView(R.id.spalash_background)
    ImageView back;

    private static final int sleepTime = 3000;

    @Override
    protected boolean isHideNavigationBar() {
        return true;
    }

    @Override
    protected boolean isDisplayHomeAsUp() {
        return false;
    }

    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        if (PreferenceManager.getInstance().isFirstUse()) {
//            PreferenceManager.getInstance().setFirstUse(false);
//            startActivity(new Intent(this, IntroduceActivity.class));
//            finish();
//        }


//        ImageLoader.getInstance().displayImage(SmallTools.resourceIdToUri(R.drawable.grooo_spalash),back,this);
        Picasso.with(this).load(R.drawable.grooo_spalash)
                .resize(Tools.getScreenWidth(this), Tools.getScreenHeight(this))
                .centerCrop()
                .into(back, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2500);
        rootLayout.startAnimation(animation);

//        versionText.setText("GROOO " + GroooAppManager.getVersion());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GroooAppManager.initUserData(new GroooAppManager.InitCallback() {
            @Override
            public void onSuccess() {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                        if (GroooAppManager.getAppUser() != null) {
                            //进入主页面
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, GroooLoginActivity.class));
                            finish();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }
}
