package com.wenym.grooo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.wenym.grooo.R;

/**
 * Created by runzii on 16-8-1.
 */
public class FabBottomSheetBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private final static String TAG = "behavior";
    private Context mContext;

    public FabBottomSheetBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof RelativeLayout;
    }

    private int fabBottomMargin;

    private int distanceToScroll;

    private float startDependencyY;

    private int bottomSheetPeekHeight;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof RelativeLayout) {
            maybeInitProperties(child, dependency);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                //toolbar被移走的距离与本身高度的比值
                float ty = dependency.getY() - startDependencyY;
                if (ty > 0) {
                    float ratio = ty / (float) bottomSheetPeekHeight;
                    //toolbar被移走几分之几，fab就向下滑几分之几
                    child.setTranslationY(distanceToScroll * ratio);
                }
            }
        }
        return true;
    }

    private void maybeInitProperties(FloatingActionButton child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (fabBottomMargin == 0)
            fabBottomMargin = lp.bottomMargin;
        if (distanceToScroll == 0)
            distanceToScroll = child.getHeight() + fabBottomMargin;
        if (startDependencyY == 0)
            startDependencyY = dependency.getY();
        lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (bottomSheetPeekHeight == 0)
            bottomSheetPeekHeight = ((BottomSheetBehavior) lp.getBehavior()).getPeekHeight();
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
