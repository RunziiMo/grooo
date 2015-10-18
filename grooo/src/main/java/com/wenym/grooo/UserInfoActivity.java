package com.wenym.grooo;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wenym.grooo.utils.ImageProvider;
import com.wenym.grooo.utils.SmallTools;

import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class UserInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private View content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("个人资料");

        content = findViewById(R.id.main_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        loadBackDrop();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initSystemBar();
        }

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
            content.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
    }
}
