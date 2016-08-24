package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityShopdetailBinding;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.model.app.Basket;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.ui.fragments.ShopMenuFragment;
import com.wenym.grooo.util.RxEvent.FoodEvent;
import com.wenym.grooo.util.RxJava.RxBus;
import com.wenym.grooo.util.SmallTools;
import com.wenym.grooo.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends BaseActivity<ActivityShopdetailBinding> {

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
        return R.layout.activity_shopdetail;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bind.setShop(SmallTools.fromGson(getIntent().getStringExtra("shopid"), Shop.class));
        Basket.INSTANCE.init(bind.getShop());

        bind.setBasket(Basket.INSTANCE);

        bottomSheetBehavior = BottomSheetBehavior.from(bind.basketForm);

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

//    @Override
//    public void onPanelSlide(View panel, float slideOffset) {
//        final View background = panel.findViewById(R.id.basket_info);
//        int before = currBasketColor;
//        int after = 0;
//        if (slideOffset > 0f) {
//            after = getResources().getColor(R.color.colorPrimary);
//            if (currBasketColor == after) {
//                return;
//            }
//            ((TextView) getBasket().findViewById(R.id.basket_costname)).setTextColor(Color.WHITE);
//            ((TextView) getBasket().findViewById(R.id.basket_cost)).setTextColor(Color.WHITE);
//            basketIcon.setImageDrawable(new IconicsDrawable(RestaurantDetailActivity.this
//                    , GoogleMaterial.Icon.gmd_keyboard_arrow_down).color(Color.WHITE).sizeDp(36));
//        } else {
//            after = getResources().getColor(R.color.white);
//            ((TextView) getBasket().findViewById(R.id.basket_costname)).setTextColor(Color.BLACK);
//            ((TextView) getBasket().findViewById(R.id.basket_cost)).setTextColor(Color.BLACK);
//            basketIcon.setImageDrawable(new IconicsDrawable(RestaurantDetailActivity.this
//                    , GoogleMaterial.Icon.gmd_keyboard_arrow_up).color(Color.BLACK).sizeDp(36));
//        }
//        ObjectAnimator colorAnim = ObjectAnimator.ofInt(background, "backgroundColor", before, after);
//        colorAnim.setEvaluator(new ArgbEvaluator());
//        colorAnim.setDuration((long) 100);
//        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int colorAlpha = Utils.colorWithAlpha(((Integer) animation.getAnimatedValue()).intValue(), 0f);
//                background.setBackgroundColor(colorAlpha);
//            }
//        });
//        colorAnim.start();
//        currBasketColor = after;
//    }

    public void confirmOrder(View view) {

        Intent intent = new Intent(RestaurantDetailActivity.this
                , ConfirmOrderActivity.class);
        startActivityForResult(intent, REQUESTCODE_CONFIRMPAY);
    }

    public void clearOrder(View view) {

        Basket.INSTANCE.init(bind.getShop());

        bind.setBasket(Basket.INSTANCE);

        if (menu_fragment != null) {
            menu_fragment.getListAdpter().notifyDataSetChanged();
        }
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
