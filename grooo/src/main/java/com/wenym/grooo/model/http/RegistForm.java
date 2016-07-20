package com.wenym.grooo.model.http;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by runzii on 16-7-2.
 */
public class RegistForm extends BaseObservable{

    /**
     * username : Runzii
     * password : 123456
     * school_id : 1
     */

    private String username;
    private String password;
    private int school_id;

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }
}
