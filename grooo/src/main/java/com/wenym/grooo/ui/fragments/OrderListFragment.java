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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.DeliveryOrderAdapter;
import com.wenym.grooo.adapters.FoodOrderAdapter;
import com.wenym.grooo.model.ecnomy.DeliveryOrder;
import com.wenym.grooo.model.ecnomy.FoodOrder;
import com.wenym.grooo.provider.ExtraArgumentKeys;
import com.wenym.grooo.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class OrderListFragment extends BaseFragment {


    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.tv_emptyview)
    TextView emptyView;
    private List<FoodOrder> mlist = null;
    private List<DeliveryOrder> mlistDelivery = null;
    @SuppressWarnings("rawtypes")
    private RecyclerView.Adapter mAdapter;


    public static OrderListFragment newInstanceFood(List<FoodOrder> objects) {
        OrderListFragment f = new OrderListFragment();
        Bundle b = new Bundle();
        b.putBoolean(ExtraArgumentKeys.ISFOODORDER.toString(), true);
        b.putString(ExtraArgumentKeys.ORDERS.toString(), new Gson().toJson(objects));
        f.setArguments(b);
        return f;
    }

    public static OrderListFragment newInstanceDelivery(List<DeliveryOrder> objects) {
        OrderListFragment f = new OrderListFragment();
        Bundle b = new Bundle();
        b.putBoolean(ExtraArgumentKeys.ISFOODORDER.toString(), false);
        b.putString(ExtraArgumentKeys.ORDERS.toString(), new Gson().toJson(objects));
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String listStr = getArguments().getString(ExtraArgumentKeys.ORDERS.toString());
        if (getArguments().getBoolean(ExtraArgumentKeys.ISFOODORDER.toString())) {
            mlist = new Gson().fromJson(listStr, new TypeToken<ArrayList<FoodOrder>>() {
            }.getType());
        } else {
            mlistDelivery = new Gson().fromJson(listStr, new TypeToken<ArrayList<DeliveryOrder>>() {
            }.getType());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();
    }


    private void setAdapter() {
        if (mlist != null) {
            if (mlist.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
            mAdapter = new FoodOrderAdapter(getActivity(), mlist);
            ((FoodOrderAdapter) mAdapter)
                    .setMode(Attributes.Mode.Single);
            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(
                    mAdapter);
            mRecyclerView
                    .setAdapter(animationAdapter);
        } else {
            if (mlistDelivery.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
            mAdapter = new DeliveryOrderAdapter(getActivity(), mlistDelivery);
            ((DeliveryOrderAdapter) mAdapter)
                    .setMode(Attributes.Mode.Single);
            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(
                    mAdapter);
            mRecyclerView
                    .setAdapter(animationAdapter);
        }
    }
}