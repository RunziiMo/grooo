package com.wenym.grooo.ui.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runzii.lib.utils.Logs;
import com.wenym.grooo.R;
import com.wenym.grooo.model.app.Shop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by runzii on 16-7-3.
 */
public class ShopViewPagerAdapter extends PagerAdapter {

    private List<ArrayList<Shop>> items = new ArrayList<>();

    private LayoutInflater inflater;

    public ShopViewPagerAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setShops(List<Shop> shops) {
        items = new ArrayList<>();
//        for (Shop shop : shops) {
//            boolean added = false;
//            for (ArrayList<Shop> list : items)
//                if (list.get(0).getCategory().equals(shop.getCategory())) {
//                    list.add(shop);
//                    added = true;
//                }
//            if (!added) {
//                ArrayList<Shop> addedList = new ArrayList<>();
//                addedList.add(shop);
//                items.add(addedList);
//            }
//        }
//        notifyDataSetChanged();
        Logs.d("notifyDataSetChanged");
        Observable.from(shops)
                .observeOn(AndroidSchedulers.mainThread())
                .toMultimap(shop -> shop.getCategory(),
                        shop1 -> shop1,
                        () -> new HashMap<>()
                        , s -> new ArrayList<>())
                .subscribe(stringCollectionMap -> {
                    for (Map.Entry<String, Collection<Shop>> set : stringCollectionMap.entrySet()) {
                        items.add((ArrayList<Shop>) set.getValue());
                    }
                    notifyDataSetChanged();
                });
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Log.d("ShopViewPagerAdapter", "setPrimaryItem");
        if (object instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) object;
            if (recyclerView.getAdapter() == null)
                recyclerView.setAdapter(new ShopListAdapter(items.get(position)));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView v = (RecyclerView) inflater.inflate(R.layout.item_shoplist_pager, container, false);
        v.setLayoutManager(new LinearLayoutManager(container.getContext()));
        container.addView(v);
        return v;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).get(0).getCategory();
    }
}
