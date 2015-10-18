package com.wenym.grooo;

import android.os.Bundle;

import com.wenym.grooo.fragments.WebViewFragment;
import com.wenym.grooo.widgets.BaseActivity;

/**
 * Created by Wouldyou on 2015/6/29.
 */
public class WebViewActivity extends BaseActivity {

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
