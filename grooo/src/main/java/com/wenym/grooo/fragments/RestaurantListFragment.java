package com.wenym.grooo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.RestaurantListAdapter;
import com.wenym.grooo.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RestaurantListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private boolean isWaiMai;

    private List<Restaurant> mContentItems = new ArrayList<Restaurant>();

    public static RestaurantListFragment newInstance() {
        RestaurantListFragment restaurantListFragment = new RestaurantListFragment();
        return restaurantListFragment;
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


        mAdapter = new RecyclerViewMaterialAdapter(new RestaurantListAdapter(mContentItems, isWaiMai));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    public RestaurantListFragment setRestaurants(ArrayList<Restaurant> restaurants) {
        mContentItems.addAll(restaurants);
        return this;
    }

    public RestaurantListFragment setIsWaiMai(boolean isWaiMai) {
        this.isWaiMai = isWaiMai;
        return this;
    }


}
