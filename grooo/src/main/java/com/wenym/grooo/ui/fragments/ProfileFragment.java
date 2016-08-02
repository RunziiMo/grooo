package com.wenym.grooo.ui.fragments;

import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.jakewharton.rxbinding.view.RxView;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentProfileBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.provider.ImageBacks;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.util.GroooAppManager;
import com.wenym.grooo.util.stackblur.StackBlurManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> implements FABProgressListener {


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
        bind.setProfile(AppPreferences.get().getProfile());
        bind.setAddress(AppPreferences.get().getAddress());
        loadBackDrop();
        bind.fabProgressCircle.attachListener(this);
        RxView.clicks(bind.fab).debounce(500, TimeUnit.MILLISECONDS)
                .map((aVoid) -> editing.get())
                .flatMap(aBoolean -> {
                    if (aBoolean) {
                        return NetworkWrapper.get().putProfile(bind.getProfile())
                                .doOnSubscribe(() -> bind.fabProgressCircle.show())
                                .doOnError(throwable -> bind.fabProgressCircle.hide())
                                .doOnCompleted(() -> bind.fabProgressCircle.beginFinalAnimation());
                    } else {
                        bind.fab.setImageResource(R.drawable.ic_file_cloud_upload);
                        editing.set(true);
                    }
                    return null;
                })
                .subscribe(s -> {
                            editing.set(false);
                            Snackbar.make(bind.fab, s, Snackbar.LENGTH_SHORT).show();
                        }
                        , throwable -> Snackbar.make(bind.fab, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
    }

    private ObservableBoolean editing = new ObservableBoolean(false);

    private void loadBackDrop() {

        Glide.with(this).load(ImageBacks.getOne()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                new Thread(() -> {
                    Bitmap bitmap = new StackBlurManager(resource).process(15);
                    getActivity().runOnUiThread(() -> {
                        AlphaAnimation animation = new AlphaAnimation(0f, 1.0f);
                        animation.setDuration(2500);
                        bind.backdrop.startAnimation(animation);
                        bind.backdrop.setImageBitmap(bitmap);
                    });
                }).start();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(bind.toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.label_profile);
        }
    }

    @Override
    public void onFABProgressAnimationEnd() {
        bind.fab.setImageResource(R.drawable.ic_content_create);
    }
}
