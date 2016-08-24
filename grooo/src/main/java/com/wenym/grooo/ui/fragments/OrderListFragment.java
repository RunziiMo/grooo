/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wenym.grooo.ui.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentRecyclerviewBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Order;
import com.wenym.grooo.ui.adapters.FoodOrderAdapter;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.RxJava.RxNetWorking;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;


public class OrderListFragment extends BaseFragment<FragmentRecyclerviewBinding> {


    private List<Order> mlist = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;

    private Observable<List<Order>> orderOb;

    public static OrderListFragment newInstance() {
        OrderListFragment f = new OrderListFragment();
        return f;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderOb = NetworkWrapper.get().getOrders()
                .compose(RxNetWorking.bindRefreshing(bind.swipeRefreshLayout));
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bind.recyclerView.setEmptyView(bind.tvEmptyview);
        Subscription rx = RxSwipeRefreshLayout.refreshes(bind.swipeRefreshLayout)
                .subscribe(aVoid -> {
                    loadOrder();
                }, errorHandle("获取所有订单"));
        addSubscription(rx);
        setAdapter();
        loadOrder();
    }


    private void loadOrder() {
        Subscription s = orderOb.
                compose(RxNetWorking.bindRefreshing(bind.swipeRefreshLayout))
                .subscribe(orders -> {
                    mlist.clear();
                    mlist.addAll(orders);
                    mAdapter.notifyDataSetChanged();
                }, errorHandle("加载所有订单"));
        addSubscription(s);
    }


    private void setAdapter() {
        mAdapter = new FoodOrderAdapter(getActivity(), mlist);
        bind.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(bind.toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.label_order_list);
        }
    }
}