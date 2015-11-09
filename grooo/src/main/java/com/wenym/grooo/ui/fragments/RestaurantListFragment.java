package com.wenym.grooo.ui.fragments;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.RestaurantListAdapter;
import com.wenym.grooo.model.ecnomy.Restaurant;
import com.wenym.grooo.provider.ExtraArgumentKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RestaurantListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Restaurant> mContentItems = new ArrayList<Restaurant>();

    public static RestaurantListFragment newInstance(String restaurant) {
        RestaurantListFragment restaurantListFragment = new RestaurantListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ExtraArgumentKeys.SHOPS.toString(), restaurant);
        restaurantListFragment.setArguments(bundle);
        return restaurantListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String shops_string = getArguments().getString(ExtraArgumentKeys.SHOPS.toString(), "");
        List<Restaurant> shops = new Gson().fromJson(shops_string, new TypeToken<List<Restaurant>>() {
        }.getType());
        mContentItems.addAll(shops);
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


        mAdapter = new RecyclerViewMaterialAdapter(new RestaurantListAdapter(mContentItems));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }


}
