package com.wenym.grooo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenym.grooo.http.model.InitBuildingsSuccessData;
import com.wenym.grooo.model.app.AppUser;
import com.wenym.grooo.model.app.Building;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Wouldyou on 2015/5/28.
 */
public class PreferencesUtil {

    private static PreferencesUtil instance;

    private SharedPreferences sh;

    private PreferencesUtil() {
    }

    private PreferencesUtil(Context ctx) {
        sh = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static PreferencesUtil getInstance() {
        if (instance == null) {
            instance = new PreferencesUtil(GroooAppManager.getAppContext());
        }
        return instance;
    }

    public boolean getIsAutoLogin() {
        return sh.getBoolean(SPsettings.AUTOLOGIN.toString(), false);
    }

    public boolean setIsAutoLogin(boolean b) {
        return sh.edit().putBoolean(SPsettings.AUTOLOGIN.toString(), b).commit();
    }

    public boolean getIsRememberPassword() {
        return sh.getBoolean(SPsettings.REMEMBERPASSWORD.toString(), false);
    }

    public boolean setIsRememberPassword(boolean b) {
        return sh.edit().putBoolean(SPsettings.REMEMBERPASSWORD.toString(), b).commit();
    }

    public boolean setAppUser(AppUser user) {
        return sh.edit().putString(SPsettings.APPUSER.toString(), user.toString()).commit();
    }

    public AppUser getAppUser() {
        return new Gson().fromJson(sh.getString(SPsettings.APPUSER.toString(), ""), AppUser.class);
    }

    public ArrayList<Building> getBuildings() {
        return new Gson().fromJson(sh.getString(SPsettings.BUILDINGS.toString(), ""), new TypeToken<ArrayList<Building>>() {
        }.getType());
    }

    public boolean setBuildings(InitBuildingsSuccessData buildings) {
        return sh.edit().putString(SPsettings.BUILDINGS.toString(), buildings.toString()).commit();
    }

    public Set<String> getFavoriteShops(){
        return sh.getStringSet(SPsettings.FAVOROTE_RESTAURANT.toString(), new HashSet<String>());
    }

    public boolean setFavoriteShops(Set<String> favorites){
        return sh.edit().putStringSet(SPsettings.FAVOROTE_RESTAURANT.toString(),favorites).commit();
    }

    public boolean clearAll() {
        return sh.edit().clear().commit();
    }

    public enum SPsettings {

        REMEMBERPASSWORD("isRememberPassword"),
        AUTOLOGIN("isAutoLogin"),
        APPUSER("appUser"),
        BUILDINGS("buildingList"),
        FAVOROTE_RESTAURANT("favoriteRestaurants");

        private String setting;

        private SPsettings(String setting) {
            this.setting = setting;
        }

        @Override
        public String toString() {
            return setting;
        }
    }

}
