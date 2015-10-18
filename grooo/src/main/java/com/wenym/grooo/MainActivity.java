package com.wenym.grooo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.wenym.grooo.fragments.AboutUsFragment;
import com.wenym.grooo.fragments.BasketListFragment;
import com.wenym.grooo.fragments.HomeFragment;
import com.wenym.grooo.utils.SharedPreferencesUtil;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements
        Callback {

    public static final int LOGIN_SUCCESS = 10;
    public static final int LOGIN_FAILED = 11;
    public static final int SHOW_LOGIN = 20;
    public static final int HAS_UPDATE = 30;
    public static final int NO_UPDATE = 31;
    public static final int ORDERFOOD_FAILED = 40;
    public static final int ORDERFOOD_SUCCEED = 41;
    public static final int BACK_PRESS = 60;
    public static final int PAY_LINGSHI = 50;
    private static final int[] backgrounds = {R.drawable.bamboo,
            R.drawable.mat2, R.drawable.mat3, R.drawable.ny_light};
    public static Handler handler;
    private static Boolean isExit = false;
    //save our header or result
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;
    private HandlerThread thread;

    private Fragment homeFragment, aboutFragment, currFragment;

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
    public boolean handleMessage(final Message msg) {
        switch (msg.what) {
            case SHOW_LOGIN:
                break;
            case LOGIN_SUCCESS:
                break;
            case LOGIN_FAILED:
                break;
            case ORDERFOOD_SUCCEED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        new MaterialDialog.Builder(MainActivity.this)
                                .content(BasketListFragment.what).positiveText("确定")
                                .positiveColorRes(R.color.black).show();
                    }
                });
                break;
            case ORDERFOOD_FAILED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        new MaterialDialog.Builder(MainActivity.this)
                                .content("下单失败").positiveText("确定")
                                .positiveColorRes(R.color.black).show();
                    }
                });
                break;
            case PAY_LINGSHI:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        getSupportActionBar().setTitle("咕噜超市-快到碗里来");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, BasketListFragment.newInstance())
                                .commit();
                    }
                });
                break;
            case BACK_PRESS:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        aboutFragment = new AboutUsFragment();
        currFragment = homeFragment;

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final IProfile profile = new ProfileDrawerItem().withName(SharedPreferencesUtil.getUsername())
                .withEmail(SharedPreferencesUtil.getUserBuilding() + SharedPreferencesUtil.getUserRoom()).withIcon(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.responsible));

        // Create the AccountHeader
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(backgrounds[new Random().nextInt(backgrounds.length)])
                .addProfiles(
                        profile,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("退出登录")
                                .withDescription("Add new GitHub Account")
                                .withIcon(new IconicsDrawable(this, Octicons.Icon.oct_sign_out)
                                        .actionBarSize().colorRes(R.color.material_drawer_primary_text))
                                .withIdentifier(1),
                        new ProfileSettingDrawerItem().withName("更换用户")
                                .withIcon(new IconicsDrawable(this, Octicons.Icon.oct_issue_reopened)
                                        .actionBarSize().colorRes(R.color.material_drawer_primary_text))
                                .withIdentifier(2)
                ).withSavedInstance(savedInstanceState)
                .withCurrentProfileHiddenInList(true)
                .withCompactStyle(true)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        switch (iProfile.getIdentifier()) {
                            case 1:
                                SharedPreferencesUtil.clearAll();
                                finish();
                                break;
                            case 2:
                                SharedPreferencesUtil.clearAll();
                                startActivity(new Intent(MainActivity.this, GroooLoginActivity.class));
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();

        //Create the drawer
        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("咕噜Grooo").withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withCheckable(true),
                        new SectionDrawerItem().withName("你的咕噜"),
                        new SecondaryDrawerItem().withName("查看订单").withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_event_note).withIdentifier(2).withCheckable(false),
                        new SecondaryDrawerItem().withName(SharedPreferencesUtil.getUsername()).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_account_box).withIdentifier(3).withCheckable(false)
                ).addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("关于我们").withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_extension).withIdentifier(4).withCheckable(true)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            Fragment temp = null;
                            switch (drawerItem.getIdentifier()) {
                                case 1:
                                    getSupportActionBar().setTitle(((Nameable) drawerItem).getName());
                                    if (currFragment instanceof HomeFragment) {
                                    } else {
                                        temp = homeFragment;
                                    }
                                    break;
                                case 2:
                                    intent = new Intent(MainActivity.this, LookOrderActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(MainActivity.this, UserInfoActivity.class);
                                    break;
                                case 4:
                                    getSupportActionBar().setTitle(((Nameable) drawerItem).getName());
                                    if (currFragment instanceof AboutUsFragment) {
                                    } else {
                                        temp = aboutFragment;
                                    }
                                    break;
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                            if (temp != null) {
                                currFragment = temp;
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, currFragment).commit();
                            }
                        }
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {

                    @Override
                    public void onDrawerOpened(View view) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);
                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }

                    @Override
                    public void onDrawerSlide(View view, float v) {

                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, currFragment).commit();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 10
            result.setSelectionByIdentifier(1, false);

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
        } else if (currFragment instanceof HomeFragment) {
            finish();
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
            currFragment = homeFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, currFragment).commit();
        }
    }
}
