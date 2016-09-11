package com.wenym.grooo.widgets.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.runzii.lib.widgets.behavior.BottomSheetAnchorBehavior;
import com.wenym.grooo.R;

/**
 * Created by runzii on 16-8-1.
 */
public class FabBottomSheetBehavior extends FloatingActionButton.Behavior {

    private final static String TAG = "behavior";
    private Context mContext;

    public FabBottomSheetBehavior(Context context, AttributeSet attrs) {
        mContext = context;
        offset = 0;
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    private int fabBottomMargin;

    private int distanceToScroll;

    private float startDependencyY;

    private int bottomSheetPeekHeight;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {

        if (offset == 0)
            setOffsetValue(parent);

        if (child.getY() <= (offset + child.getHeight()) && child.getVisibility() == View.VISIBLE)
            child.hide();
        else if (child.getY() > offset && child.getVisibility() != View.VISIBLE)
            child.show();

        if (dependency instanceof NestedScrollView) {
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
        if (bottomSheetPeekHeight == 0
                && lp.getBehavior() != null
                && lp.getBehavior() instanceof BottomSheetAnchorBehavior)
            bottomSheetPeekHeight = ((BottomSheetAnchorBehavior<View>) lp.getBehavior()).getPeekHeight();
    }

    float offset;

    private void setOffsetValue(CoordinatorLayout coordinatorLayout) {

        for (int i = 0; i < coordinatorLayout.getChildCount(); i++) {
            View child = coordinatorLayout.getChildAt(i);

            if (child instanceof AppBarLayout) {

                if (child.getTag() != null &&
                        child.getTag().toString().contentEquals("modal-appbar")) {
                    offset = child.getY() + child.getHeight();
                    break;
                }
            }
        }
    }
}
