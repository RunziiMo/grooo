package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.runzii.lib.ui.base.BaseActivity;
import com.runzii.lib.utils.Logs;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityMainBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.model.http.UpdateInfo;
import com.wenym.grooo.ui.adapters.BottomNavPagerAdapter;
import com.wenym.grooo.util.GroooAppManager;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static final int REQUEST_CODE_SUGGEST = 10;

    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false;
                }
            }, 2000);

        } else {
            finish();
        }
    }


    private BottomNavPagerAdapter adapter;

    private BottomBar mBottomBar;

    @Override
    protected boolean isDisplayHomeAsUp() {
        return false;
    }

    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Profile profile = GroooAppManager.getProfile();
        if(!JPushInterface.getRegistrationID(this).equals(profile.getPush_id())) {
            profile.setPush_id(JPushInterface.getRegistrationID(this));
            GroooAppManager.setProfile(profile);
            NetworkWrapper.get().putProfile(profile).subscribe(s -> Toasts.show(s), errorHandle("设置个人推送信息"));
        }
        FIR.checkForUpdateInFIR("487080b6240eab23c5e9b55a7712b23a", new VersionCheckCallback() {

            @Override
            public void onSuccess(String versionJson) {
                UpdateInfo info = new Gson().fromJson(versionJson, UpdateInfo.class);
                if (GroooAppManager.getVersionCode() < info.getBuild())
                    new MaterialDialog.Builder(MainActivity.this)
                            .title("新版本更新")
                            .content(info.getChangelog())
                            .positiveText("安装")
                            .negativeText("取消")
                            .neutralText("更新")
                            .onPositive((dialog, which) -> {
                                Uri uri = Uri.parse(info.getInstall_url());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            })
                            .onNeutral((dialog1, which1) -> {
                                Uri uri = Uri.parse(info.getInstall_url());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            })
                            .show();
            }

            @Override
            public void onFail(Exception exception) {
                Logs.d("fir check fir.im fail! " + "\n" + exception.getMessage());
            }

            @Override
            public void onStart() {
                Logs.d("正在获取更新信息");
            }

            @Override
            public void onFinish() {
                Logs.d("获取更新信息完成");
            }
        });

        bind.pager.setAdapter(new BottomNavPagerAdapter(getSupportFragmentManager()));
        bind.pager.setOffscreenPageLimit(2);
        setUpBottomNavigation(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpBottomNavigation(Bundle savedInstanceState) {

        mBottomBar = BottomBar.attachShy(bind.coordinatorLayout,
                bind.pager, savedInstanceState);
        mBottomBar.setItems(R.menu.menu_bottom_navigation);

        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {


            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                switch (menuItemId) {
                    case R.id.bottom_nav_market:
                        bind.pager.setCurrentItem(0, false);
                        break;
                    case R.id.bottom_nav_order:
                        bind.pager.setCurrentItem(1, false);
                        break;
                    case R.id.bottom_nav_profile:
                        bind.pager.setCurrentItem(2, false);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

}
