package com.wenym.grooo.ui.profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentProfileBinding;
import com.wenym.grooo.model.viewmodel.ProfileViewModel;
import com.wenym.grooo.provider.ImageBacks;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.stackblur.StackBlurManager;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    public ProfileViewModel model;

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
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ProfileViewModel(getActivity());
        bind.setModel(model);
        loadBackDrop();
    }

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
    public void onDestroy() {
        super.onDestroy();
        if (model != null)
            model.onDestroy();
    }
}
