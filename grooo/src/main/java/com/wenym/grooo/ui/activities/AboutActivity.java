package com.wenym.grooo.ui.activities;

import android.os.Bundle;

import com.wenym.grooo.R;
import com.wenym.grooo.ui.fragments.AboutUsFragment;
import com.wenym.grooo.widgets.BaseActivity;

/**
 * Created by runzii on 15-11-1.
 */
public class AboutActivity extends BaseActivity{

    @Override
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected boolean isHideNavigationBar() {
        return false;
    }

    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new AboutUsFragment()).commit();
    }
}
