package net.azstudio.groooseller.utils.RxJava;

import android.support.v4.widget.SwipeRefreshLayout;

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
}
