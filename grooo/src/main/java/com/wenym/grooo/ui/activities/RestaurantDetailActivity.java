package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.materialviewpager.Utils;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.PayListAdapter;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.http.model.GetMenuData;
import com.wenym.grooo.http.model.GetMenuSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.ecnomy.Restaurant;
import com.wenym.grooo.provider.ShoppingBasket;
import com.wenym.grooo.ui.fragments.ShopMenuFragment;
import com.wenym.grooo.utils.PreferencesUtil;
import com.wenym.grooo.ui.base.BaseActivity;
import com.wenym.grooo.widgets.DividerItemDecoration;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RestaurantDetailActivity extends BaseActivity implements SlidingUpPanelLayout.PanelSlideListener {

    public static final int REQUESTCODE_CONFIRMPAY = 1;


    private SlidingUpPanelLayout mLayout;
    private ImageView basketIcon;

    private AppBarLayout appbar;
    private ViewPager viewPager;
    private Restaurant restaurant;

    private TextView order_cart;
    private ImageView order_car;
    private TextView goto_pay;
    private TextView shop_introduce;

    private LinearLayout headerView;
    private int currBasketColor = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Button confirm_order, clear_order;

    private ShopMenuFragment menu_fragment;


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
        return R.layout.activity_restaurantdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        restaurant = new Gson().fromJson(intent.getStringExtra("entity"), Restaurant.class);

        currBasketColor = getResources().getColor(R.color.white);


        getSupportActionBar().setTitle(restaurant.getShopname());

        appbar = (AppBarLayout) findViewById(R.id.appbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        basketIcon = (ImageView) findViewById(R.id.basket_icon);

        basketIcon.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_keyboard_arrow_up).color(Color.BLACK).sizeDp(36));


        ShoppingBasket basket = ShoppingBasket.getInstance();
        basket.init(restaurant);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PayListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        confirm_order = (Button) findViewById(R.id.confirm_order);

        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(RestaurantDetailActivity.this
                        , ConfirmOrderActivity.class), REQUESTCODE_CONFIRMPAY);
            }
        });

        clear_order = (Button) findViewById(R.id.clear_order);
        clear_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingBasket.getInstance().init(restaurant);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                if (menu_fragment != null) {
                    menu_fragment.getListAdpter().notifyDataSetChanged();
                }

                ((TextView) getBasket().findViewById(R.id.basket_cost)).setText("￥" + ShoppingBasket.getInstance().getTotalPrice());
            }
        });

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setTouchEnabled(false);
        mLayout.findViewById(R.id.basket_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                }
            }
        });

        mLayout.setPanelSlideListener(this);


        headerView = (LinearLayout) getLayoutInflater().inflate(
                R.layout.restaurant_list_header, null, false);
        shop_introduce = (TextView) headerView
                .findViewById(R.id.headview_introduce);
        shop_introduce.setText(restaurant.getAnnouncement());

        initMenu();
    }

    private void initMenu() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .progress(true, -1)
                .content("正在加载菜单")
                .cancelable(false)
                .build();
        dialog.show();

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
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("Getmenu " + statusCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_CONFIRMPAY) {
                Toasts.show(data.getStringExtra(ExtraActivityKeys.PAYINFO.toString()));
                toggleSlidingUpLayout(SlidingUpPanelLayout.PanelState.COLLAPSED);
                finish();
            }
        }
    }


    private void setupViewPager(ViewPager viewPager, ArrayList<com.wenym.grooo.model.ecnomy.Menu> menus) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        menu_fragment = ShopMenuFragment.newInstance(menus);
        adapter.addFragment(menu_fragment, "菜单");
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
        if (PreferencesUtil.getInstance().getFavoriteShops()
                .contains(restaurant.getId())) {
            isFavorite.setVisible(false);
        } else {
            isFavorited.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Set<String> favorites = PreferencesUtil.getInstance().getFavoriteShops();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_favorite:
                favorites.add(restaurant.getId());
                break;
            case R.id.action_favorited:
                favorites.remove(restaurant.getId());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        PreferencesUtil.getInstance().setFavoriteShops(favorites);
        supportInvalidateOptionsMenu();
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

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        final View background = panel.findViewById(R.id.basket_info);
        int before = currBasketColor;
        int after = 0;
        if (slideOffset > 0f) {
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
    public void onPanelCollapsed(View panel) {

    }

    @Override
    public void onPanelExpanded(View panel) {

    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

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
