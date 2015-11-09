package com.wenym.grooo.ui.activities;

import android.os.Bundle;

import com.wenym.grooo.R;
import com.wenym.grooo.ui.fragments.FetchKuaidiFragment;
import com.wenym.grooo.widgets.BaseActivity;

/**
 * Created by Wouldyou on 2015/6/29.
 */
public class FecthKuaidiActivity extends BaseActivity {

    private String link;

    @Override
    protected boolean isHideNavigationBar() {
        return false;
    }

    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new FetchKuaidiFragment()).commit();
        hideSoftKeyboard();
    }
}
