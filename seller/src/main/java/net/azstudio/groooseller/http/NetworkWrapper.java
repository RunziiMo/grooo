package net.azstudio.groooseller.http;


import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.azstudio.groooseller.model.app.PushInfo;
import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.model.business.FoodOrder;
import net.azstudio.groooseller.model.business.Menu;
import net.azstudio.groooseller.model.http.AuthToken;
import net.azstudio.groooseller.model.http.AuthUser;
import net.azstudio.groooseller.model.http.HttpFood;
import net.azstudio.groooseller.model.http.HttpResult;
import net.azstudio.groooseller.model.http.ShopStatus;
import net.azstudio.groooseller.utils.AppManager;
import net.azstudio.groooseller.utils.AppPreferences;
import net.azstudio.groooseller.utils.Logs;
import net.azstudio.groooseller.utils.SmallTools;
import net.azstudio.groooseller.utils.Toasts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by runzii on 16-5-1.
 */
public class NetworkWrapper {

    private ApiService apiService;

    private static final String BASE_URL = "http://api.grooo.cn/api/v3/";

    private static final int DEFAULT_TIMEOUT = 10;

    private Retrofit retrofit;

    //构造方法私有
    private NetworkWrapper() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public Subscription getAuthToken(Subscriber<AuthToken> subscriber, AuthUser user) {
        return apiService.getAuthToken(user)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Observable<ShopInfo> getShopInfo(final int shopid) {
        return apiService.getShopInfo(AppPreferences.get().getAuth(), shopid)
                .onErrorResumeNext(new CheckAuth<ShopInfo>() {
                    @Override
                    protected Observable<HttpResult<ShopInfo>> getObservable(String finalToken) {
                        return apiService.getShopInfo(finalToken, shopid);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> setShopInfo(final int shopid, ShopInfo shopInfo) {
        return apiService.setShopInfo(AppPreferences.get().getAuth(), shopid, shopInfo)
                .onErrorResumeNext(new CheckAuth<String>() {
                    @Override
                    protected Observable<HttpResult<String>> getObservable(String finalToken) {
                        return apiService.setShopInfo(finalToken, shopid, shopInfo);
                    }
                })
                .map(stringHttpResult -> stringHttpResult.getMessage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> setPushInfo(PushInfo pushInfo) {
        return apiService.setPushInfo(AppPreferences.get().getAuth(), AppManager.getShopInfo().getId(), pushInfo)
                .onErrorResumeNext(new CheckAuth<String>() {
                    @Override
                    protected Observable<HttpResult<String>> getObservable(String finalToken) {
                        return apiService.setPushInfo(finalToken, AppManager.getShopInfo().getId(), pushInfo);
                    }
                })
                .map(stringHttpResult -> stringHttpResult.getMessage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> setShopStatus(final int status) {
        ShopStatus shopStatus = new ShopStatus(status);
        return apiService.setShopStatus(AppPreferences.get().getAuth(), AppManager.getShopInfo().getId(), shopStatus)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return apiService.setShopStatus(finalToken, AppManager.getShopInfo().getId(), shopStatus);
                    }
                })
                .map(responseBodyHttpResult -> responseBodyHttpResult.getMessage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Menu>> getMenu(final String token, final int shopid) {
        return apiService.getMenu(token, shopid)
                .onErrorResumeNext(new CheckAuth<List<HttpFood>>() {
                    @Override
                    protected Observable<HttpResult<List<HttpFood>>> getObservable(String finalToken) {
                        return apiService.getMenu(finalToken, shopid);
                    }
                })
                .map(new HttpResultFunc<>())
                .map(httpFoods -> SmallTools.toMenuMap(httpFoods))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Subscription addFood(Action1<ResponseBody> action1, final String token, final int shopid, final HttpFood food) {
        return apiService.addFood(token, shopid, food)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return apiService.addFood(finalToken, shopid, food);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public Subscription editFood(Action1<ResponseBody> action1, final String token, final int shopid, final HttpFood food) {
        return apiService.editFood(token, shopid, food)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return apiService.editFood(finalToken, shopid, food);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public Observable<ResponseBody> editOrder(final FoodOrder order) {
        int shopid = AppManager.getShopInfo().getId();
        return apiService.editOrder(AppPreferences.get().getAuth(), shopid, order)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return apiService.editOrder(finalToken, shopid, order);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<HttpFood> deleteFood(final String token, final int shopid, final List<HttpFood> foods) {
        return Observable.from(foods)
                .flatMap(food -> {
                    Logs.d(food.getName());
                    return apiService.deleteFood(token, shopid, food.getId())
                            .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                                @Override
                                protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                                    return apiService.deleteFood(finalToken, shopid, food.getId());
                                }
                            }).map(new Func1<HttpResult<ResponseBody>, HttpFood>() {
                                @Override
                                public HttpFood call(HttpResult<ResponseBody> responseBodyHttpResult) {
                                    return food;
                                }
                            });
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<FoodOrder>> getOrder(Date start, Date end) {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateStart = myFmt2.format(start);
        String dateEnd = myFmt2.format(end);
        return apiService.getOrder(AppPreferences.get().getAuth(), AppManager.getShopInfo().getId(), dateStart, dateEnd)
                .onErrorResumeNext(new CheckAuth<List<FoodOrder>>() {
                    @Override
                    protected Observable<HttpResult<List<FoodOrder>>> getObservable(String finalToken) {
                        return apiService.getOrder(finalToken, AppManager.getShopInfo().getId(), dateStart, dateEnd);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() >= 300) {
                Toasts.show(httpResult.getMessage());
//                throw new RuntimeException(httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }

    private abstract class CheckAuth<T> implements Func1<Throwable, Observable<HttpResult<T>>> {

        @Override
        public Observable<HttpResult<T>> call(Throwable throwable) {
            if (isHttp401Error(throwable)) {
                Logs.d(throwable.getMessage());
                return apiService.getAuthToken(AppPreferences.get().getAuthUser()).flatMap(authTokenHttpResult -> {
                    String auth = authTokenHttpResult.getData().getToken();
                    AppPreferences.get().setAuth(auth);
                    return getObservable(auth);
                });
            }
            return Observable.error(new Throwable("请重新登录"));
        }

        protected abstract Observable<HttpResult<T>> getObservable(String finalToken);
    }


    private boolean isHttp401Error(Throwable throwable) {
        return "HTTP 401 Unauthorized".equals(throwable.getMessage()) ? true : false;
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final NetworkWrapper INSTANCE = new NetworkWrapper();
    }

    //获取单例
    public static NetworkWrapper get() {
        return SingletonHolder.INSTANCE;
    }


}
