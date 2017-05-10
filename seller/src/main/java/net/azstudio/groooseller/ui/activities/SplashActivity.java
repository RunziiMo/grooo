package net.azstudio.groooseller.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.ActivitySplashBinding;
import net.azstudio.groooseller.ui.base.BaseActivity;


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
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        if (PreferenceManager.getInstance().isFirstUse()) {
//            PreferenceManager.getInstance().setFirstUse(false);
//            startActivity(new Intent(this, IntroduceActivity.class));
//            finish();
//        }

        Glide.with(this).load(R.drawable.grooo_spalash)
                .centerCrop()
                .into(binding.spalashBackground);

        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2500);
        binding.splashRoot.startAnimation(animation);

//        versionText.setText("GROOO " + GroooAppManager.getVersion());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

}
