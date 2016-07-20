package com.wenym.grooo.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ItemShoplistBinding;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.ui.fragments.ShopListFragment;
import com.wenym.grooo.util.RxEvent.ImageEvent;
import com.wenym.grooo.util.RxJava.RxBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;

/**
 * Created by runzii on 16-7-3.
 */
public class ShopViewPagerAdapter1 extends FragmentStatePagerAdapter {


    int oldPosition = -1;
    private List<ArrayList<Shop>> items = new ArrayList<>();

    public ShopViewPagerAdapter1(FragmentManager fm) {
        super(fm);
    }

    public void setShops(List<Shop> shops) {
        items.clear();
        Observable.from(shops)
                .toMultimap(shop -> shop.getCategory(),
                        shop1 -> shop1,
                        () -> new HashMap<>()
                        , s -> new ArrayList<>())
                .subscribe(stringCollectionMap -> {
                    for (Map.Entry<String, Collection<Shop>> set : stringCollectionMap.entrySet()) {
                        items.add((ArrayList<Shop>) set.getValue());
                        notifyDataSetChanged();
                    }
                });
    }

    @Override
    public Fragment getItem(int position) {
        return ShopListFragment.newInstance(items.get(position));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).get(0).getCategory();
    }
}
