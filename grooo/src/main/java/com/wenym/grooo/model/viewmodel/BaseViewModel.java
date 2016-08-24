package com.wenym.grooo.model.viewmodel;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by runzii on 16-8-23.
 */
public abstract class BaseViewModel {

    protected Activity activity;

    private CompositeSubscription mCompositeSubscription;

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    public BaseViewModel(Activity activity) {
        this.activity = activity;
    }

    public void onDestroy() {
        activity = null;
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    protected void onResume() {

    }

    protected Action1<Throwable> errorHandle(String message) {
        return throwable -> Snackbar.make(activity.getWindow().getDecorView(), message + ' ' + throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
    }
}
