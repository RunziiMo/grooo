package com.wenym.grooo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.ui.fragments.AboutUsFragment;
import com.wenym.grooo.ui.fragments.RegisterFragment;

/**
 * Created by runzii on 15-11-9.
 */
public class MyFragmentActivity extends BaseActivity {

    public final static int about = 1, suggest = 2, delivery = 3, regist = 4;

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
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frame_container);

        if (fragment == null) {
            fragment = createFragment();

            fm.beginTransaction().add(R.id.frame_container, fragment).commit();
        }
    }

    private Fragment createFragment() {
        Fragment fragment = null;
        switch (getIntent().getIntExtra("fragment", 0)) {
            case 1:
                fragment = new AboutUsFragment();
                break;
            case 4:
                fragment = new RegisterFragment();
                getSupportActionBar().setTitle(R.string.activitylabel_regist);
                break;
            default:
                fragment = new AboutUsFragment();
                break;
        }
        return fragment;
    }
}
