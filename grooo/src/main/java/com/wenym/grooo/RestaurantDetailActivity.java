package com.wenym.grooo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.materialviewpager.Utils;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wenym.grooo.fragments.BasketListFragment;
import com.wenym.grooo.fragments.BasketStatusFragment;
import com.wenym.grooo.fragments.RestaurantMenuFragment;
import com.wenym.grooo.http.model.GetBaiduPictureData;
import com.wenym.grooo.http.model.GetBaiduPictureSuccessData;
import com.wenym.grooo.http.model.GetMenuData;
import com.wenym.grooo.http.model.GetMenuSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.Restaurant;
import com.wenym.grooo.model.ShoppingBasket;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static int[] images = {R.drawable.food1, R.drawable.food2,
            R.drawable.food3, R.drawable.food4, R.drawable.food5,
            R.drawable.food6, R.drawable.food7, R.drawable.food8,
            R.drawable.food9};

    private SlidingUpPanelLayout mLayout;
    private ImageView basketIcon;

    private Toolbar toolbar;
    private AppBarLayout appbar;
    private ViewPager viewPager;
    private ViewPager viewPagerBasket;
    private Restaurant restaurant;

    private TextView order_cart;
    private ImageView order_car;
    private TextView goto_pay;
    private TextView shop_introduce;

    private LinearLayout headerView;
    private boolean isWaiMai;
    private int currBasketColor = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        restaurant = new Gson().fromJson(intent.getStringExtra("entity"), Restaurant.class);
        isWaiMai = intent.getBooleanExtra("isWaiMai", true);

        currBasketColor = getResources().getColor(R.color.white);

        setContentView(R.layout.activity_restaurantdetail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(restaurant.getShopname());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appbar = (AppBarLayout) findViewById(R.id.appbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerBasket = (ViewPager) findViewById(R.id.basket_viewpager);

        setupViewPagerBasket(viewPagerBasket);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.basket_tabs);
        tabLayout.setupWithViewPager(viewPagerBasket);

        basketIcon = (ImageView) findViewById(R.id.basket_icon);

        basketIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_keyboard_arrow_up).color(Color.BLACK).sizeDp(36));

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.6f);
        mLayout.setTouchEnabled(false);
        mLayout.findViewById(R.id.basket_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    }
                }
            }
        });

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                final View background = view.findViewById(R.id.basket_info);
                int before = currBasketColor;
                int after = 0;
                if (v > 0f) {
                    after = getResources().getColor(R.color.colorPrimary);
                    if (currBasketColor == after) {
                        return;
                    }
                    ((TextView) getBasket().findViewById(R.id.basket_costname)).setTextColor(Color.WHITE);
                    ((TextView) getBasket().findViewById(R.id.basket_cost)).setTextColor(Color.WHITE);
                    basketIcon.setImageDrawable(new IconicsDrawable(RestaurantDetailActivity.this
                            , GoogleMaterial.Icon.gmd_keyboard_arrow_down).color(Color.WHITE).sizeDp(36));
                } else {
                    after = getResources().getColor(R.color.white);
                    ((TextView) getBasket().findViewById(R.id.basket_costname)).setTextColor(Color.BLACK);
                    ((TextView) getBasket().findViewById(R.id.basket_cost)).setTextColor(Color.BLACK);
                    basketIcon.setImageDrawable(new IconicsDrawable(RestaurantDetailActivity.this
                            , GoogleMaterial.Icon.gmd_keyboard_arrow_up).color(Color.BLACK).sizeDp(36));
                }
                ObjectAnimator colorAnim = ObjectAnimator.ofInt(background, "backgroundColor", before, after);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.setDuration((long) 100);
                colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int colorAlpha = Utils.colorWithAlpha(((Integer) animation.getAnimatedValue()).intValue(), 0f);
                        background.setBackgroundColor(colorAlpha);
                    }
                });
                colorAnim.start();
                currBasketColor = after;
            }

            @Override
            public void onPanelCollapsed(View view) {

            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });


        HttpUtils.MakeAPICall(new GetBaiduPictureData(), this, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                GetBaiduPictureSuccessData getBaiduPictureSuccessData = (GetBaiduPictureSuccessData) object;
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("getBaiduPicture " + statusCode);
            }
        });

