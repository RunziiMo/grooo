package com.wenym.grooo.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wenym.grooo.R;
import com.wenym.grooo.ui.activities.WebViewActivity;
import com.wenym.grooo.model.app.New;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class NewsFragment extends Fragment {

    private static final String ARG_POSTION = "position";
    private static final String ARG_COLOR = "color";
    private static final String ARG_NEW = "new";

    private int mPosition;
    private int mColor;
    private ImageView view;
    private TextView viewText;
    private New mNew;

    public static NewsFragment newInstance(int mPosition, int mColor, New mNew) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSTION, mPosition);
        bundle.putInt(ARG_COLOR, mColor);
        bundle.putString(ARG_NEW, new Gson().toJson(mNew));
        newsFragment.setArguments(bundle);
        return newsFragment;
    }


    public static boolean checkURL(String url) {
        boolean value = false;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            int code = conn.getResponseCode();
            value = code == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSTION);
            mColor = getArguments().getInt(ARG_COLOR);
            mNew = new Gson().fromJson(getArguments().getString(ARG_NEW), New.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.newstoday_fragment, container,
                false);
        view = (ImageView) v.findViewById(R.id.news);
        viewText = (TextView) v.findViewById(R.id.newsText);
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                if (checkURL(mNew.getLink())) {
//                    v.setOnClickListener(new OnClickListener() {
//
//                        @SuppressWarnings({"rawtypes", "unchecked"})
//                        @Override
//                        public void onClick(View v) {
//
////                            getActivity().getActionBar().setTitle(mNew.getTitle());
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.frame_container, new WebViewFragment(mNew.getLink()))
//                                    .addToBackStack("home").commit();
//                        }
//                    });
//                }
//            }
//        }).start();
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNew.getLink().contains("http")) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("link", mNew.getLink());
                    intent.putExtra("title", mNew.getTitle());
                    startActivity(intent);
                }
            }
        });

        if (mNew.getContent().contains("http")
                || mNew.getContent().contains(
                "android.resource")) {
            String[] matches = mNew.getContent().split("'.*'");
            for (int i = 0; i < matches.length; i++) {
                mNew.setContent(mNew.getContent().replace(matches[i], ""));
            }
            mNew.setContent(mNew.getContent().replaceAll("'", ""));
            Picasso.with(getActivity()).load(mNew.getContent()).centerInside().fit().into(view);
            view.setBackgroundColor(TextUtils.isEmpty(mNew.getBackground_color()) ? mColor
                    : Color.parseColor(mNew.getBackground_color()));
        } else {
            view.setVisibility(View.INVISIBLE);
            viewText.setVisibility(View.VISIBLE);
            String string = mNew.getContent()
                    .replace("<br>", "\n");
            viewText.setGravity(Gravity.CENTER);
            viewText.setText(string);
            viewText.setBackgroundColor(TextUtils.isEmpty(mNew.getBackground_color()) ? mColor
                    : Color.parseColor(mNew.getBackground_color()));
        }
        return v;
    }
}