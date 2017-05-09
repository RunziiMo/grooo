package net.azstudio.groooseller.ui.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.runzii.lib.constant.MyActions;
import com.runzii.lib.constant.MyKeys;

import net.azstudio.groooseller.MyService;
import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.ActivityMainBinding;
import net.azstudio.groooseller.http.NetworkWrapper;
import net.azstudio.groooseller.model.app.PushInfo;
import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.model.business.FoodOrder;
import net.azstudio.groooseller.model.http.UpdateInfo;
import net.azstudio.groooseller.provider.ExtraActivityKeys;
import net.azstudio.groooseller.provider.FragmentTags;
import net.azstudio.groooseller.ui.base.BaseActivity;
import net.azstudio.groooseller.ui.widgets.EmptySupportRecyclerView;
import net.azstudio.groooseller.ui.widgets.adapter.OrderAdapter;
import net.azstudio.groooseller.utils.AppManager;
import net.azstudio.groooseller.utils.AppPreferences;
import net.azstudio.groooseller.utils.Logs;
import net.azstudio.groooseller.utils.RxEvent.OrderEvent;
import net.azstudio.groooseller.utils.RxJava.RxBus;
import net.azstudio.groooseller.utils.RxJava.RxNetWorking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import rx.Observable;
import rx.functions.Func1;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements OnCheckedChangeListener {

    public static final int[] backgrounds = {R.drawable.mat1, R.drawable.mat4};
    public static boolean isForeground = false;
    private AccountHeader headerResult = null;

    private IProfile profile;
    private Drawer result = null;
    private SwitchDrawerItem shopStatus;

    private List<FoodOrder> undoOrder = new ArrayList<>(), finishedOrder = new ArrayList<>();

    private Observable<ShopInfo> shopInfoObservable;

    private Observable<List<FoodOrder>[]> observableRefreshData;

    private OrderAdapter undo, finished;
    private ActivityMainBinding mainBinding;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = getDataBinding();
        if (AppPreferences.get().getAuthUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            FIR.checkForUpdateInFIR("487080b6240eab23c5e9b55a7712b23a", new VersionCheckCallback() {
                @Override
                public void onSuccess(String versionJson) {
                    UpdateInfo info = new Gson().fromJson(versionJson, UpdateInfo.class);
                    if (AppManager.getVersionCode() < info.getBuild())
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
                    Logs.d("正在获取");
                }

                @Override
                public void onFinish() {
                    Logs.d("正在获取");
                }
            });
            NetworkWrapper.get()
                    .setPushInfo(new PushInfo(JPushInterface.getRegistrationID(this)))
                    .subscribe(s -> {
                            }
                            , throwable ->
                                    Snackbar.make(mainBinding.pager, throwable.getMessage(), Snackbar.LENGTH_SHORT).show()
                    );
//            Logs.d(JPushInterface.getRegistrationID(this));
            setUpDrawer(savedInstanceState);
            registerMessageReceiver();
            setUpPager();
            shopInfoObservable = NetworkWrapper.get().getShopInfo(AppManager.getShopInfo().getId());
            mainBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryLight, R.color.colorPrimary);
            RxSwipeRefreshLayout.refreshes(mainBinding.swipeRefreshLayout)
                    .doOnUnsubscribe(() -> {
                        mainBinding.swipeRefreshLayout.setRefreshing(false);
                    })
                    .compose(bindToLifecycle())
                    .subscribe(aVoid -> {
                        refreshData();
                    });
            RxBus.getDefault().toObserverable(OrderEvent.class)
                    .compose(bindToLifecycle())
                    .subscribe(orderEvent -> {
                        finishedOrder.add(0, orderEvent.getOrder());
                        finished.notifyDataSetChanged();
                    });
        }

    }

    @Override
    protected void onResume() {
        isForeground = true;
        refreshData();
        sendBroadcast(new Intent(MyActions.ACTION_PAUSE));
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    private void refreshData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        observableRefreshData = NetworkWrapper.get()
                .getOrder(new Date(), calendar.getTime())
                .map((Func1<List<FoodOrder>, List<FoodOrder>[]>) orders -> {
                    Collections.sort(orders, (lhs, rhs) -> {
                        if (lhs.getTime().equals(rhs.getTime()))
                            return lhs.getOrder_id().compareTo(rhs.getOrder_id());
                        else
                            return lhs.getTime().compareTo(rhs.getTime());
                    });
                    List<FoodOrder> undo = new ArrayList<>(), finished = new ArrayList<>();
                    for (FoodOrder order : orders) {
                        int status = order.getStatus();
                        if (status == 0 || status == 20)
                            undo.add(order);
                        else finished.add(order);
                    }
                    return new List[]{undo, finished};
                })
                .compose(RxNetWorking.bindRefreshing(mainBinding.swipeRefreshLayout))
                .compose(bindToLifecycle());
        observableRefreshData.subscribe(menus -> notifyDataSetChanged(menus)
                , throwable -> Snackbar.make(mainBinding.pager, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
        shopInfoObservable.subscribe(shopInfo -> {
            AppPreferences.get().setShopInfo(shopInfo);
            AppManager.setShopInfo(shopInfo);
            headerResult.getActiveProfile().withEmail(shopInfo.getDescription());
            headerResult.getActiveProfile().withName(shopInfo.getName());
            headerResult.getActiveProfile().withIcon(shopInfo.getLogo());
            headerResult.updateProfile(profile);
            boolean status = shopInfo.getStatus() == 1;
            setIntentService(status);
            shopStatus.withName("营业状态:" + (status ? "开" : "关"));
            shopStatus.withChecked(status);
            result.updateItem(shopStatus);
        }, throwable -> Snackbar.make(mainBinding.pager, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
    }

    private void setIntentService(boolean action) {
        if (action)
            startService(new Intent(this, MyService.class));
        else
            stopService(new Intent(this, MyService.class));
    }

    private void notifyDataSetChanged(List<FoodOrder>[] orders) {
        undoOrder.clear();
        finishedOrder.clear();
        undoOrder.addAll(orders[0]);
        finishedOrder.addAll(orders[1]);
        undo.notifyDataSetChanged();
        finished.notifyDataSetChanged();
    }

    private void setUpPager() {
        undo = new OrderAdapter(undoOrder);
        finished = new OrderAdapter(finishedOrder);
        mainBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mainBinding.swipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
        mainBinding.pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = getLayoutInflater().inflate(R.layout.item_viewpager, container, false);
                EmptySupportRecyclerView recyclerView = (EmptySupportRecyclerView) v.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setEmptyView(v.findViewById(R.id.empty_view));
                if (position == 0)
                    recyclerView.setAdapter(undo);
                else {
                    recyclerView.setAdapter(finished);
                }
                container.addView(v);
                return v;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0)
                    return "未处理";
                else return "已处理";
            }
        });
        mainBinding.tabs.setupWithViewPager(mainBinding.pager);
    }

    @Override
    protected void onDestroy() {
        if (mMessageReceiver != null)
            unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void setUpDrawer(Bundle savedInstanceState) {

        shopStatus = new SwitchDrawerItem().withName("营业状态:" + (AppManager.getShopInfo().getStatus() == 1 ? "开" : "关"))
                .withIcon(Octicons.Icon.oct_tools).withChecked(AppManager.getShopInfo().getStatus() == 1 ? true : false).withIdentifier(1).withOnCheckedChangeListener(this);

        profile = new ProfileDrawerItem().withName(AppManager.getShopInfo().getName())
                .withEmail(AppManager.getShopInfo().getDescription()).withIcon(AppManager.getShopInfo().getLogo()).withIdentifier(100);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(backgrounds[new Random().nextInt(backgrounds.length)])
                .addProfiles(
                        profile
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        shopStatus,
                        new PrimaryDrawerItem().withName(R.string.activitylabel_ordertoday).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_view_list).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.activitylabel_shopmanage).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_shop).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.activitylabel_menumanage).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_menu).withIdentifier(4).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.activitylabel_allorder).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_done_all).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.activitylabel_exit).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_assignment_return).withIdentifier(6).withSelectable(false)
                ).addStickyDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.activitylabel_aboutus).withTextColorRes(R.color.material_drawer_primary_text).withIcon(GoogleMaterial.Icon.gmd_extension).withIdentifier(7).withSelectable(false)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    //check if the drawerItem is set.
                    //there are different reasons for the drawerItem to be null
                    //--> click on the header
                    //--> click on the footer
                    //those items don't contain a drawerItem

                    if (drawerItem != null) {
                        Intent intent = null;
                        switch ((int) drawerItem.getIdentifier()) {
                            case 3:
                                intent = new Intent(MainActivity.this, ManageActivity.class);
                                break;
                            case 4:
                                intent = new Intent(MainActivity.this, MenuActivity.class);
                                break;
                            case 5:
                                intent = new Intent(MainActivity.this, OrderActivity.class);
                                break;
                            case 6:
                                AppPreferences.get().clearAll();
                                finish();
                                break;
                            case 7:
                                intent = new Intent(MainActivity.this, SingleFragmentActivity.class);
                                intent.putExtra(ExtraActivityKeys.FRAGMENT.toString(), FragmentTags.ABOUT_US);
                                break;
                        }
                        if (intent != null) {
                            MainActivity.this.startActivity(intent);
                        }
                    }

                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 10

            //set the active profile
            headerResult.setActiveProfile(profile);
        }
    }

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
            PackageManager pm = getPackageManager();
            ResolveInfo homeInfo = pm.resolveActivity(new Intent(
                    Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent
                    .setComponent(new ComponentName(ai.packageName, ai.name));
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(startIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // finish();
            // System.exit(0);
        }
    }


    @Override
    public void onBackPressed() {
        // 点击返回键关闭滑动菜单
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            exitBy2Click();
        }
    }

    @Override
    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {

        switch ((int) drawerItem.getIdentifier()) {
            case 1:
                addSubscription(NetworkWrapper.get()
                        .setShopStatus(isChecked ? 1 : 0)
                        .compose(bindToLifecycle())
                        .subscribe(message -> {
                            MainActivity.this.shopStatus.withName("营业状态:" + (isChecked ? "开" : "关"));
                            AppManager.getShopInfo().setStatus(isChecked ? 1 : 0);
                            result.updateItem(MainActivity.this.shopStatus);
                            setIntentService(isChecked);
                        }, throwable -> Snackbar.make(mainBinding.pager, throwable.getMessage(), Snackbar.LENGTH_SHORT).show()));
                break;
        }
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MyActions.MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyActions.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(MyKeys.KEY_MESSAGE);
                String extras = intent.getStringExtra(MyKeys.KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(MyKeys.KEY_MESSAGE + " : " + messge + "\n");
                showMsg.append(MyKeys.KEY_EXTRAS + " : " + extras + "\n");
                setCostomMsg(showMsg.toString());
            }
        }

    }

    private void setCostomMsg(String msg) {
        Snackbar.make(mainBinding.pager, "你有新订单", Snackbar.LENGTH_SHORT).show();
    }
}
