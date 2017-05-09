package net.azstudio.groooseller.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


import net.azstudio.groooseller.R;
import net.azstudio.groooseller.ui.base.BaseFragment;

import butterknife.OnClick;


public class AboutUsFragment extends BaseFragment {

    @OnClick(R.id.btn_gotoweb)
    void gotoWeb() {
        Intent intent = new Intent();
        intent.setAction("android.intent.MyActions.VIEW");
        Uri content_url = Uri.parse("http://www.grooo.cn/");
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_aboutus;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}