package com.wenym.grooo.ui.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewChildAttachStateChangeEvent;
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.runzii.lib.utils.Logs;
import com.wenym.grooo.R;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.util.RxEvent.ScrollEvent;
import com.wenym.grooo.util.RxJava.RxBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
        if (object instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) object;
            if (recyclerView.getAdapter() == null)
                recyclerView.setAdapter(new ShopListAdapter(items.get(position)));
        }
    }

    private ScrollEvent scrollEvent = new ScrollEvent(RecyclerView.SCROLL_STATE_IDLE);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("shopview","instantiateItem");
        RecyclerView v = (RecyclerView) inflater.inflate(R.layout.item_shoplist_pager, container, false);
        v.setLayoutManager(new LinearLayoutManager(container.getContext()));
        RxRecyclerView.scrollStateChanges(v)
                .subscribe(integer -> {
                    if (isScrollToBottom(integer, v))
                        scrollEvent.setState(1);
                    else if (!v.canScrollVertically(-1))
                        scrollEvent.setState(0);
                    else
                        scrollEvent.setState(integer);
                    RxBus.getDefault().post(scrollEvent);
                });
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

    private boolean isScrollToBottom(int state, RecyclerView recyclerView) {
        // 如果停止滑动
        if (state == RecyclerView.SCROLL_STATE_IDLE) {

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                // 获取布局管理器
                StaggeredGridLayoutManager layout =
                        (StaggeredGridLayoutManager) layoutManager;
                // 用来记录lastItem的position
                // 由于瀑布流有多个列 所以此处用数组存储
                int column = layout.getColumnCountForAccessibility(null, null);
                int positions[] = new int[column];
                // 获取lastItem的positions
                layout.findLastVisibleItemPositions(positions);
                for (int i = 0; i < positions.length; i++) {
                    /**
                     * 判断lastItem的底边到recyclerView顶部的距离
                     * 是否小于recyclerView的高度
                     * 如果小于或等于 说明滚动到了底部
                     */
                    // 刚才忘了写判断是否是最后一个item了
                    if (positions[i] >= (layout.getItemCount() - column)
                            && layout.findViewByPosition(positions[i]).getBottom() <= recyclerView.getHeight()) {
                        return true;
                    }

                }
            } else if (layoutManager instanceof LinearLayoutManager) {

                LinearLayoutManager layout =
                        (LinearLayoutManager) layoutManager;
                int position = layout.findLastVisibleItemPosition();
                // 刚才忘了写判断是否是最后一个item了
                if (position == layout.getItemCount() - 1
                        && layout.findViewByPosition(position).getBottom() <= recyclerView.getHeight()) {
                    return true;
                }

            }

        }
        return false;
    }
}
