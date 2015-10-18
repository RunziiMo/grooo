package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class SuggestData {

    private String content;
    private String userid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "SuggestData{" +
                "content='" + content + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
