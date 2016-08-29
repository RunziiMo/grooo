package com.wenym.grooo.ui.activity;

import android.os.Bundle;

import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.ui.fragments.WebViewFragment;

/**
 * Created by Wouldyou on 2015/6/29.
 */
public class WebViewActivity extends BaseActivity {

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
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        link = getIntent().getStringExtra("link");
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", link);
        webViewFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, webViewFragment).commit();
    }
}
