package com.wenym.grooo.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.runzii.lib.ui.base.BaseActivity;
import com.runzii.lib.utils.Logs;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.School;
import com.wenym.grooo.ui.profile.ProfileFragment;
import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.util.RxEvent.ScrollEvent;
import com.wenym.grooo.util.RxJava.RxBus;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityMainBinding;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.model.http.UpdateInfo;
import com.wenym.grooo.ui.adapters.BottomNavPagerAdapter;
import com.wenym.grooo.util.GroooAppManager;

import java.util.Timer;
import java.util.TimerTask;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static final int REQUEST_CODE_SUGGEST = 10;
    public static final int REQUEST_CODE_SCHOOL = 0x30;
    public static final int REQUEST_CODE_EMAIL = 0x31;
    public static final int REQUEST_CODE_BUILDING = 0x32;
    public static final int REQUEST_CODE_ROOM = 0x33;
    public static final int REQUEST_CODE_NICK = 0X34;

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
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        adapter = new BottomNavPagerAdapter(getSupportFragmentManager());

        bind.pager.setAdapter(adapter);
        bind.pager.setOffscreenPageLimit(0);
        setUpBottomNavigation(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NICK:
                    if (adapter != null && adapter.getFragment(bind.pager.getCurrentItem()) instanceof ProfileFragment) {
                        String nick = data.getStringExtra("nick");
                        Profile profile = AppPreferences.get().getProfile();
                        if (!TextUtils.isEmpty(nick) && !nick.equals(profile.getNickname())) {
                            ProfileFragment fragment = (ProfileFragment) adapter.getFragment(bind.pager.getCurrentItem());
                            profile.setNickname(nick);
                            fragment.model.setProfile(profile);
                        }
                    }
                    break;
                case REQUEST_CODE_SCHOOL:
                    if (adapter != null && adapter.getFragment(bind.pager.getCurrentItem()) instanceof ProfileFragment) {
                        School school = data.getParcelableExtra("school");
                        Profile profile = AppPreferences.get().getProfile();
                        if (school != null && !school.getName().equals(profile.getSchool().getName())) {
                            ProfileFragment fragment = (ProfileFragment) adapter.getFragment(bind.pager.getCurrentItem());
                            profile.setSchool(school);
                            fragment.model.setProfile(profile);
                        }
                    }
                    break;
                case REQUEST_CODE_EMAIL:
                    if (adapter != null && adapter.getFragment(bind.pager.getCurrentItem()) instanceof ProfileFragment) {
                        String email = data.getStringExtra("edit_result");
                        Profile profile = AppPreferences.get().getProfile();
                        if (!TextUtils.isEmpty(email) && !email.equals(profile.getEmail())) {
                            ProfileFragment fragment = (ProfileFragment) adapter.getFragment(bind.pager.getCurrentItem());
                            profile.setEmail(email);
                            fragment.model.setProfile(profile);
                        }
                    }
                    break;
                case REQUEST_CODE_BUILDING:
                    if (adapter != null && adapter.getFragment(bind.pager.getCurrentItem()) instanceof ProfileFragment) {
                        String building = data.getStringExtra("building");
                        Address address = AppPreferences.get().getAddress();
                        if (!TextUtils.isEmpty(building) && !building.equals(address.getBuilding())) {
                            ProfileFragment fragment = (ProfileFragment) adapter.getFragment(bind.pager.getCurrentItem());
                            address.setBuilding(building);
                            fragment.model.setAddress(address);
                        }
                    }
                    break;
                case REQUEST_CODE_ROOM:
                    if (adapter != null && adapter.getFragment(bind.pager.getCurrentItem()) instanceof ProfileFragment) {
                        String room = data.getStringExtra("room");
                        Address address = AppPreferences.get().getAddress();
                        if (!TextUtils.isEmpty(room) && !room.equals(address.getAddress())) {
                            ProfileFragment fragment = (ProfileFragment) adapter.getFragment(bind.pager.getCurrentItem());
                            address.setAddress(room);
                            fragment.model.setAddress(address);
                        }
                    }
                    break;
            }
        }
    }

    private void setUpBottomNavigation(Bundle savedInstanceState) {

        mBottomBar = BottomBar.attachShy(bind.coordinatorLayout,
                bind.pager, savedInstanceState);
        mBottomBar.setItems(R.menu.menu_bottom_navigation);

        mBottomBar.useOnlyStatusBarTopOffset();

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

        RxBus.getDefault().toObserverable(ScrollEvent.class)
                .compose(bindToLifecycle())
                .subscribe(scrollEvent -> {
                    switch (scrollEvent.getState()) {
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            mBottomBar.show();
                            break;
                        case RecyclerView.SCROLL_STATE_IDLE:
                            mBottomBar.hide();
                            break;
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
