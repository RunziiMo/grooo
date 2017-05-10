package net.azstudio.groooseller.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.FragmentAboutusBinding;
import net.azstudio.groooseller.ui.base.BaseFragment;


public class AboutUsFragment extends BaseFragment<FragmentAboutusBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_aboutus;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnGotoweb.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.MyActions.VIEW");
            Uri content_url = Uri.parse("http://www.grooo.cn/");
            intent.setData(content_url);
            startActivity(intent);
        });
    }
}