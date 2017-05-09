package net.azstudio.groooseller.http;


import net.azstudio.groooseller.model.app.PushInfo;
import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.model.business.FoodOrder;
import net.azstudio.groooseller.model.http.HttpFood;
import net.azstudio.groooseller.model.http.AuthToken;
import net.azstudio.groooseller.model.http.AuthUser;
import net.azstudio.groooseller.model.http.HttpResult;
import net.azstudio.groooseller.model.http.ShopStatus;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by runzii on 16-4-13.
 */
public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("accounts/token")
    Observable<HttpResult<AuthToken>> getAuthToken(@Body AuthUser user);

    @Headers("Content-Type: application/json")
    @GET("shops/shop/{id}/info")
    Observable<HttpResult<ShopInfo>> getShopInfo(@Header("Authorization") String token, @Path("id") int shopid);

    @Headers("Content-Type: application/json")
    @PATCH("shops/shop/{id}/info")
    Observable<HttpResult<String>> setShopInfo(@Header("Authorization") String token, @Path("id") int shopid, @Body ShopInfo shopInfo);

    @Headers("Content-Type: application/json")
    @PUT("accounts/{id}/profile")
    Observable<HttpResult<String>> setPushInfo(@Header("Authorization") String token, @Path("id") int shopid, @Body PushInfo pushInfo);

    @Headers("Content-Type: application/json")
    @PUT("shops/shop/{id}/status")
    Observable<HttpResult<ResponseBody>> setShopStatus(@Header("Authorization") String token, @Path("id") int shopid, @Body ShopStatus status);

    @Headers("Content-Type: application/json")
    @GET("shops/shop/{id}/menu")
    Observable<HttpResult<List<HttpFood>>> getMenu(@Header("Authorization") String token, @Path("id") int shopid);

    @Headers("Content-Type: application/json")
    @POST("shops/shop/{id}/menu")
    Observable<HttpResult<ResponseBody>> addFood(@Header("Authorization") String token, @Path("id") int shopid, @Body HttpFood food);

    @Headers("Content-Type: application/json")
    @PUT("shops/shop/{id}/menu")
    Observable<HttpResult<ResponseBody>> editFood(@Header("Authorization") String token, @Path("id") int shopid, @Body HttpFood food);

    @Headers("Content-Type: application/json")
    @DELETE("shops/shop/{id}/menu")
    Observable<HttpResult<ResponseBody>> deleteFood(@Header("Authorization") String token, @Path("id") int shopid, @Query("id") int foodid);

    @Headers("Content-Type: application/json")
    @DELETE("shops/shop/{id}/menu")
    Observable<HttpResult<ResponseBody>> deleteFoodByCategory(@Header("Authorization") String token, @Path("id") int shopid, @Query("category") String foodid);

    @Headers("Content-Type: application/json")
    @GET("shops/shop/{id}/order")
    Observable<HttpResult<List<FoodOrder>>> getOrder(@Header("Authorization") String token, @Path("id") int shopid, @Query("start_time") String start, @Query("end_time") String end);

    @Headers("Content-Type: application/json")
    @PUT("shops/shop/{id}/order")
    Observable<HttpResult<ResponseBody>> editOrder(@Header("Authorization") String token, @Path("id") int shopid, @Body FoodOrder order);
}
