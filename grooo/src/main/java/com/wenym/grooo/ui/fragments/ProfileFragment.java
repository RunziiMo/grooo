package com.wenym.grooo.ui.fragments;

import android.os.Bundle;

import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentProfileBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.util.GroooAppManager;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
