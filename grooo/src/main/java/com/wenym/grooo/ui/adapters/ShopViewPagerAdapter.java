package com.wenym.grooo.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.runzii.lib.utils.Logs;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.ui.fragments.ShopListFragment;
import com.wenym.grooo.util.Toasts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by runzii on 16-7-3.
 */
public class ShopViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ArrayList<Shop>> items = new ArrayList<>();

    public ShopViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setShops(List<Shop> shops) {
        items = new ArrayList<>();
        Observable.from(shops)
                .toMultimap(shop -> shop.getCategory(),
                        shop1 -> shop1,
                        () -> new HashMap<>()
                        , s -> new ArrayList<>())
                .subscribe(stringCollectionMap -> {
                    for (Map.Entry<String, Collection<Shop>> set : stringCollectionMap.entrySet()) {
                        items.add((ArrayList<Shop>) set.getValue());
                        if (items.size() == 3) {
                            notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public Fragment getItem(int position) {
        return ShopListFragment.newInstance(items.get(position));
    }


    /**
     * 返回该值保证调用notifyDataSetChanged时强制刷新
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
