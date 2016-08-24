package com.wenym.grooo.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.wenym.grooo.R;
import com.wenym.grooo.model.viewmodel.BaseViewModel;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by runzii on 15-9-24.
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    private CompositeSubscription mCompositeSubscription;


    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 广播拦截器
     */
    private InternalReceiver internalReceiver;

    /**
     * 当前页面是否可以销毁
     */
    private boolean isFinish = false;

    /**
     * 如果子界面需要拦截处理注册的广播
     * 需要实现该方法
     *
     * @param context
     * @param intent
     */
    protected void handleReceiver(Context context, Intent intent) {
        // 广播处理
    }

    protected abstract int getLayoutId();

    protected T bind;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), container, false);
        bind = DataBindingUtil.bind(v);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            // 注销广播监听器
            getActivity().unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }
    }

    protected Action1<Throwable> errorHandle(String message) {
        return throwable -> Snackbar.make(getView(), message + ' ' + throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 处理按钮按下事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getActivity().onKeyDown(keyCode, event)) {
            return true;
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return false;
    }

    /**
     * 注册广播Action，子类如果需要监听广播可以调用
     * 该方法传入相应事件的Action
     *
     * @param actionArray
     */
    protected final void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        for (String action : actionArray) {
            intentfilter.addAction(action);
        }
        //intentfilter.addAction(CASIntent.ACTION_SERVICE_DESTORY);
        //intentfilter.addAction(CASIntent.ACTION_FORCE_DEACTIVE);
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        getActivity().registerReceiver(internalReceiver, intentfilter);
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    /**
     * 重载页面关闭方法
     */
    public void finish() {
        if (getActivity() == null) {
            return;
        }
        if (isFinish) {
            getActivity().finish();
            return;
        }

        getActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * 自定义应用全局广播处理器，方便全局拦截广播并进行分发
     *
     * @author 容联•云通讯
     * @version 4.0
     * @date 2014-12-4
     */
    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            handleReceiver(context, intent);
        }

    }

    /**
     * hide inputMethod
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getActivity().getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    /**
     *
     */
    public void toggleSoftInput() {
        // Display the soft keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getActivity().getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (getView() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            getView().startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (getView() != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            getView().startAnimation(fadeOut);
        }
    }
}
