package com.wenym.grooo.model.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import com.wenym.grooo.R;

/**
 * Created by runzii on 16-8-26.
 */
public class ShopViewModel extends BaseViewModel {

    public ObservableBoolean isBottomExpanded = new ObservableBoolean(false);

    public ShopViewModel(Activity activity) {
        super(activity);
    }

    @BindingAdapter("animation")
    public static void setAnimation(LinearLayout view, boolean isAnimation) {
        if (view.getTag() == null) {
            view.setTag(true);
            return;
        }
        Animator animator;
        if (isAnimation) {
            animator = AnimatorInflater.loadAnimator(view.getContext(), R.anim.bottom_back_expand);
        } else {
            animator = AnimatorInflater.loadAnimator(view.getContext(), R.anim.bottom_back_collapse);
        }
        animator.setTarget(view);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                int backColor = ContextCompat.getColor(view.getContext(), isAnimation ? R.color.colorPrimary : R.color.white);
                view.setBackgroundColor(backColor);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }
}
