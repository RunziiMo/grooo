package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.header.MaterialViewPagerImageHelper;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.GetBaiduPictureData;
import com.wenym.grooo.http.model.GetBaiduPictureSuccessData;
import com.wenym.grooo.http.model.SuggestData;
import com.wenym.grooo.http.model.SuggestSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.ui.fragments.ListBuddiesFragment;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.utils.PreferencesUtil;
import com.wenym.grooo.ui.base.BaseActivity;
import com.wenym.grooo.utils.UpdateAppManager;
import com.wenym.grooo.widgets.Toasts;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.InjectView;


public class MainActivity extends BaseActivity {

    public static final int REQUEST_CODE_SUGGEST = 10;

    public static final int LOGIN_FAILED = 11;
    public static final int SHOW_LOGIN = 20;
    public static final int HAS_UPDATE = 30;
    public static final int NO_UPDATE = 31;
    public static final int ORDERFOOD_FAILED = 40;
    public static final int ORDERFOOD_SUCCEED = 41;
    public static final int BACK_PRESS = 60;
    public static final int PAY_LINGSHI = 50;
    public static final int[] backgrounds = {R.drawable.bamboo,
            R.drawable.mat2, R.drawable.mat3, R.drawable.ny_light};
    public static Handler handler;
    private static Boolean isExit = false;
    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private HandlerThread thread;

    @InjectView(R.id.main_back)
    ImageView home_back;

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

    @Override
    protected boolean isHideNavigationBar() {
        return false;
    }

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

        new UpdateAppManager(this).checkUpdateInfo();

        HttpUtils.MakeAPICall(new GetBaiduPictureData(), this, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {

                GetBaiduPictureSuccessData data = (GetBaiduPictureSuccessData) object;

                MaterialViewPagerImageHelper
                        .setImageUrl(home_back, data.getData().get(new Random().nextInt(data.getData().size())).getImage_url(), 500);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {

            }
        });

        final IProfile profile = new ProfileDrawerItem().withName(GroooAppManager.getAppUser().getUsername())
                .withEmail(GroooAppManager.getAppUser().getUserBuilding() + GroooAppManager.getAppUser().getRoomNum()).withIcon(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.responsible));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(backgrounds[new Random().nextInt(backgrounds.length)])
                .addProfiles(
                        profile,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("退出登录")
                                .withDescription("Add new GitHub Account")
                                .withIcon(new IconicsDrawable(this, Octicons.Icon.oct_sign_out)
                                        .actionBar().colorRes(R.color.material_drawer_primary_text))
                                .withIdentifier(1),
                        new ProfileSettingDrawerItem().withName("更换用户")
                                .withIcon(new IconicsDrawable(this, Octicons.Icon.oct_issue_reopened)
                                        .actionBar().colorRes(R.color.material_drawer_primary_text))
                                .withIdentifier(2)
                ).withSavedInstance(savedInstanceState)
                .withCurrentProfileHiddenInList(true)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        switch (iProfile.getIdentifier()) {
                            case 1:
                                PreferencesUtil.getInstance().clearAll();
                                finish();
                                break;
                            case 2:
                                PreferencesUtil.getInstance().clearAll();
                                startActivity(new Intent(MainActivity.this, GroooLoginActivity.class));
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("咕噜Grooo").withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withSelectable(true),
                        new SectionDrawerItem().withName("你的咕噜"),
                        new SecondaryDrawerItem().withName("查看订单").withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_event_note).withIdentifier(2).withSelectable(false),
                        new SecondaryDrawerItem().withName(GroooAppManager.getAppUser().getUsername()).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_account_box).withIdentifier(3).withSelectable(false)
                ).addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.aboutus).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_extension).withIdentifier(4).withSelectable(true)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            switch (drawerItem.getIdentifier()) {
                                case 1:
                                    break;
                                case 2:
                                    intent = new Intent(MainActivity.this, LookOrderActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(MainActivity.this, UserInfoActivity.class);
                                    break;
                                case 4:
                                    intent = new Intent(MainActivity.this, AboutActivity.class);
                                    break;
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withTranslucentStatusBar(false)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, ListBuddiesFragment.newInstance(true)).commit();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 10

            //set the active profile
            headerResult.setActiveProfile(profile);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SUGGEST:
                    SuggestData suggestData = new SuggestData();
                    suggestData.setContent(data.getStringExtra(ExtraActivityKeys.SUGGESTION.toString()));
                    HttpUtils.MakeAPICall(suggestData, this, new HttpCallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            SuggestSuccessData suggestSuccessData = (SuggestSuccessData) object;
                            Toasts.show("建议已提交，我们会尽快反馈");
                        }

                        @Override
                        public void onFailed() {

                        }

                        @Override
                        public void onError(int statusCode) {
                            Toasts.show("Suggest " + statusCode);
                        }
                    });
                    break;
            }
        }
    }


}
