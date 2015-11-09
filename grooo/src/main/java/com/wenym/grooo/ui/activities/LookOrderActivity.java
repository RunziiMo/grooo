package com.wenym.grooo.ui.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wenym.grooo.R;
import com.wenym.grooo.ui.fragments.OrderListFragment;
import com.wenym.grooo.widgets.BaseActivity;
import com.wenym.grooo.widgets.NoScrollViewPager;

public class LookOrderActivity extends BaseActivity {

    private AppBarLayout appbar;
    private TabLayout tabs;
    private NoScrollViewPager pager;
    private MyPagerAdapter adapter;
    private Toolbar toolbar;


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

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (NoScrollViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyPagerAdapter extends FragmentStatePagerAdapter {

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
            return OrderListFragment.newInstance(position);
        }

    }

}
