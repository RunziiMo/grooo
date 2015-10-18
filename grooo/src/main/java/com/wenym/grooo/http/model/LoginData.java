package com.wenym.grooo.http.model;

import com.wenym.grooo.http.util.HttpConstants;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

@SuppressWarnings("deprecation")
public class LoginData {

    private String username;
    private String password;
    private StringEntity se;

    public LoginData(String username, String password) {
        super();
        this.username = username;
        this.password = password;
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
                    + password);
            se.setContentType(HttpConstants.contentType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return se;
    }

}
