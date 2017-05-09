package net.azstudio.groooseller.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.http.NetworkWrapper;
import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.ui.base.BaseActivity;
import net.azstudio.groooseller.utils.AppManager;
import net.azstudio.groooseller.utils.Logs;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by runzii on 16-5-29.
 */
public class ManageActivity extends BaseActivity {

    private Observable<ShopInfo> shopInfoObservable;
    private Observable<String> setInfoObservable;

    @BindView(R.id.iv_user_logo)
    ImageView logo;
    @BindView(R.id.tv_user_monthsold)
    TextView monthsold;
    @BindView(R.id.tv_user_rating)
    TextView rating;
    @BindView(R.id.tv_user_ratenum)
    TextView ratenum;
    @BindView(R.id.tv_user_name)
    TextView name;
    @BindView(R.id.tv_user_phone)
    TextView phone;
    @BindView(R.id.tv_user_baseprice)
    TextView baseprice;
    @BindView(R.id.tv_user_description)
    TextView description;
    @BindView(R.id.tv_user_activity)
    TextView activity;
    @BindView(R.id.iv_user_name_logo)
    ImageView name_logo;
    @BindView(R.id.iv_user_phone_logo)
    ImageView phone_logo;
    @BindView(R.id.iv_user_baseprice_logo)
    ImageView baseprice_logo;
    @BindView(R.id.iv_user_description_logo)
    ImageView description_logo;
    @BindView(R.id.iv_user_activity_logo)
    ImageView activity_logo;

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
        return R.layout.activity_shopmanage;
    }

    private ShopInfo shopInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopInfo = new ShopInfo();
        applyShopInfo(AppManager.getShopInfo());
        shopInfoObservable = NetworkWrapper.get()
                .getShopInfo(AppManager.getShopInfo().getId())
                .compose(bindToLifecycle());
        setInfoObservable = NetworkWrapper.get()
                .setShopInfo(AppManager.getShopInfo().getId(), shopInfo)
                .compose(bindToLifecycle());
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_store)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(name_logo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_local_phone)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(phone_logo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_attach_money)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(baseprice_logo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_description)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(description_logo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_local_activity)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(activity_logo);
        RxToolbar.itemClicks(getToolbar())
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(menuItem -> menuItem.getItemId())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onToolbarItemClicked);
        Observable.combineLatest(RxTextView.textChanges(name).skip(1)
                , RxTextView.textChanges(phone).skip(1)
                , RxTextView.textChanges(baseprice).skip(1)
                , RxTextView.textChanges(description).skip(1)
                , RxTextView.textChanges(activity).skip(1)
                , (name, phone, baseprice, description, activity) -> {
                    shopInfo.setName(name.toString());
                    shopInfo.setPhone(phone.toString());
                    shopInfo.setBasePrice(baseprice.toString());
                    shopInfo.setDescription(description.toString());
                    shopInfo.setActivity(activity.toString());
                    return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)
                            && !TextUtils.isEmpty(baseprice)
                            && !TextUtils.isEmpty(description);
                })
                .subscribe(aBoolean -> {
                    isCheckable = aBoolean;
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopInfoObservable.subscribe(shopInfo -> {
                    AppManager.setShopInfo(shopInfo);
                    applyShopInfo(shopInfo);
                }
                , throwable -> Snackbar.make(name, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.manage_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isEditMode = false;
    private boolean isCheckable = false;

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem check = menu.findItem(R.id.action_check);
        if (isEditMode) {
            edit.setVisible(false);
        } else {
            check.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void onToolbarItemClicked(int itemId) {
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_edit:
                setEditMode(true);
                supportInvalidateOptionsMenu();
                break;
            case R.id.action_check:
                if (isCheckable) {
                    Logs.d(shopInfo.toString());
                    setInfoObservable.subscribe(s -> {
                                Snackbar.make(name, s, Snackbar.LENGTH_SHORT).show();
                                setEditMode(false);
                                supportInvalidateOptionsMenu();
                            }
                            , throwable -> Snackbar.make(name, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
                } else {
                    Snackbar.make(name, "请输入完整信息", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void setEditMode(boolean editable) {
        isEditMode = editable;
        name.setEnabled(editable);
        phone.setEnabled(editable);
        baseprice.setEnabled(editable);
        description.setEnabled(editable);
        activity.setEnabled(editable);
    }

    private void applyShopInfo(ShopInfo shopInfo) {
        Glide.with(this).load(shopInfo.getLogo()).placeholder(R.drawable.login_logo).into(logo);
        name.setText(shopInfo.getName());
        monthsold.setText(shopInfo.getMonthSold());
        rating.setText(shopInfo.getRating());
        ratenum.setText(shopInfo.getRateNumber());
        phone.setText(shopInfo.getPhone());
        baseprice.setText(shopInfo.getBasePrice());
        description.setText(shopInfo.getDescription());
        activity.setText(shopInfo.getActivity());
    }

}
