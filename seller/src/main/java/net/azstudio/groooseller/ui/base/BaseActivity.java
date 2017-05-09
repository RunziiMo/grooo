package net.azstudio.groooseller.ui.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import com.runzii.lib.app.SwipeBackActivity;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.utils.Logs;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wouldyou on 2015/6/10.
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends SwipeBackActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CompositeSubscription _compositeSubscription = new CompositeSubscription();

    private T t;

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected abstract boolean isDisplayHomeAsUp();

    protected abstract boolean isEnableSwipe();

    protected abstract int getLayoutId();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectViews();
    }

    protected void injectViews() {

    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        this.toolbar = toolbar;
        super.setSupportActionBar(toolbar);
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    public T getDataBinding() {
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSwipeBackEnable(isEnableSwipe());

        if (getLayoutId() != 0) {
            t = DataBindingUtil.setContentView(this, getLayoutId());
        } else {
            Logs.d("avtivity_base");
            t = DataBindingUtil.setContentView(this, R.layout.activity_base);
        }
        ButterKnife.bind(this);
        setupToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            sHandler = new Handler();
            sHandler.post(mHideRunnable); // hide the navigation bar
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    sHandler.post(mHideRunnable); // hide the navigation bar
                }
            });
        }
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

    private static Handler sHandler;

    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            int flags;
            int curApiVersion = Build.VERSION.SDK_INT;
            // This work only for android 4.4+
            if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
                // This work only for android 4.4+
                // hide navigation bar permanently in android activity
                // touch the screen, the navigation bar will not show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;

            } else {
                // touch the screen, the navigation bar will show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            // must be executed in main thread :)
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    };

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


}
