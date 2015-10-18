package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class GetOrderData {

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "GetOrderData{" +
                "userid='" + userid + '\'' +
                '}';
    }
}
