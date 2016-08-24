package com.wenym.grooo.http;

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
import com.wenym.grooo.model.http.RegistForm;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface GroooService {

    @POST("accounts")
    Observable<HttpResult<ResponseBody>> regist(@Body RegistForm registForm);

    @PUT("accounts")
    Observable<HttpResult<ResponseBody>> changePwd(@Body ChangePwdForm changePwdForm);

    @Headers("Content-Type: application/json")
    @POST("accounts/token")
    Observable<HttpResult<AuthToken>> getAuthToken(@Body AuthUser user);

    @GET("accounts/school")
    Observable<HttpResult<ArrayList<School>>> getSchools();

    @GET("shops/building")
    Observable<HttpResult<List<String>>> getBuildings(@Query("school_id") int schoolId);

    @GET("accounts/{id}/profile")
    Observable<HttpResult<Profile>> getProfile(@Path("id") int userId);

    @PUT("accounts/{id}/profile")
    Observable<HttpResult<ResponseBody>> putProfile(@Header("Authorization") String auth,@Path("id") int userId, @Body Profile profile);

    @GET("shops/user/order")
    Observable<HttpResult<List<Order>>> getOrders(@Header("Authorization") String auth);

    @GET("shops/user/profile")
    Observable<HttpResult<Address>> getAddress(@Header("Authorization") String auth);

    @PUT("shops/user/profile")
    Observable<HttpResult<ResponseBody>> putAddress(@Header("Authorization") String auth, @Body Address address);

    @GET("shops/shop")
    Observable<HttpResult<List<Shop>>> getAllShop(@Query("school_id") int schoolId);

    @GET("shops/shop/{id}/info")
    Observable<HttpResult<Shop>> getShopInfo(@Path("id") int shopId);

    @GET("shops/shop/{id}/menu")
    Observable<HttpResult<List<Food>>> getShopFood(@Path("id") int shopId);

    @POST("shops/shop/{id}/order")
    Observable<HttpResult<ResponseBody>> postOrder(@Header("Authorization") String auth, @Body OrderForm orderForm);

    @POST("shops/user/order")
    Observable<HttpResult<ResponseBody>> postComment(@Header("Authorization") String auth, @Body CommentForm commentForm);

}
