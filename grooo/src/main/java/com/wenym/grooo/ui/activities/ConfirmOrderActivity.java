package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityConfirmOrderBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Basket;
import com.wenym.grooo.model.http.OrderForm;
import com.wenym.grooo.provider.ExtraActivityKeys;

/**
 * Created by runzii on 15-11-7.
 */
public class ConfirmOrderActivity extends BaseActivity<ActivityConfirmOrderBinding> {

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
        return R.layout.activity_confirm_order;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OrderForm form = Basket.INSTANCE.getOrder();
        bind.setForm(form);
    }

    public void confirmOrder(View view) {
        Basket basket = Basket.INSTANCE;
        switch (basket.isOkToPay()) {
            case OK:
                break;
            case RESTING:
                Toasts.show("商家已休息");
                return;
            case NOTENOUGH:
                Toasts.show("未达到起送价");
                return;
        }
        NetworkWrapper.get().postOrder(bind.getForm())
                .compose(bindToLifecycle())
                .subscribe(s -> {
                    Snackbar.make(bind.confirmPay, s, Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(ExtraActivityKeys.CONFIRMPAY.toString(), true);
                    intent.putExtra(ExtraActivityKeys.PAYINFO.toString(), s);
                    setResult(RESULT_OK, intent);
                    finish();
                });
    }
}
