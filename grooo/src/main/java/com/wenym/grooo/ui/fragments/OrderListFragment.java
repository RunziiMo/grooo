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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.OrderListAdapter;
import com.wenym.grooo.http.model.GetOrderData;
import com.wenym.grooo.http.model.GetOrderSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.ecnomy.DeliveryOrder;
import com.wenym.grooo.model.ecnomy.FoodOrder;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.widgets.Toasts;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class OrderListFragment extends BaseFragment {

    private static final String ARG_POSITION = "position";

    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.tv_emptyview)
    TextView emptyView;
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
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getDate();
    }

    private void getDate() {
        if (GroooAppManager.getTakeouts() == null) {
            mlist = GroooAppManager.getTakeouts();
            mlistKuadi = GroooAppManager.getDeliveries();
            GroooAppManager.initOrders(getActivity());
            setAdapter();
        } else {
            HttpUtils.MakeAPICall(new GetOrderData(), getActivity(), new HttpCallBack() {
                @Override
                public void onSuccess(Object object) {
                    GetOrderSuccessData orderSuccessData = (GetOrderSuccessData) object;
                    mlist = orderSuccessData.getRestaurant();
                    mlistKuadi = orderSuccessData.getDelivery();
                    setAdapter();
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

    private void setAdapter() {
        if (position == 0) {
            if (mlist.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
            mAdapter = new OrderListAdapter(getActivity(), mlist);
            ((OrderListAdapter) mAdapter)
                    .setMode(Attributes.Mode.Single);
            SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(
                    mAdapter);
            mRecyclerView
                    .setAdapter(animationAdapter);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}