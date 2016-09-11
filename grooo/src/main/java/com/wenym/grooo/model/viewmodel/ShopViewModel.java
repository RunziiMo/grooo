package com.wenym.grooo.model.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.databinding.ObservableMap;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.runzii.lib.utils.Logs;
import com.runzii.lib.widgets.behavior.BottomSheetAnchorBehavior;
import com.wenym.grooo.R;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Food;
import com.wenym.grooo.model.app.Order;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.model.http.OrderForm;
import com.wenym.grooo.ui.activity.EditActivity;
import com.wenym.grooo.ui.activity.MainActivity;
import com.wenym.grooo.ui.profile.BuildingActivity;
import com.wenym.grooo.ui.shop.PayListAdapter;
import com.wenym.grooo.ui.shop.ShopActivity;
import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.util.RxEvent.ClearEvent;
import com.wenym.grooo.util.RxEvent.FoodEvent;
import com.wenym.grooo.util.RxJava.RxBus;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.widgets.behavior.MergedAppBarLayoutBehavior;

import java.util.LinkedList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by runzii on 16-8-26.
 */
public class ShopViewModel extends BaseViewModel {

    public ObservableBoolean isBottomExpanded = new ObservableBoolean(false);

    public ObservableFloat totalPrice = new ObservableFloat(0.0f);

    public ObservableInt bottomSheetState = new ObservableInt(BottomSheetAnchorBehavior.STATE_COLLAPSED);

    public ObservableMap<Food, Integer> orders = new ObservableArrayMap<>();

    public ObservableField<String> building = new ObservableField<>();

    public ObservableField<String> room = new ObservableField<>();

    public ObservableField<String> remark = new ObservableField<>();

    private boolean status;
    public float minPrice;
    private int id;

    public ShopViewModel(Activity activity, AppBarLayout mergedAppBar, Shop shop) {
        super(activity);
        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBar);
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(this::collapseBottomSheet);
        Subscription s = RxBus.getDefault()
                .toObserverable(FoodEvent.class)
                .subscribe(this::processFoodEvent);
        addSubscription(s);
        status = shop.getStatus() == 1;
        minPrice = shop.getBasePrice();
        id = shop.getId();
        Address address = AppPreferences.get().getAddress();
        if (address != null) {
            building.set(address.getBuilding());
            room.set(address.getAddress());
        }
    }

    private void processFoodEvent(FoodEvent foodEvent) {
        final Food food = foodEvent.getFood();
        if (foodEvent.isAdd()) {
            totalPrice.set(totalPrice.get() + Float.parseFloat(food.getPrice()));
            if (orders.containsKey(food)) {
                orders.put(food, orders.get(food) + 1);
            } else {
                orders.put(food, 1);
            }
        } else {
            if (orders.containsKey(food)) {
                if (orders.get(food) == 1) {
                    orders.remove(food);
                } else {
                    orders.put(food, orders.get(food) - 1);
                }
                totalPrice.set(totalPrice.get() - Float.parseFloat(food.getPrice()));
            } else {
                Toasts.show("本来就没有");
            }
        }
    }

    public void setBuilding(View view) {
        Intent intent = new Intent(activity, BuildingActivity.class);
        activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_BUILDING);
    }

    public void setRoom(View view) {
        EditActivity.lanuch(activity, "请在此输入寝室号", "修改寝室号", EditActivity.ROOM, MainActivity.REQUEST_CODE_ROOM);
    }

    public void setRemark(View view) {
        EditActivity.lanuch(activity, "请在此输入备注", "给商家留言", EditActivity.REMARK, MainActivity.REQUEST_CODE_REMARK);
    }

    public void showBottomOrBuy(View view) {
        if (bottomSheetState.get() != BottomSheetAnchorBehavior.STATE_ANCHORED)
            bottomSheetState.set(BottomSheetAnchorBehavior.STATE_ANCHORED);
        else {
            if (TextUtils.isEmpty(room.get()) || TextUtils.isEmpty(building.get())) {
                Toasts.show("请选择配送地点");
                return;
            }
            if (totalPrice.get() < minPrice) {
                Toasts.show("未达到起送价");
                return;
            }
            if (!status) {
                Toasts.show("商家未营业");
//                return;
            }
            if (orders.size() <= 0) {
                Toasts.show("请选择商品");
                return;
            }
            OrderForm form = new OrderForm();
            List<Order.DetailBean> beanList = new LinkedList<>();
            for (Food food : orders.keySet()) {
                Order.DetailBean bean = new Order.DetailBean();
                bean.setName(food.getName());
                bean.setCount(orders.get(food).toString());
                beanList.add(bean);
            }
            form.setDetail(beanList);
            form.setAddress(room.get());
            form.setBuilding(building.get());
            form.setRemark(remark.get());
            new MaterialDialog.Builder(activity)
                    .content("确定要下单吗？")
                    .positiveText(android.R.string.ok)
                    .onPositive((dialog, which) -> {
                        Subscription subscription = NetworkWrapper.get().postOrder(id,form)
                                .subscribe(s -> {
                                    Snackbar.make(activity.getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
                                });
                        addSubscription(subscription);
                    })
                    .show();
        }
    }

    public void clearOrder(View view) {
        RxBus.getDefault().post(new ClearEvent());
        orders.clear();
        totalPrice.set(0);
    }

    public void collapseBottomSheet(View view) {
        ((ShopActivity) activity).toggleSlidingUpLayout(BottomSheetAnchorBehavior.STATE_COLLAPSED);
    }

    public void setAddress(Address address) {
        Subscription s = NetworkWrapper.get()
                .putAddress(address)
                .subscribe(s1 -> {
                    Log.d("setAddress", s1);
                    building.set(address.getBuilding());
                    room.set(address.getAddress());
                    AppPreferences.get().setAddress(address);
                }, errorHandle("设置地址"));
        addSubscription(s);
    }

    @BindingAdapter("bottom_sheet_state")
    public static void setBottomSheetState(View view, int state) {
        BottomSheetAnchorBehavior behavior = BottomSheetAnchorBehavior.from(view);
        behavior.setState(state);
    }

    @BindingAdapter("entries_orders")
    public static void entriesBasket(RecyclerView recyclerView, ObservableMap<Food, Integer> orders) {
        if (recyclerView.getAdapter() == null)
            recyclerView.setAdapter(new PayListAdapter(orders));
        else {
            Logs.d("entries_orders调用");
            PayListAdapter adapter = (PayListAdapter) recyclerView.getAdapter();
            adapter.setBasket(orders);
        }
    }

    @BindingAdapter("animation")
    public static void setAnimation(LinearLayout view, boolean isAnimation) {
        if (view.getTag() == null) {
            view.setTag(true);
            return;
        }
        Animator animator;
        if (isAnimation) {
            animator = AnimatorInflater.loadAnimator(view.getContext(), R.animator.bottom_back_expand);
        } else {
            animator = AnimatorInflater.loadAnimator(view.getContext(), R.animator.bottom_back_collapse);
        }
        animator.setTarget(view);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                int backColor = ContextCompat.getColor(view.getContext(), isAnimation ? R.color.colorPrimary : R.color.white);
                view.setBackgroundColor(backColor);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

}
