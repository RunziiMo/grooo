package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class GetMenuData {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GetMenuData{" +
                "id='" + id + '\'' +
                '}';
    }
}
