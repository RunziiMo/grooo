package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class PushInfoData {

    private String userid;
    private String pushid;

    @Override
    public String toString() {
        return "PushInfoData{" +
                "userid='" + userid + '\'' +
                ", pushid='" + pushid + '\'' +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

}
