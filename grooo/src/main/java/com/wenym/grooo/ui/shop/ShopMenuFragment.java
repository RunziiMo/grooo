/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.wenym.grooo.ui.shop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.runzii.lib.widgets.behavior.BottomSheetAnchorBehavior;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentShopMenuBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Food;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.RxEvent.ClearEvent;
import com.wenym.grooo.util.RxJava.RxBus;
import com.wenym.grooo.util.RxJava.RxNetWorking;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.util.Tools;
import com.wenym.grooo.widgets.BadgeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ShopMenuFragment extends BaseFragment<FragmentShopMenuBinding> implements StickyListHeadersListView.OnStickyHeaderChangedListener {


    private ArrayList<String> stickers;
    private ArrayList<Food> foods;
    private ArrayList<String> stickers_left;
    private ViewGroup anim_mask_layout;//动画层
    private View basketBar;//显示数量的图片
    private MenuListViewAdapter contentAdapter;
    private BaseAdapter headAdapter;

    private int shopId;

    private Observable<List<Food>> foodObservable;

    public static ShopMenuFragment newInstance(int shopId) {
        ShopMenuFragment fragment = new ShopMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("shopId", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MenuListViewAdapter getListAdpter() {
        return contentAdapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop_menu;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        shopId = getArguments().getInt("shopId");
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodObservable = NetworkWrapper.get().getShopFood(shopId)
                .compose(RxNetWorking.bindProgress(bind.menuProgress))
                .compose(bindToLifecycle())
                .flatMap(Observable::from)
                .groupBy(Food::getCategory)
                .flatMap(Observable::toList);
        loadMenu();
        setupView();

        RxBus.getDefault().toObserverable(ClearEvent.class)
                .compose(bindToLifecycle())
                .subscribe(clearEvent -> {
                    if (contentAdapter != null) contentAdapter.clearNumb();
                });
    }

    private void setupView() {
        contentAdapter = new MenuListViewAdapter(foods, stickers);
        headAdapter = new HeaderListViewAdapter(stickers_left);
        bind.foodListContentList.setAdapter(contentAdapter);
        bind.foodListContentList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("onScrollStateChanged", "scrollState " + scrollState);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == (view.getCount() - 1))
                        ((ShopActivity) getActivity()).toggleSlidingUpLayout(BottomSheetAnchorBehavior.STATE_HIDDEN);
                    else
                        ((ShopActivity) getActivity()).toggleSlidingUpLayout(BottomSheetAnchorBehavior.STATE_COLLAPSED);
                } else
                    ((ShopActivity) getActivity()).toggleSlidingUpLayout(BottomSheetAnchorBehavior.STATE_HIDDEN);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        bind.foodListContentList.setOnStickyHeaderChangedListener(this);
        bind.foodListHeaderList.setAdapter(headAdapter);
        bind.foodListHeaderList.setOnItemClickListener((parent, view, position, id) -> bind.foodListContentList.setSelection(stickers.indexOf(stickers_left.get(position))));
    }

    private void loadMenu() {
        foods = new ArrayList<>();
        stickers = new ArrayList<>();
        stickers_left = new ArrayList<>();
        foodObservable
                .subscribe(foods1 -> {
                    foods.addAll(foods1);
                    stickers_left.add(foods1.get(0).getCategory());
                    for (int i = 0; i < foods1.size(); i++) {
                        stickers.add(foods1.get(i).getCategory());
                    }
                    basketBar = ((ShopActivity) getActivity()).getBasket();
                }, throwable -> Toasts.show(throwable.getMessage()), () -> {
                    contentAdapter.notifyDataSetChanged();
                    headAdapter.notifyDataSetChanged();
                });
    }


    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        bind.foodListHeaderList.setItemChecked(stickers_left.indexOf(stickers.get(itemPosition)), true);
    }


    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    /**
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getActivity().getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    private void setAnim(final View v, int[] start_location) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);// 把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v,
                start_location);
        int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
        basketBar.findViewById(R.id.basket_cost).getLocationInWindow(end_location);// shopCart是那个购物车

        // 计算位移
        int endX = end_location[0] - start_location[0] + 60;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                // buyNumView.setText(buyNum + "");//
                // buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                // buyNumView.show();
            }
        });

    }

    public Bitmap getAddDrawBitMap(int position) {
        Tools tools = new Tools();
        BadgeView add = new BadgeView(getActivity());
        add.setText(String.valueOf(foods.get(position).getName()));
        return tools.convertViewToBitmap(add);
    }
}
