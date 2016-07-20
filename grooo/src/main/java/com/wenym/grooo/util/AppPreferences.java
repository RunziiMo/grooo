package com.wenym.grooo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.model.http.AuthUser;

/**
 * Created by Wouldyou on 2015/5/28.
 */
public class AppPreferences {

    private static final Gson gson = new Gson();

    private static final String AUTH = "grooo_token";
    private static final String AUTH_USER = "grooo_authuser";
    private static final String USER_PROFILE = "grooo_profile";
    private static final String USER_ADDRESS = "grooo_address";

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

    public boolean setProfile(Profile profile) {
        return sh.edit().putString(USER_PROFILE, gson.toJson(profile)).commit();
    }

    public Profile getProfile() {
        return gson.fromJson(sh.getString(USER_PROFILE, ""), Profile.class);
    }

    public boolean setAddress(Address address) {
        return sh.edit().putString(USER_ADDRESS, gson.toJson(address)).commit();
    }

    public Address getAddress() {
        return gson.fromJson(sh.getString(USER_ADDRESS, ""), Address.class);
    }


    private static AppPreferences instance;

    private SharedPreferences sh;

    private AppPreferences() {
    }

    private AppPreferences(Context ctx) {
        sh = PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static AppPreferences get() {
        if (instance == null) {
            instance = new AppPreferences(GroooAppManager.getAppContext());
        }
        return instance;
    }

}
