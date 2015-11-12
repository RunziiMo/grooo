package com.wenym.grooo.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wenym.grooo.R;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.utils.ImageProvider;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.ui.base.BaseActivity;

import butterknife.InjectView;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class UserInfoActivity extends BaseActivity {

    @InjectView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @InjectView(R.id.toolbar_layout_background)
    GPUImageView backdrop;
    @InjectView(R.id.main_content)
    View content;
    @InjectView(R.id.tv_user_school)
    TextView tv_school;
    @InjectView(R.id.tv_user_building)
    TextView tv_building;
    @InjectView(R.id.tv_user_roomnumber)
    TextView tv_room;
    @InjectView(R.id.tv_user_mail)
    TextView tv_email;


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

        toolbarLayout.setTitle("个人资料");

        loadBackDrop();

        tv_building.setText(GroooAppManager.getAppUser().getUserBuilding());
        tv_email.setText(GroooAppManager.getAppUser().getEmail());
        tv_room.setText(GroooAppManager.getAppUser().getRoomNum());

    }


    private void loadBackDrop() {
        backdrop.setFilter(new GPUImageAlphaBlendFilter());
        backdrop.setImage(Uri.parse(SmallTools.resourceIdToUri(ImageProvider.getAnPictureRes())));
    }

}
