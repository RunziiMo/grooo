package net.azstudio.groooseller.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.ActivityShopmanageBinding;
import net.azstudio.groooseller.http.NetworkWrapper;
import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.ui.base.BaseActivity;
import net.azstudio.groooseller.utils.AppManager;
import net.azstudio.groooseller.utils.Logs;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by runzii on 16-5-29.
 */
public class ManageActivity extends BaseActivity<ActivityShopmanageBinding> {

    private Observable<ShopInfo> shopInfoObservable;
    private Observable<String> setInfoObservable;

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
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(binding.ivUserNameLogo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_local_phone)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(binding.ivUserPhoneLogo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_attach_money)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(binding.ivUserBasepriceLogo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_description)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(binding.ivUserDescriptionLogo);
        Glide.with(this).load("").placeholder(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_local_activity)
                .sizeDp(36).colorRes(R.color.colorAccent).paddingRes(R.dimen.padding_icon)).into(binding.ivUserActivityLogo);
        RxToolbar.itemClicks(getToolbar())
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(menuItem -> menuItem.getItemId())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onToolbarItemClicked);
        Observable.combineLatest(RxTextView.textChanges(binding.tvUserName).skip(1)
                , RxTextView.textChanges(binding.tvUserPhone).skip(1)
                , RxTextView.textChanges(binding.tvUserBaseprice).skip(1)
                , RxTextView.textChanges(binding.tvUserDescription).skip(1)
                , RxTextView.textChanges(binding.tvUserActivity).skip(1)
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
                , throwable -> Snackbar.make(binding.tvUserName, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
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
                                Snackbar.make(binding.tvUserName, s, Snackbar.LENGTH_SHORT).show();
                                setEditMode(false);
                                supportInvalidateOptionsMenu();
                            }
                            , throwable -> Snackbar.make(binding.tvUserName, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
                } else {
                    Snackbar.make(binding.tvUserName, "请输入完整信息", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void setEditMode(boolean editable) {
        isEditMode = editable;
        binding.tvUserName.setEnabled(editable);
        binding.tvUserPhone.setEnabled(editable);
        binding.tvUserBaseprice.setEnabled(editable);
        binding.tvUserDescription.setEnabled(editable);
        binding.tvUserActivity.setEnabled(editable);
    }

    private void applyShopInfo(ShopInfo shopInfo) {
        Glide.with(this).load(shopInfo.getLogo()).placeholder(R.drawable.login_logo).into(binding.ivUserLogo);
        binding.tvUserName.setText(shopInfo.getName());
        binding.tvUserMonthsold.setText(shopInfo.getMonthSold());
        binding.tvUserRating.setText(shopInfo.getRating());
        binding.tvUserRatenum.setText(shopInfo.getRateNumber());
        binding.tvUserPhone.setText(shopInfo.getPhone());
        binding.tvUserBaseprice.setText(shopInfo.getBasePrice());
        binding.tvUserDescription.setText(shopInfo.getDescription());
        binding.tvUserActivity.setText(shopInfo.getActivity());
    }

}
