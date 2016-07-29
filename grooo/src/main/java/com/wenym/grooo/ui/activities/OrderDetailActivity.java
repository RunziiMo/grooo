package com.wenym.grooo.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.Property;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.runzii.lib.ui.base.BaseActivity;
import com.runzii.lib.widgets.AnimatorPath;
import com.runzii.lib.widgets.PathEvaluator;
import com.runzii.lib.widgets.PathPoint;
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

        RxView.focusChanges(bind.orderDetailOrderRatingRemark)
                .compose(bindToLifecycle())
                .subscribe(aBoolean -> {
                    bind.appbar.setExpanded(!aBoolean, true);
                });

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

    public void callSeller(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bind.getOrder().getSeller().getPhone()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void postComment(View view) {


    }

    public void startComment(View view) {

        final int length = bind.orderDetailOrderRatingEdit.getChildCount();
        bind.orderDetailOrderRatingEdit.setVisibility(View.VISIBLE);

        final Animator[] animators = new Animator[length];
        for (int i = 0; i < length; i++) {
            View target = bind.orderDetailOrderRatingEdit.getChildAt(i);
            final float x0 = 0;// i == 0 ? 0 : -10 * (1 + i * 0.2f);
            final float y0 = 10 * i;

            target.setTranslationX(x0);
            target.setTranslationY(y0);

            AnimatorPath path = new AnimatorPath();
            path.moveTo(x0, y0);
            path.lineTo(0, 0);

            PathPoint[] points = new PathPoint[path.getPoints().size()];
            path.getPoints().toArray(points);

            AnimatorSet set = new AnimatorSet();
            set.play(ObjectAnimator.ofObject(target, PATH_POINT, new PathEvaluator(), points))
                    .with(ObjectAnimator.ofFloat(target, View.ALPHA, 0.8f, 1f));

            animators[i] = set;
            animators[i].setStartDelay(15 * i);
        }

        final AnimatorSet sequential = new AnimatorSet();
        sequential.playTogether(animators);
        sequential.setInterpolator(new FastOutLinearInInterpolator());
        sequential.setDuration(200);
        sequential.start();
    }


    private final static Property<View, PathPoint> PATH_POINT =
            new Property<View, PathPoint>(PathPoint.class, "PATH_POINT") {
                PathPoint point;

                @Override
                public PathPoint get(View object) {
                    return point;
                }

                @Override
                public void set(View object, PathPoint value) {
                    point = value;

                    object.setTranslationX(value.mX);
                    object.setTranslationY(value.mY);
                }
            };
}
