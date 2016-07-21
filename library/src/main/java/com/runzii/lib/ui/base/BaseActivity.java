package com.runzii.lib.ui.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.runzii.lib.R;
import com.runzii.lib.app.SwipeBackActivity;

import cn.jpush.android.api.JPushInterface;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wouldyou on 2015/6/10.
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends SwipeBackActivity {


    private CompositeSubscription _compositeSubscription = new CompositeSubscription();

    protected abstract boolean isDisplayHomeAsUp();

    protected abstract boolean isEnableSwipe();

    protected abstract int getLayoutId();

    protected T bind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutId() != 0)
            bind = DataBindingUtil.setContentView(this, getLayoutId());
        else bind = DataBindingUtil.setContentView(this, R.layout.activity_base);

        setSwipeBackEnable(isEnableSwipe());


        if (isDisplayHomeAsUp() && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (findViewById(R.id.toolbar_dropshadow) != null) {
                findViewById(R.id.toolbar_dropshadow).setVisibility(View.GONE);
            }
        }

    }

    public final void addSubscription(Subscription s) {
        _compositeSubscription.add(s);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        _compositeSubscription.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                hideSoftKeyboard();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * hide inputMethod
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }


    protected Action1<Throwable> errorHandle(String message) {
        return throwable -> Snackbar.make(bind.getRoot(), message + ' ' + throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

}
