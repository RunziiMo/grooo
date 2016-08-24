package com.wenym.grooo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.wenym.grooo.R;

/**
 * Created by runzii on 16-8-1.
 */
public class AvatarImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private final static float MIN_AVATAR_PERCENTAGE_SIZE = 0.3f;
    private final static int EXTRA_FINAL_AVATAR_PADDING = 80;

    private final static String TAG = "behavior";
    private Context mContext;

    private float mCustomFinalYPosition;
    private float mCustomStartXPosition;
    private float mCustomStartToolbarPosition;
    private float mCustomStartHeight;
    private float mCustomFinalHeight;

    private float mAvatarMaxSize;
    private float mFinalLeftAvatarPadding;
    private float mStartPosition;
    private float mStartXPosition;
    private float mStartYPosition;
    private float mStartToolbarPosition;
    private float mStartDependencyY;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalXPosition;
    private float mChangeBehaviorY;

    public AvatarImageBehavior(Context context, AttributeSet attrs) {
        mContext = context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior);
            mCustomFinalYPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0);
            mCustomStartXPosition = a.getDimension(R.styleable.AvatarImageBehavior_startXPosition, 0);
            mCustomStartToolbarPosition = a.getDimension(R.styleable.AvatarImageBehavior_startToolbarPosition, 0);
            mCustomStartHeight = a.getDimension(R.styleable.AvatarImageBehavior_startHeight, 0);
            mCustomFinalHeight = a.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0);

            a.recycle();
        }

        init();

        mFinalLeftAvatarPadding = context.getResources().getDimension(
                R.dimen.activity_vertical_margin);
    }

    private void init() {
        bindDimensions();
    }

    private void bindDimensions() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof FrameLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        maybeInitProperties(child, dependency);

        Log.e("AvatarImageBehavior", dependency.getY() + " " + mFinalXPosition + dependency);
        final int maxScrollDistance = (int) (mStartToolbarPosition);
        float expandedPercentageFactor = 1 - (dependency.getY() - getActionBarHeight()) / (mChangeBehaviorY - getActionBarHeight());

        if (dependency.getY() < mChangeBehaviorY) {

//
//            child.setY(mStartYPosition - distanceYToSubtract);
//
//            float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);
//
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.height = lp.width = (int) (mStartHeight - (mStartHeight - mCustomFinalHeight) * expandedPercentageFactor);
            child.setLayoutParams(lp);
            float distanceXToSubtract = (mStartXPosition - mFinalXPosition) * expandedPercentageFactor;
            child.setX(mStartXPosition - distanceXToSubtract);
            child.setY(mFinalYPosition);
        } else {
            child.setY(mStartYPosition - (mStartDependencyY - dependency.getY()));
            child.setX(mStartXPosition);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = mStartHeight;
            lp.height = mStartHeight;
            child.setLayoutParams(lp);

        }
        return true;
    }

    private void maybeInitProperties(CircleImageView child, View dependency) {
        if (mStartDependencyY == 0)
            mStartDependencyY = dependency.getY();

        if (mFinalYPosition == 0)
            mFinalYPosition = (int) ((getActionBarHeight() - mCustomFinalHeight) / 2);

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (mStartXPosition == 0)
            mStartXPosition = child.getX();

        if (mStartYPosition == 0)
            mStartYPosition = child.getY();

        if (mFinalXPosition == 0)
            mFinalXPosition = mContext.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + ((int) mCustomFinalHeight / 2);

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY();

        if (mChangeBehaviorY == 0) {
            mChangeBehaviorY = dependency.getY() - child.getY() + mFinalYPosition;
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getActionBarHeight() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.actionBarSize);
    }
}
