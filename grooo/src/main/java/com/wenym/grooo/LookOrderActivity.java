package com.wenym.grooo;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wenym.grooo.fragments.OrderListFragment;
import com.wenym.grooo.widgets.NoScrollViewPager;

public class LookOrderActivity extends AppCompatActivity {

    private AppBarLayout appbar;
    private TabLayout tabs;
    private NoScrollViewPager pager;
    private MyPagerAdapter adapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookorder);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (NoScrollViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initSystemBar();
        }
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

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            appbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
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
