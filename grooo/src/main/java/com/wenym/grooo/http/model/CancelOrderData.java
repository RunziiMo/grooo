package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class CancelOrderData {

    private String userid;
    private String did;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    @Override
    public String toString() {
        return "CancelOrderData{" +
                "userid='" + userid + '\'' +
                ", did='" + did + '\'' +
                '}';
    }
}
