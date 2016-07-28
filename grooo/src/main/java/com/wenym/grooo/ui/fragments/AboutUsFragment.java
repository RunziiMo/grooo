package com.wenym.grooo.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenym.grooo.R;

public class AboutUsFragment extends Fragment {

    private TextView gotoWeb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.fragment_aboutus, container, false);
        gotoWeb = (TextView) v.findViewById(R.id.btn_gotoweb);
        gotoWeb.setOnClickListener(v1 -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("http://www.grooo.cn/");
            intent.setData(content_url);
            startActivity(intent);
        });
        return v;
    }
}