//        order_cart = (TextView) findViewById(R.id.food_list_shipping_fee);
//        order_car = (ImageView) findViewById(R.id.food_list_order_cart);
//        goto_pay = (TextView) findViewById(R.id.food_list_take_order_button);

        headerView = (LinearLayout) getLayoutInflater().inflate(
                R.layout.restaurant_list_header, null, false);
        shop_introduce = (TextView) headerView
                .findViewById(R.id.headview_introduce);
        shop_introduce.setText(restaurant.getAnnouncement());
//        if (restaurant.getStatus()) {
//            goto_pay.setOnClickListener(new OnClickListener() {
//
//                @SuppressWarnings({"unchecked", "rawtypes"})
//                @Override
//                public void onClick(View v) {
//                    if (ShoppingBasket.getInstance().isOkToPay()) {
//                        onBackPressed();
//                        onBackPressed();
//
//                        getActionBar().setTitle(restaurant.getShopname() + "-快到碗里来");
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.frame_container, new PayFragment(ShoppingBasket.getInstance(),
//                                        isWaiMai, restaurant.getId()))
//                                .addToBackStack("home").commit();
//                    }
//                }
//            });
//        }

        GetMenuData getMenuData = new GetMenuData();
        getMenuData.setId(restaurant.getId());
        HttpUtils.MakeAPICall(getMenuData, this, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {

                GetMenuSuccessData getMenuSuccessData = (GetMenuSuccessData) object;
                if (viewPager != null) {
                    setupViewPager(viewPager, getMenuSuccessData.getMenus());
                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                }

//                if (getFragmentManager() != null) {
//                    getFragmentManager().popBackStack();
//                }
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("Getmenu " + statusCode);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initSystemBar();
        }


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
            appbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
    }

    private void setupViewPagerBasket(ViewPager viewPager) {
        ShoppingBasket basket = ShoppingBasket.getInstance();
        basket.init(restaurant);
        BasketListFragment fragment1 = BasketListFragment.newInstance();
        BasketStatusFragment fragment2 = BasketStatusFragment.newInstance(isWaiMai);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragment1, "订单内容");
        adapter.addFragment(fragment2, "确认订单");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager(ViewPager viewPager, ArrayList<com.wenym.grooo.model.Menu> menus) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < menus.size(); i++) {
            RestaurantMenuFragment fragment = new RestaurantMenuFragment();
            Bundle bundle = new Bundle();
            bundle.putString("menu", new Gson().toJson(menus.get(i), com.wenym.grooo.model.Menu.class));
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, menus.get(i).getFoodclass());
        }
        viewPager.setAdapter(adapter);
    }

    public View getBasket() {
        return findViewById(R.id.basket_info);
    }

    public void toggleSlidingUpLayout(SlidingUpPanelLayout.PanelState job) {
        if (mLayout != null) {
            if (mLayout.getPanelState() != job) {
                mLayout.setPanelState(job);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem isFavorite = menu.findItem(R.id.action_favorite);
        MenuItem isFavorited = menu.findItem(R.id.action_favorited);
        if (GroooApplication.getAppSharedPreferences()
                .getStringSet("favorite", new HashSet<String>())
                .contains(restaurant.getShopname())) {
            isFavorite.setVisible(false);
        } else {
            isFavorited.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Set<String> favorites = GroooApplication.getAppSharedPreferences()
                .getStringSet("favorite", new HashSet<String>());
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_favorite:
                favorites.add(restaurant.getShopname());
                GroooApplication.getAppSharedPreferences().edit()
                        .putStringSet("favorite", favorites).commit();
                supportInvalidateOptionsMenu();
                break;
            case R.id.action_favorited:
                favorites.remove(restaurant.getShopname());
                GroooApplication.getAppSharedPreferences().edit()
                        .putStringSet("favorite", favorites).commit();
                supportInvalidateOptionsMenu();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
