package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class PushInfoData {

    private String uid;
    private String userid;
    private String channelid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    @Override
    public String toString() {
        return "PushInfoData{" +
                "uid='" + uid + '\'' +
                ", userid='" + userid + '\'' +
                ", channelid='" + channelid + '\'' +
                '}';
    }
}
