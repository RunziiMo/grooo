package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class CancelOrderSuccessData {

    private String info;

    public CancelOrderSuccessData(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CancelOrderSuccessData{" +
                "info='" + info + '\'' +
                '}';
    }
}
