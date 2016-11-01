package com.wenym.grooo.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.runzii.lib.utils.Logs;
import com.wenym.grooo.BuildConfig;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.model.app.School;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.model.app.Food;
import com.wenym.grooo.model.app.Order;
import com.wenym.grooo.model.http.AuthToken;
import com.wenym.grooo.model.http.AuthUser;
import com.wenym.grooo.model.http.ChangePwdForm;
import com.wenym.grooo.model.http.CommentForm;
import com.wenym.grooo.model.http.HttpResult;
import com.wenym.grooo.model.http.OrderForm;
import com.wenym.grooo.model.http.RegisterForm;
import com.wenym.grooo.util.AppPreferences;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
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

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(logging);
        }

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        groooService = retrofit.create(GroooService.class);
    }

    public Observable<String> regist(RegisterForm registerForm) {
        return groooService.regist(registerForm)
                .map(HttpResult::getMessage)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> changePwd(ChangePwdForm changePwdForm) {
        return groooService.changePwd(changePwdForm)
                .map(HttpResult::getMessage)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<AuthToken> getAuthToken(AuthUser user) {
        return groooService.getAuthToken(user)
                .map(retrofit2.Response::body)
                .map(HttpResult::getData)
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
        return groooService.putProfile(AppPreferences.get().getAuth(), profile.getId(), profile)
                .map(HttpResult::getMessage)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Order>> getOrders() {
        return groooService.getOrders(AppPreferences.get().getAuth())
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
        return groooService.getAddress(AppPreferences.get().getAuth())
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


    public Observable<String> putAddress(Address address) {
        return groooService.putAddress(AppPreferences.get().getAuth(), address)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return groooService.putAddress(finalToken, address);
                    }
                })
                .map(HttpResult::getMessage)
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

    public Observable<String> postOrder(int id, OrderForm orderForm) {
        return groooService.postOrder(AppPreferences.get().getAuth(), id, orderForm)
                .onErrorReturn(new CheckError<>())
                .map(HttpResult::getMessage)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> postComment(CommentForm commentForm) {
        return groooService.postComment(AppPreferences.get().getAuth(), commentForm)
                .onErrorResumeNext(new CheckAuth<ResponseBody>() {
                    @Override
                    protected Observable<HttpResult<ResponseBody>> getObservable(String finalToken) {
                        return groooService.postComment(finalToken, commentForm);
                    }
                })
                .map(responseBodyHttpResult -> responseBodyHttpResult.getCode() < 300)
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
                throw new RuntimeException(httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }


    private abstract class CheckAuth<T> implements Func1<Throwable, Observable<HttpResult<T>>> {

        @Override
        public Observable<HttpResult<T>> call(Throwable throwable) {
            Logs.d(throwable.getMessage());
            if (isHttp401Error(throwable)) {
                return groooService.getAuthToken(AppPreferences.get().getAuthUser()).flatMap(authTokenHttpResult -> {
                    String auth = authTokenHttpResult.body().getData().getToken();
                    AppPreferences.get().setAuth(auth);
                    return getObservable(auth);
                });
            }
            return Observable.error(new Throwable("请重新登录"));
        }

        protected abstract Observable<HttpResult<T>> getObservable(String finalToken);
    }

    private class CheckError<T> implements Func1<Throwable, HttpResult<T>> {

        @Override
        public HttpResult<T> call(Throwable throwable) {
            Log.d("postOrder", throwable.toString());
            HttpResult<T> responseError = null;
            if (throwable instanceof HttpException) {
                try {
                    String errorBody = ((HttpException) throwable).response().errorBody().string();
                    Type type = new TypeToken<HttpResult<ResponseBody>>() {
                    }.getType();
                    responseError = new Gson().fromJson(errorBody, type);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(throwable.getMessage());
                }
            }
            return responseError;
        }

    }


    private boolean isHttp401Error(Throwable throwable) {
        return "HTTP 401 Unauthorized".equals(throwable.getMessage());
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