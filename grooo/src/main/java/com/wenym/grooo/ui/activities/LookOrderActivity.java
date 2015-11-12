package com.wenym.grooo.ui.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.GetOrderData;
import com.wenym.grooo.http.model.GetOrderSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.ecnomy.DeliveryOrder;
import com.wenym.grooo.model.ecnomy.FoodOrder;
import com.wenym.grooo.ui.fragments.OrderListFragment;
import com.wenym.grooo.ui.base.BaseActivity;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.widgets.NoScrollViewPager;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class LookOrderActivity extends BaseActivity {

    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.tabs)
    TabLayout tabs;
    @InjectView(R.id.pager)
    NoScrollViewPager pager;
    private MyPagerAdapter adapter;
    private MaterialDialog dialog;

    private List<FoodOrder> mlist = new ArrayList<>();
    private List<DeliveryOrder> mlistKuadi = new ArrayList<>();


    @Override
    protected boolean isHideNavigationBar() {
        return false;
    }

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
        return R.layout.activity_lookorder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new MaterialDialog.Builder(this)
                .content("正在加载订单")
                .cancelable(false)
                .progress(true, -1).build();
        dialog.show();

        if (GroooAppManager.getTakeouts() == null) {
            mlist = GroooAppManager.getTakeouts();
            mlistKuadi = GroooAppManager.getDeliveries();
            GroooAppManager.initOrders(this);
            setAdapter();
        } else {
            HttpUtils.MakeAPICall(new GetOrderData(), this, new HttpCallBack() {
                @Override
                public void onSuccess(Object object) {
                    GetOrderSuccessData orderSuccessData = (GetOrderSuccessData) object;
                    mlist = orderSuccessData.getRestaurant();
                    mlistKuadi = orderSuccessData.getDelivery();
                    setAdapter();
                }

                @Override
                public void onFailed(String reason) {
                    Toasts.show(reason);
                    setAdapter();
                }

                @Override
                public void onError(int statusCode) {
                    setAdapter();
                    Toasts.show("getOrder" + statusCode);
                }
            });
            // AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(
            // mAdapter);
        }


    }

    private void setAdapter() {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            HttpUtils.CancelHttpTask();
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"外卖订单", "快递订单"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? OrderListFragment.newInstanceFood(mlist) : OrderListFragment.newInstanceDelivery(mlistKuadi);
        }

    }

}
