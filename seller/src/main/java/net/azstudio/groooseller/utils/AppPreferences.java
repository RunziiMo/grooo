package net.azstudio.groooseller.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.model.http.AuthUser;

/**
 * Created by runzii on 16-5-1.
 */
public class AppPreferences {

    private static final String AUTH = "auth_groooseller_self";
    private static final String AUTH_USER = "auth_user";
    private static final String SHOP_INFO = "shop_info";

    private static AppPreferences instance;

    private SharedPreferences sh;

    private AppPreferences() {
    }

    private AppPreferences(Context ctx) {
        sh = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static AppPreferences get() {
        if (instance == null) {
            instance = new AppPreferences(AppManager.getAppContext());
        }
        return instance;
    }

    public String getAuth() {
        return sh.getString(AUTH, "");
    }

    public boolean setAuth(String auth) {
        return sh.edit().putString(AUTH, auth).commit();
    }


    public boolean clearAll() {
        return sh.edit().clear().commit();
    }

    public boolean setAuthUser(AuthUser user) {
        return sh.edit().putString(AUTH_USER, user.toString()).commit();
    }

    public AuthUser getAuthUser() {
        return new Gson().fromJson(sh.getString(AUTH_USER, ""), AuthUser.class);
    }

    public boolean setShopInfo(ShopInfo info) {
        return sh.edit().putString(SHOP_INFO, info.toString()).commit();
    }

    public ShopInfo getShopInfo() {
        return new Gson().fromJson(sh.getString(SHOP_INFO, ""), ShopInfo.class);
    }
}
