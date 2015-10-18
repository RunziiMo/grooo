package com.wenym.grooo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {

    WebView webView;
    WebViewClient webViewClient;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.url = getArguments().getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        webView = new WebView(getActivity());
        webView.setWebChromeClient(new WebChromeClient());
        webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        webView.setLayoutParams(new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        webView.loadUrl(url);
        return webView;
    }

}
