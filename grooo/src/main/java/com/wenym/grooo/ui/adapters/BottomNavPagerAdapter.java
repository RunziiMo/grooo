package com.wenym.grooo.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.wenym.grooo.ui.profile.ProfileFragment;
import com.wenym.grooo.ui.fragments.OrderListFragment;
import com.wenym.grooo.ui.shop.ShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runzii on 16-7-3.
 */
public class BottomNavPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public BottomNavPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(ShopFragment.newInstance());
        fragments.add(OrderListFragment.newInstance());
        fragments.add(ProfileFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    private Fragment curr;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        Fragment fragment = (Fragment) object;
        if (fragment != curr && curr != null) {
            curr = fragment;
            fragment.onResume();
        }
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
