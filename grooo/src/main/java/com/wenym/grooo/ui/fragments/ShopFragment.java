package com.wenym.grooo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentShoplistBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.provider.ImageBacks;
import com.wenym.grooo.ui.adapters.ShopViewPagerAdapter;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.GroooAppManager;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscription;

public class ShopFragment extends BaseFragment<FragmentShoplistBinding> {


    private Observable<List<Shop>> shopObservable;

    private ShopViewPagerAdapter pagerAdapter;

    public static ShopFragment newInstance() {
        ShopFragment shopFragment = new ShopFragment();
        return shopFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shoplist;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopObservable = NetworkWrapper.get()
                .getAllShop(GroooAppManager.getProfile().getSchool().getId());
//                .compose(RxNetWorking.bindRefreshing(bind.swipeRefreshLayout));
//        Subscription rx = RxSwipeRefreshLayout.refreshes(bind.swipeRefreshLayout)
//                .subscribe(aVoid -> {
//                    loadShop();
//                }, errorHandle());
//        addSubscription(rx);
        setUpViewPager();
        view.post(() -> loadShop());
        loadBackDrop();
    }

    private void setUpViewPager() {
        pagerAdapter = new ShopViewPagerAdapter(getFragmentManager());
//        bind.toolbar.setTitle("世界那么大，你想吃什么");
//        bind.toolbar.setNavigationIcon(null);
        bind.pager.setAdapter(pagerAdapter);
        bind.pager.setOffscreenPageLimit(2);
        Random random = new Random();
        int color = 0xff000000 | random.nextInt(0x00ffffff);
//        bind.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                bind.swipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
//            }
//        });
        bind.tabs.setupWithViewPager(bind.pager);
    }

    private void loadBackDrop(){
        Glide.with(getActivity()).load(ImageBacks.getOne()).into(bind.backDrop);
    }

    private void loadShop() {
        Subscription s = shopObservable
                .subscribe(shops -> pagerAdapter.setShops(shops)
                        , errorHandle("获取所有商家"));
        addSubscription(s);
    }

}
