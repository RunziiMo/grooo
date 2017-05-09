package net.azstudio.groooseller.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.provider.ExtraActivityKeys;
import net.azstudio.groooseller.provider.FragmentTags;
import net.azstudio.groooseller.ui.base.BaseActivity;
import net.azstudio.groooseller.ui.fragments.AboutUsFragment;


/**
 * Created by runzii on 15-11-9.
 */
public class SingleFragmentActivity extends BaseActivity {


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
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frame_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.frame_container, fragment).commit();
        }
    }

    private Fragment createFragment() {
        Fragment fragment = null;
        switch ((FragmentTags) getIntent().getSerializableExtra(ExtraActivityKeys.FRAGMENT.toString())) {
            case ABOUT_US:
                fragment = new AboutUsFragment();
                break;
            default:
                fragment = new AboutUsFragment();
                break;
        }
        return fragment;
    }
}
