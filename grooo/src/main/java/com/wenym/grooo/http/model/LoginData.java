package com.wenym.grooo.http.model;

import com.wenym.grooo.http.util.HttpConstants;


import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

@SuppressWarnings("deprecation")
public class LoginData {

    private String username;
    private String password;
    private String pushid;
    private StringEntity se;

    public String getPushid() {
        return pushid;
    }

    public LoginData(String username, String password, String pushid) {
        this.username = username;
        this.password = password;
        this.pushid = pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StringEntity toStringEntity() {
        try {
            se = new StringEntity("username=" + username + "&password="
                    + password + "&pushid=" + pushid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return se;
    }

}
