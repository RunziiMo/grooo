package com.wenym.grooo.ui.shop;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jakewharton.rxbinding.support.design.widget.RxAppBarLayout;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityShopBinding;
import com.wenym.grooo.model.app.Basket;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.ui.activities.ConfirmOrderActivity;
import com.wenym.grooo.util.RxEvent.FoodEvent;
import com.wenym.grooo.util.RxJava.RxBus;
import com.wenym.grooo.util.SmallTools;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends BaseActivity<ActivityShopBinding> {

    public static final int REQUESTCODE_CONFIRMPAY = 1;

    private ShopMenuFragment menu_fragment;


    BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bind.setShop(getIntent().getParcelableExtra("shopId"));
        Basket.INSTANCE.init(bind.getShop());

        bind.setBasket(Basket.INSTANCE);

        bottomSheetBehavior = BottomSheetBehavior.from(bind.basketForm);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d("BottomSheetCallback", "newState " + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("BottomSheetCallback", "slideOffset " + slideOffset);
                if (slideOffset > 0.05f) {
                    bind.fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    bind.fab.setIma
                }else {
                    bind.fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        bind.basketList.setLayoutManager(layoutManager);
        bind.basketList.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
        bind.basketList.setHasFixedSize(true);


        setupViewPager(bind.viewpager, bind.getShop().getId());

        RxBus.getDefault().toObserverable(FoodEvent.class)
                .compose(bindToLifecycle())
                .subscribe(foodEvent -> {
                    bind.basketList.getAdapter().notifyDataSetChanged();
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_CONFIRMPAY) {
                Toasts.show(data.getStringExtra(ExtraActivityKeys.PAYINFO.toString()));
                toggleSlidingUpLayout(BottomSheetBehavior.STATE_COLLAPSED);
                finish();
            }
        }
    }


    private void setupViewPager(ViewPager viewPager, int shopId) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        menu_fragment = ShopMenuFragment.newInstance(shopId);
        adapter.addFragment(menu_fragment, "菜单");
        viewPager.setAdapter(adapter);
    }

    public View getBasket() {
        return findViewById(R.id.basket_info);
    }

    public void toggleSlidingUpLayout(int job) {
        if (bottomSheetBehavior.getState() != job)
            bottomSheetBehavior.setState(job);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_restaurant_detail, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem isFavorite = menu.findItem(R.id.action_favorite);
//        MenuItem isFavorited = menu.findItem(R.id.action_favorited);
//        if (AppPreferences.get().getFavoriteShops()
//                .contains(restaurant.getId())) {
//            isFavorite.setVisible(false);
//        } else {
//            isFavorited.setVisible(false);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Set<String> favorites = AppPreferences.get().getFavoriteShops();
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//            case R.id.action_favorite:
//                favorites.add(restaurant.getId());
//                break;
//            case R.id.action_favorited:
//                favorites.remove(restaurant.getId());
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        AppPreferences.get().setFavoriteShops(favorites);
//        supportInvalidateOptionsMenu();
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED
                || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    public void clearOrder(View view) {

        Basket.INSTANCE.init(bind.getShop());

        bind.setBasket(Basket.INSTANCE);

        if (menu_fragment != null) {
            menu_fragment.getListAdpter().notifyDataSetChanged();
        }
    }

    public void goPay(View view) {
        Intent intent = new Intent(ShopActivity.this
                , ConfirmOrderActivity.class);
        startActivityForResult(intent, REQUESTCODE_CONFIRMPAY);
    }

    public void showBottomOrHide(View view) {
        if (bottomSheetBehavior == null)
            return;
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
            toggleSlidingUpLayout(BottomSheetBehavior.STATE_EXPANDED);
        else
            toggleSlidingUpLayout(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void showBottomOrBuy(View view) {
        if (bottomSheetBehavior == null)
            return;
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
            toggleSlidingUpLayout(BottomSheetBehavior.STATE_EXPANDED);
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
