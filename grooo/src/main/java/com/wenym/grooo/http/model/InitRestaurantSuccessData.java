package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/28.
 */
public class InitRestaurantSuccessData {

    private String[] titles;

    public InitRestaurantSuccessData(String response) {
        titles = response.replaceAll(" ", "").replace(",咕噜", "").split(",");
    }

    public String[] getTitles() {
        return titles;
    }
}
