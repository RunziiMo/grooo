package com.wenym.grooo.ui.fragments;

import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentProfileBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.provider.ImageBacks;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.GroooAppManager;
import com.wenym.grooo.util.stackblur.StackBlurManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind.setEditng(editing);
        loadBackDrop();
        RxView.clicks(bind.fab).debounce(500, TimeUnit.MILLISECONDS)
                .map((aVoid) -> editing.get())
                .flatMap(aBoolean -> {
                    if (aBoolean) {
                        bind.fab.setImageResource(R.drawable.ic_file_cloud_upload);
                        return NetworkWrapper.get().putProfile(bind.getProfile());
                    } else
                        editing.set(true);
                    return null;
                })
    }

    private ObservableBoolean editing = new ObservableBoolean(false);

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
