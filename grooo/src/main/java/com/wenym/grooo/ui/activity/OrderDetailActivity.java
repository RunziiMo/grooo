package com.wenym.grooo.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityOrderDetailBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Order;
import com.wenym.grooo.model.http.CommentForm;
import com.wenym.grooo.provider.ImageBacks;
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

        Order order = getIntent().getParcelableExtra("order");

        bind.setOrder(order);

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

    public void callSeller(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bind.getOrder().getSeller().getPhone()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void postComment(View view) {

    }

    public static final int COMMENT_CODE = 1000;

    public void startComment(View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("order_id", bind.getOrder().getOrder_id());
        intent.putExtra("title", "评价 " + bind.getOrder().getSeller().getName());
        startActivityForResult(intent, COMMENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CommentForm form = null;
        if (data != null)
            form = data.getParcelableExtra("comment");
        if (requestCode == COMMENT_CODE && resultCode == RESULT_OK && form != null) {
            Log.d("onOrderCommentResult", form.toString());
            final CommentForm finalForm = form;
            NetworkWrapper.get().postComment(form)
                    .doOnSubscribe(() -> bind.orderDetailCommentProgress.setVisibility(View.VISIBLE))
                    .doOnCompleted(() -> bind.orderDetailCommentProgress.setVisibility(View.GONE))
                    .doOnError((throwable) -> bind.orderDetailCommentProgress.setVisibility(View.GONE))
                    .compose(bindToLifecycle())
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            Snackbar.make(bind.orderDetailCommentProgress, "评价成功", Snackbar.LENGTH_SHORT).show();
                            Order order = bind.getOrder();
                            order.setRating(finalForm.getRating());
                            order.setRating_remark(finalForm.getRating_remark());
                            order.setStatus(31);
                            bind.setOrder(order);
                        }
                    }, throwable -> Snackbar.make(bind.orderDetailCommentProgress, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
        }

    }

}
