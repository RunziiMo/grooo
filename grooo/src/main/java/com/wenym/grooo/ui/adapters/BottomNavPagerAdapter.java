package com.wenym.grooo.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.wenym.grooo.ui.fragments.ProfileFragment;
import com.wenym.grooo.ui.fragments.OrderListFragment;
import com.wenym.grooo.ui.fragments.ShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runzii on 16-7-3.
 */
public class BottomNavPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public BottomNavPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.clear();
        fragments.add(ShopFragment.newInstance());
        fragments.add(OrderListFragment.newInstance());
        fragments.add(ProfileFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
