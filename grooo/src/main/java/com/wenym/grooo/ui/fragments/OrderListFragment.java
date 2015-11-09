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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.util.Attributes;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.OrderListAdapter;
import com.wenym.grooo.http.model.GetOrderData;
import com.wenym.grooo.http.model.GetOrderSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.DeliveryOrder;
import com.wenym.grooo.model.FoodOrder;
import com.wenym.grooo.widgets.Toasts;

import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class OrderListFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private View currentView;
    private RecyclerView mRecyclerView;
    private List<FoodOrder> mlist;
    private List<DeliveryOrder> mlistKuadi;
    @SuppressWarnings("rawtypes")
    private RecyclerView.Adapter mAdapter;


    private int position;

    public static OrderListFragment newInstance(int position) {
        OrderListFragment f = new OrderListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_recyclerview,
                container, false);
        ViewCompat.setElevation(currentView, 30);
        mRecyclerView = (RecyclerView) currentView
                .findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getDate();

        return currentView;
    }

    private void getDate() {

        HttpUtils.MakeAPICall(new GetOrderData(), getActivity(), new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                GetOrderSuccessData orderSuccessData = (GetOrderSuccessData) object;
                mlist = orderSuccessData.getRestaurant();
                mlistKuadi = orderSuccessData.getDelivery();
                if (position == 0) {
                    mAdapter = new OrderListAdapter(getActivity(), mlist);
                    ((OrderListAdapter) mAdapter)
                            .setMode(Attributes.Mode.Single);
                    SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(
                            mAdapter);
                    mRecyclerView
                            .setAdapter(animationAdapter);
                }
            }

            @Override
            public void onFailed() {
            }

            @Override
            public void onError(int statusCode) {
                Toasts.show(String.valueOf(statusCode));
            }
        });
        // AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(
        // mAdapter);
    }
}