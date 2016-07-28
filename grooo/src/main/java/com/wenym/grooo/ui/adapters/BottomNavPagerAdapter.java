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

    public BottomNavPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShopFragment.newInstance();
            case 1:
                return OrderListFragment.newInstance();
            case 2:
                return ProfileFragment.newInstance();
            default:
                return ProfileFragment.newInstance();
        }
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

    @Override
    public int getCount() {
        return 3;
    }

}
