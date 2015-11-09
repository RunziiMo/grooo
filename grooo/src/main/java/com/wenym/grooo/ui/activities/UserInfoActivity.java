package com.wenym.grooo.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.MenuItem;
import android.view.View;

import com.wenym.grooo.R;
import com.wenym.grooo.utils.ImageProvider;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.ui.base.BaseActivity;

import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class UserInfoActivity extends BaseActivity {

    private CollapsingToolbarLayout toolbarLayout;
    private View content;


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
        return R.layout.activity_userinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("个人资料");

        content = findViewById(R.id.main_content);

        loadBackDrop();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadBackDrop() {
        GPUImageView backdrop = (GPUImageView) findViewById(R.id.toolbar_layout_background);
        backdrop.setFilter(new GPUImageAlphaBlendFilter());
        backdrop.setImage(Uri.parse(SmallTools.resourceIdToUri(ImageProvider.getAnPictureRes())));
    }

}
