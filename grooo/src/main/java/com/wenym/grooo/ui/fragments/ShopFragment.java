package com.wenym.grooo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentShoplistBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.ui.adapters.ShopViewPagerAdapter;
import com.wenym.grooo.ui.adapters.ShopViewPagerAdapter1;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.GroooAppManager;
import com.wenym.grooo.util.RxEvent.ImageEvent;
import com.wenym.grooo.util.RxJava.RxBus;
import com.wenym.grooo.util.RxJava.RxNetWorking;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscription;

public class ShopFragment extends BaseFragment<FragmentShoplistBinding> {


    private Observable<List<Shop>> shopObservable;

    private ShopViewPagerAdapter1 pagerAdapter;

    public static ShopFragment newInstance() {
        ShopFragment restaurantListFragment = new ShopFragment();
        return restaurantListFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shoplist;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopObservable = NetworkWrapper.get()
                .getAllShop(GroooAppManager.getProfile().getSchool().getId())
                .compose(RxNetWorking.bindRefreshing(bind.swipeRefreshLayout));
        pagerAdapter = new ShopViewPagerAdapter1(getFragmentManager());
        Subscription rx = RxSwipeRefreshLayout.refreshes(bind.swipeRefreshLayout)
                .subscribe(aVoid -> {
                    loadShop();
                }, errorHandle());
        Subscription s = RxBus.getDefault().toObserverable(ImageEvent.class)
                .subscribe(imageEvent -> {
                    final int fadeDuration = 400;
                    bind.materialViewPager.setImageUrl(imageEvent.getImageUrl(), fadeDuration);
                    bind.materialViewPager.setColor(imageEvent.getColor(), fadeDuration);
                }, errorHandle());
        addSubscription(s);
        addSubscription(rx);
        setUpViewPager();
    }

    private void setUpViewPager() {
        Toolbar toolbar = bind.materialViewPager.getToolbar();
        toolbar.setTitle("商店");
        toolbar.setNavigationIcon(null);
        bind.materialViewPager.getViewPager().setAdapter(pagerAdapter);
        bind.materialViewPager.getPagerTitleStrip().setViewPager(bind.materialViewPager.getViewPager());
        bind.materialViewPager.getViewPager().setCurrentItem(0);
        bind.materialViewPager.setMaterialViewPagerListener(page -> {
            Random random = new Random();
            int color = 0xff000000 | random.nextInt(0x00ffffff);
            String imageUrl = "";

            switch (random.nextInt(10)) {
                case 0:
                    imageUrl = "http://www.cnwest88.com/uploadfile/2012/0509/20120509094628310.jpg";
                    break;
                case 1:
                    imageUrl = "http://www.izmzg.com/empirecms/d/file/zmzg/beijing/jingdian/2012-03-16/a60ca4bef8ab6c4343bc486a548610ea.jpg";
                    break;
                case 2:
                    imageUrl = "http://img.kpkpw.com/201106/27/14696_1309186930X2Mt.jpg";
                    break;
                case 3:
                    imageUrl = "http://img1.58.com/groupbuy/n_s12275644173422709133%2120811748818944.jpg";
                    break;
                case 4:
                    imageUrl = "http://www.nmghuana.com/UploadFiles/2014-03/nmghuana/2014032610591940146.jpg";
                    break;
                case 5:
                    imageUrl = "http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1107/30/c0/8490657_8490657_1311991396343.jpg";
                    break;
                case 6:
                    imageUrl = "http://www.ctps.cn/PhotoNet/Profiles/BLUE%E6%9D%B0/2009326130172166.jpg";
                    break;
                case 7:
                    imageUrl = "http://f.hiphotos.bdimg.com/album/w%3D2048/sign=f28844ce7e3e6709be0042ff0fff9e3d/962bd40735fae6cd2e8713880eb30f2442a70f23.jpg";
                    break;
                case 8:
                    imageUrl = "http://www.cdtianya.com/baike/uploads/201303/1363830542ySdkzSVt.jpg";
                    break;
                case 9:
                    imageUrl = "http://c.hiphotos.bdimg.com/album/w%3D2048/sign=e14035a13812b31bc76cca29b220347a/63d0f703918fa0ec9a37cdeb279759ee3c6ddb60.jpg";
                    break;
            }
            return HeaderDesign.fromColorAndUrl(color, imageUrl);
        });
        bind.materialViewPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                bind.swipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
    }

    private void loadShop() {
        Subscription s = shopObservable
                .subscribe(shops -> pagerAdapter.setShops(shops)
                        , throwable -> throwable.printStackTrace());
        addSubscription(s);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadShop();
    }
}
