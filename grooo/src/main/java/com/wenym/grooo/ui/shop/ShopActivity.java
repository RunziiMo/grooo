package com.wenym.grooo.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.runzii.lib.ui.base.BaseActivity;
import com.runzii.lib.widgets.behavior.BottomSheetAnchorBehavior;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityShopBinding;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.model.viewmodel.ShopViewModel;
import com.wenym.grooo.ui.activity.MainActivity;
import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends BaseActivity<ActivityShopBinding> {

    public static final int REQUEST_CODE_CONFIRM_PAY = 1;

    private ShopMenuFragment menu_fragment;

    private ShopViewModel viewModel;

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

        Shop shop = getIntent().getParcelableExtra("shop");
        viewModel = new ShopViewModel(this, bind.mergedAppbarlayout, shop);
        bind.setViewModel(viewModel);
        bind.setShop(getIntent().getParcelableExtra("shop"));

        BottomSheetAnchorBehavior bottomSheetBehavior = BottomSheetAnchorBehavior.from(bind.basketForm.getRoot());
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetAnchorBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                viewModel.bottomSheetState.set(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.05f && !viewModel.isBottomExpanded.get()) {
                    viewModel.isBottomExpanded.set(true);
                } else if (slideOffset <= 0.05f && viewModel.isBottomExpanded.get()) {
                    viewModel.isBottomExpanded.set(false);
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        bind.basketForm.basketList.setLayoutManager(layoutManager);
        bind.basketForm.basketList.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
        bind.basketForm.basketList.setHasFixedSize(true);

        setupViewPager(bind.viewpager, bind.getShop().getId());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MainActivity.REQUEST_CODE_REMARK) {
                String remark =  data.getStringExtra("remark");
                viewModel.remark.set(remark);
            } else {
                Address address = AppPreferences.get().getAddress();
                if (address == null)
                    address = new Address();
                if (requestCode == MainActivity.REQUEST_CODE_BUILDING) {
                    String building = data.getStringExtra("building");
                    if (!TextUtils.isEmpty(building) && !building.equals(address.getBuilding())) {
                        address.setBuilding(building);
                        viewModel.setAddress(address);
                    }
                } else if (requestCode == MainActivity.REQUEST_CODE_ROOM) {
                    String room = data.getStringExtra("room");
                    if (!TextUtils.isEmpty(room) && !room.equals(address.getAddress())) {
                        address.setAddress(room);
                        viewModel.setAddress(address);
                    }
                }
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
        viewModel.bottomSheetState.set(job);
    }

    @Override
    protected void onResume() {
        if (viewModel != null)
            viewModel.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (viewModel != null)
            viewModel.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (viewModel.bottomSheetState.get() == BottomSheetAnchorBehavior.STATE_EXPANDED
                || viewModel.bottomSheetState.get() == BottomSheetAnchorBehavior.STATE_ANCHORED) {
            viewModel.bottomSheetState.set(BottomSheetAnchorBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public void showBottomOrHide(View view) {
        switch (viewModel.bottomSheetState.get()) {
            case BottomSheetAnchorBehavior.STATE_ANCHORED:
                toggleSlidingUpLayout(BottomSheetAnchorBehavior.STATE_COLLAPSED);
                break;
            default:
                toggleSlidingUpLayout(BottomSheetAnchorBehavior.STATE_ANCHORED);
                break;
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
