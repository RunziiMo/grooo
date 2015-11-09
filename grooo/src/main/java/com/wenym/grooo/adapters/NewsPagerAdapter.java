package com.wenym.grooo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wenym.grooo.ui.fragments.NewsFragment;
import com.wenym.grooo.model.app.New;

import java.util.ArrayList;
import java.util.Random;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<New> newses;
    private Random random = new Random();

    public NewsPagerAdapter(FragmentManager fm, ArrayList<New> newses) {
        super(fm);
        this.newses = newses;
    }

    @Override
    public Fragment getItem(int i) {
        return NewsFragment.newInstance(i, 0xff000000 | random.nextInt(0x00ffffff), newses.get(i));
        // return ColorFragment.newInstance(0xff000000 |
        // random.nextInt(0x00ffffff));
    }

    @Override
    public int getCount() {
        return newses.size();
    }
}