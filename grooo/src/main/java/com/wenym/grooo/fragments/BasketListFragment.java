package com.wenym.grooo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.PayListAdapter;
import com.wenym.grooo.model.ShoppingBasket;

@SuppressWarnings("deprecation")
public class BasketListFragment extends Fragment {

    public static String what = "";
    private RelativeLayout payLayout;
    private Button payButton;
    private EditText building, room, remark, yudingtime;
    private boolean isWaiMai;
    private String shopid;
    private ShoppingBasket basket;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;


    public static BasketListFragment newInstance() {
        BasketListFragment fragment = new BasketListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new PayListAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }
}
