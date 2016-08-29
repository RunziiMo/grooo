package com.wenym.grooo.util.RxJava;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

import rx.Observable;

/**
 * Created by runzii on 16-5-25.
 */
public class RxNetWorking {

    public static <T> Observable.Transformer<T, T> bindRefreshing(SwipeRefreshLayout indicator) {
        return observable -> observable
                .doOnSubscribe(() -> indicator.post(() -> indicator.setRefreshing(true)))
                .doOnError(e -> indicator.post(() -> indicator.setRefreshing(false)))
                .doOnCompleted(() -> indicator.post(() -> indicator.setRefreshing(false)));
    }

    public static <T> Observable.Transformer<T, T> bindProgress(ProgressBar progressBar) {
        return observable -> observable
                .doOnSubscribe(() -> {
                    if (progressBar.getVisibility() != View.VISIBLE)
                        progressBar.setVisibility(View.VISIBLE);
                })
                .doOnError(e -> progressBar.setVisibility(View.GONE))
                .doOnCompleted(() -> progressBar.setVisibility(View.GONE));
    }
}
