package com.wenym.grooo;

import android.os.Bundle;

import com.wenym.grooo.fragments.FetchKuaidiFragment;
import com.wenym.grooo.widgets.BaseActivity;

/**
 * Created by Wouldyou on 2015/6/29.
 */
public class FecthKuaidiActivity extends BaseActivity {

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new FetchKuaidiFragment()).commit();
    }
}
