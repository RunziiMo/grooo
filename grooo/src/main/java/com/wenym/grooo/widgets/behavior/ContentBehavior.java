package com.wenym.grooo.widgets.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wenym.grooo.R;


public class ContentBehavior extends CoordinatorLayout.Behavior<ViewPager> {

    private static final String TAG = "BottomSheet";

    boolean mInit = false;
    int mCollapsedY;
    int mPeakHeight;
    int mAnchorPointY;
    int mCurrentChildY;
    private Context mContext;

    public ContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BackdropBottomSheetBehavior_Params);
        setPeekHeight(a.getDimensionPixelSize(R.styleable.BackdropBottomSheetBehavior_Params_behavior_backdrop_peekHeight, 0));
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewPager child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewPager child, View dependency) {
        if (!(dependency instanceof AppBarLayout))
            return false;
        if (dependency.getId() == R.id.merged_appbarlayout)
            return false;
//        if (!mInit) {
//            init(child, dependency);
//            return false;
//        }
//        super.onDependentViewChanged(parent, child, dependency);
        child.setY(dependency.getY() + dependency.getHeight());
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.height = (int) (parent.getHeight() - child.getY());
        child.setLayoutParams(lp);
//        child.setBottom(parent.getBottom());
//        if((mCurrentChildY = (int) ((dependency.getY()-mAnchorPointY) * mCollapsedY / (mCollapsedY-mAnchorPointY))) <= 0)
//            child.setY(mCurrentChildY = 0);
//        else
//            child.setY(mCurrentChildY);
        return true;
    }

    private void init(@NonNull View child, @NonNull View dependency) {
        mCollapsedY = dependency.getHeight() - (2 * mPeakHeight);
        mAnchorPointY = child.getHeight();
        mCurrentChildY = (int) dependency.getY();
        //TODO
        if (mCurrentChildY == mAnchorPointY || mCurrentChildY == mAnchorPointY - 1 || mCurrentChildY == mAnchorPointY + 1)
            child.setY(0);
        else child.setY(mCurrentChildY);
        mInit = true;
    }

    public void setPeekHeight(int peakHeight) {
        this.mPeakHeight = peakHeight;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
