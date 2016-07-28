package com.wenym.grooo.ui.activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityOrderDetailBinding;
import com.wenym.grooo.model.ecnomy.Order;
import com.wenym.grooo.provider.ImageBacks;
import com.wenym.grooo.util.SmallTools;
import com.wenym.grooo.util.stackblur.StackBlurManager;

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> {


    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind.setOrder(SmallTools.fromGson(getIntent().getStringExtra("order"), Order.class));


        loadBackDrop();

    }

    private void loadBackDrop() {

        Glide.with(this).load(ImageBacks.getOne()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                new Thread(() -> {
                    Bitmap bitmap = new StackBlurManager(resource).process(15);
                    runOnUiThread(() -> {
                        AlphaAnimation animation = new AlphaAnimation(0f, 1.0f);
                        animation.setDuration(2500);
                        bind.backdrop.startAnimation(animation);
                        bind.backdrop.setImageBitmap(bitmap);
                    });
                }).start();
            }
        });
    }

}
