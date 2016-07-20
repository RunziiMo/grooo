package com.wenym.grooo.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.runzii.lib.utils.Logs;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.model.app.School;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.model.ecnomy.Food;
import com.wenym.grooo.model.ecnomy.Order;
import com.wenym.grooo.model.http.AuthToken;
import com.wenym.grooo.model.http.AuthUser;
import com.wenym.grooo.model.http.ChangePwdForm;
import com.wenym.grooo.model.http.CommentForm;
import com.wenym.grooo.model.http.HttpResult;
import com.wenym.grooo.model.http.OrderForm;
import com.wenym.grooo.model.http.RegistForm;
import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.util.GroooAppManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class NetworkWrapper {

    private static final String BASE_URL = "http://api.grooo.cn/api/v3/";

    private GroooService groooService;

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

        groooService = retrofit.create(GroooService.class);
    }

    public Observable<String> regist(RegistForm registForm) {
        return groooService.regist(registForm)
                .map(responseBodyHttpResult -> responseBodyHttpResult.getMessage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> changePwd(ChangePwdForm changePwdForm) {
        return groooService.changePwd(changePwdForm)
                .map(responseBodyHttpResult -> responseBodyHttpResult.getMessage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<AuthToken> getAuthToken(AuthUser user) {
        return groooService.getAuthToken(user)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<School>> getSchools() {
        return groooService.getSchools()
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<String>> getBuildings(int schoolId) {
        return groooService.getBuildings(schoolId)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Profile> getProfile(int userId) {
        return groooService.getProfile(userId)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> putProfile(Profile profile) {
        return groooService.putProfile(GroooAppManager.getAuthToken(), profile)
                .map(responseBodyHttpResult -> responseBodyHttpResult.getMessage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Order>> getOrders() {
        return groooService.getOrders(GroooAppManager.getAuthToken())
                .onErrorResumeNext(new CheckAuth<List<Order>>() {
                    @Override
                    protected Observable<HttpResult<List<Order>>> getObservable(String finalToken) {
                        return groooService.getOrders(finalToken);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Address> getAddress() {
        return groooService.getAddress(GroooAppManager.getAuthToken())
                .onErrorResumeNext(new CheckAuth<Address>() {
                    @Override
                    protected Observable<HttpResult<Address>> getObservable(String finalToken) {
                        return groooService.getAddress(finalToken);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResponseBody> putAddress(Address address) {
        return groooService.putAddress(GroooAppManager.getAuthToken(), address)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return groooService.putAddress(finalToken, address);
                    }
                })
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Shop>> getAllShop(int schoolId) {
        return groooService.getAllShop(schoolId)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Shop> getShopInfo(int shopId) {
        return groooService.getShopInfo(shopId)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Food>> getShopFood(int shopId) {
        return groooService.getShopFood(shopId)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> postOrder(OrderForm orderForm) {
        return groooService.postOrder(GroooAppManager.getAuthToken(), orderForm)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return groooService.postOrder(finalToken, orderForm);
                    }
                })
                .map(responseBodyHttpResult -> responseBodyHttpResult.getMessage())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseBody> postComment(CommentForm commentForm) {
        return groooService.postComment(GroooAppManager.getAuthToken(), commentForm)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return groooService.postComment(finalToken, commentForm);
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


    private abstract class CheckAuth<T> implements Func1<Throwable, Observable<HttpResult<T>>>

    {

        @Override
        public Observable<HttpResult<T>> call(Throwable throwable) {
            if (isHttp401Error(throwable)) {
                Logs.d(throwable.getMessage());
                return groooService.getAuthToken(AppPreferences.get().getAuthUser()).flatMap(authTokenHttpResult -> {
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