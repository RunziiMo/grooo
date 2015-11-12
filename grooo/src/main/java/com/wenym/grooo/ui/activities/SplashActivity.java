package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wenym.grooo.R;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.ui.base.BaseActivity;

import butterknife.InjectView;


/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {

    @InjectView(R.id.splash_root)
    FrameLayout rootLayout;
    @InjectView(R.id.tvVersion)
    TextView versionText;
    @InjectView(R.id.welcome_hint1)
    TextView welcomehint1;
    @InjectView(R.id.spalash_background)
    ImageView back;

    private static final int sleepTime = 2000;

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

        versionText = (TextView) findViewById(R.id.tvVersion);
        welcomehint1 = (TextView) findViewById(R.id.welcome_hint1);

        Picasso.with(this).load(R.drawable.grooo_spalash).into(back);

//        versionText.setText("GROOO " + GroooAppManager.getVersion());
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2500);
        rootLayout.startAnimation(animation);
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
}
