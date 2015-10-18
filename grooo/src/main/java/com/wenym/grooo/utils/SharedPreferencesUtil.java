package com.wenym.grooo.utils;

import com.wenym.grooo.GroooApplication;

/**
 * Created by Wouldyou on 2015/5/28.
 */
public class SharedPreferencesUtil {

    public static boolean getIsAutoLogin() {
        return GroooApplication.getAppSharedPreferences().getBoolean("isAutoLogin", false);
    }

    public static boolean setIsAutoLogin(boolean b) {
        return GroooApplication.getAppSharedPreferences().edit().putBoolean("isAutoLogin", b).commit();
    }

    public static boolean getIsRememberPassword() {
        return GroooApplication.getAppSharedPreferences().getBoolean("isRememberPassword", false);
    }

    public static boolean setIsRememberPassword(boolean b) {
        return GroooApplication.getAppSharedPreferences().edit().putBoolean("isRememberPassword", b).commit();
    }

    public static String getUsername() {
        return GroooApplication.getAppSharedPreferences().getString("username", null);
    }

    public static boolean setUsername(String username) {
        return GroooApplication.getAppSharedPreferences().edit().putString("username", username).commit();
    }

    public static String getPassword() {
        return GroooApplication.getAppSharedPreferences().getString("password", null);
    }

    public static boolean setPassword(String password) {
        return GroooApplication.getAppSharedPreferences().edit().putString("password", password).commit();
    }

    public static String getUserId() {
        return GroooApplication.getAppSharedPreferences().getString("userid", null);
    }

    public static boolean setUserId(String userid) {
        return GroooApplication.getAppSharedPreferences().edit().putString("userid", userid).commit();
    }

    public static boolean setUserBuilding(String userbuilding) {
        return GroooApplication.getAppSharedPreferences().edit().putString("userbuilding", userbuilding).commit();
    }

    public static String getUserBuilding() {
        return GroooApplication.getAppSharedPreferences().getString("userbuilding", null);
    }

    public static boolean setUserRoom(String roomNum) {
        return GroooApplication.getAppSharedPreferences().edit().putString("roomnum", roomNum).commit();
    }

    public static String getUserRoom() {
        return GroooApplication.getAppSharedPreferences().getString("roomnum", null);
    }

    public static boolean clearAll() {
        return GroooApplication.getAppSharedPreferences().edit().clear().commit();
    }

}
