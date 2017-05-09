package net.azstudio.groooseller.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;

/**
 * Created by runzii on 15-9-24.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 广播拦截器
     */
    private InternalReceiver internalReceiver;

    /**
     * 当前页面是否可以销毁
     */
    private boolean isFinish = false;

    protected abstract int getLayoutId();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 查找	View
     *
     * @param paramInt
     * @return
     */
    public final View findViewById(int paramInt) {
        return getView().findViewById(paramInt);
    }

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


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            // 注销广播监听器
            getActivity().unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }
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
}
