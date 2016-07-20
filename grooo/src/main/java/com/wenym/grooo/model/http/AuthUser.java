package com.wenym.grooo.model.http;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by runzii on 16-5-7.
 */
public class AuthUser {

    String username;
    String password;

    public AuthUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthUser() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
