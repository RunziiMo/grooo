package net.azstudio.groooseller.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.http.NetworkWrapper;
import net.azstudio.groooseller.model.business.Menu;
import net.azstudio.groooseller.model.http.HttpFood;
import net.azstudio.groooseller.ui.base.BaseActivity;
import net.azstudio.groooseller.ui.widgets.adapter.CategoryApdater;
import net.azstudio.groooseller.ui.widgets.adapter.CategoryDetailAdapter;
import net.azstudio.groooseller.ui.widgets.DividerItemDecoration;
import net.azstudio.groooseller.ui.widgets.EmptySupportRecyclerView;
import net.azstudio.groooseller.utils.AppManager;
import net.azstudio.groooseller.utils.AppPreferences;
import net.azstudio.groooseller.utils.Logs;
import net.azstudio.groooseller.utils.RxEvent.CategoryClickEvent;
import net.azstudio.groooseller.utils.RxJava.RxBus;
import net.azstudio.groooseller.utils.RxJava.RxNetWorking;
import net.azstudio.groooseller.utils.SmallTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class MenuActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private MenuAdapter menuAdapter;
    private CategoryApdater categoryAdapter;
    private CategoryDetailAdapter foodAdapter;

    private int currCategory;
    private List<Menu> foods = new ArrayList<>();

    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuAdapter = new MenuAdapter();
        categoryAdapter = new CategoryApdater();
        foodAdapter = new CategoryDetailAdapter(new ArrayList<>(), () -> {
            foodAdapter.setSelectMode(!foodAdapter.isSelectMode());
            supportInvalidateOptionsMenu();
        }, this::showFoodDialog);
        viewPager.setAdapter(menuAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                refreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
        setUpData();
        setUpSubscriber();
        RxToolbar.itemClicks(getToolbar())
                .map(menuItem -> menuItem.getItemId())
                .subscribe(this::onToolbarItemClicked);
        refreshData();
        Glide.with(this).load("")
                .placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).sizeDp(24).colorRes(R.color.white)).into(fab);
    }

    private void setUpSubscriber() {
        RxBus.getDefault().toObserverable(CategoryClickEvent.class)
                .compose(bindToLifecycle())
                .subscribe(userEvent -> {
                    currCategory = userEvent.getPosition();
                    viewPager.setCurrentItem(1, true);
                    foodAdapter.setFoods(userEvent.getFoods());
                });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1)
            viewPager.setCurrentItem(0, true);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void onToolbarItemClicked(int itemId) {
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_select:
                foodAdapter.setSelectMode(true);
                break;
            case R.id.action_delete:
                if (foodAdapter.getSelectItems().size() > 0)
                    NetworkWrapper.get()
                            .deleteFood(AppPreferences.get().getAuth()
                                    , AppManager.getShopInfo().getId()
                                    , foodAdapter.getSelectItems())
                            .subscribe(food -> {
                                SmallTools.deleteFoodInMenu(foods, food);
                                notifyDataSetChanged(foods);
                            }, throwable -> {
                                throwable.printStackTrace();
                            }, () -> {
                                foodAdapter.setSelectMode(false);
                                supportInvalidateOptionsMenu();
                            });
                else foodAdapter.setSelectMode(false);
                break;
            default:
                break;
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        MenuItem select = menu.findItem(R.id.action_select);
        MenuItem delete = menu.findItem(R.id.action_delete);
        if (foodAdapter.isSelectMode()) {
            select.setVisible(false);
        } else {
            delete.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private Observable<List<Menu>> observableRefreshData;

    private void setUpData() {
        observableRefreshData = NetworkWrapper.get()
                .getMenu(AppPreferences.get().getAuth(), AppManager.getShopInfo().getId())
                .compose(RxNetWorking.bindRefreshing(refreshLayout));
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryLight, R.color.colorPrimary);
        RxSwipeRefreshLayout.refreshes(refreshLayout)
                .doOnUnsubscribe(() -> {
                    refreshLayout.setRefreshing(false);
                })
                .compose(bindToLifecycle())
                .subscribe(aVoid -> {
                    refreshData();
                });
    }

    private void refreshData() {
        Logs.d("refresh");
        observableRefreshData.subscribe(menus -> {
            notifyDataSetChanged(menus);
        });
    }

    @OnClick(R.id.fab)
    public void addFood() {
        showFoodDialog(new HttpFood(), true, foods.size() == 0 ? "" : foods.get(currCategory).getCategory());
    }

    private void notifyDataSetChanged(List<Menu> menus) {
        foods = menus;
        categoryAdapter.setFoods(foods);
        foodAdapter.setFoods(foods.size() == 0 ? new ArrayList<>() : foods.get(currCategory).getFoods());
    }

    private class MenuAdapter extends PagerAdapter {

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
            recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
            recyclerView.addItemDecoration(new DividerItemDecoration(MenuActivity.this, R.drawable.divider));
            recyclerView.setEmptyView(v.findViewById(R.id.empty_view));
            if (position == 0)
                recyclerView.setAdapter(categoryAdapter);
            else {
                recyclerView.setAdapter(foodAdapter);
            }
            container.addView(v);
            return v;
        }

    }

    // 点击的回调
    public interface ItemClickCallback {
        void onItemClicked(HttpFood food, boolean isNew, String category);
    }

    // 点击的回调
    public interface ItemLongClickCallback {
        void onItemLongClicked();
    }

    private EditText foodName, foodCategory, foodDescription, foodPrice;

    public void showFoodDialog(final HttpFood food, final boolean isNew, String category) {
        MaterialDialog dialog = new MaterialDialog.Builder(MenuActivity.this)
                .title("菜品属性")
                .customView(R.layout.change_food_attribute, true)
                .positiveText("确定")
                .negativeText(android.R.string.cancel)
                .onPositive((dialog1, which) -> {
                    food.setName(foodName.getText().toString().trim());
                    food.setCategory(foodCategory.getText().toString().trim());
                    food.setDescription(foodDescription.getText().toString().trim());
                    food.setPrice(foodPrice.getText().toString().trim());
                    if (isNew) {
                        addSubscription(NetworkWrapper
                                .get()
                                .addFood(responseBody -> refreshData(), AppPreferences.get().getAuth(), AppManager.getShopInfo().getId(), food));
                    } else {
                        addSubscription(NetworkWrapper
                                .get()
                                .editFood(responseBody -> refreshData(), AppPreferences.get().getAuth(), AppManager.getShopInfo().getId(), food));
                    }
                }).build();

        final MDButton positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        foodName = (EditText) dialog.getCustomView().findViewById(R.id.food_edit_name);
        foodCategory = (EditText) dialog.getCustomView().findViewById(R.id.food_edit_category);
        foodDescription = (EditText) dialog.getCustomView().findViewById(R.id.food_edit_description);
        foodPrice = (EditText) dialog.getCustomView().findViewById(R.id.food_edit_price);
        Observable.combineLatest(RxTextView.textChanges(foodName).skip(1)
                , RxTextView.textChanges(foodCategory).skip(1)
                , RxTextView.textChanges(foodPrice).skip(1)
                , (name, category1, price) -> !TextUtils.isEmpty(name) && !TextUtils.isEmpty(category1) && !TextUtils.isEmpty(price))
                .subscribe(aBoolean -> {
                    positiveAction.setEnabled(aBoolean);
                });
        if (!isNew) {
            foodName.setText(food.getName());
            foodCategory.setText(food.getCategory());
            foodDescription.setText(food.getDescription());
            foodPrice.setText(food.getPrice());
        } else {
            foodCategory.setText(category);
        }
        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }

}
