package com.wenym.grooo.provider;

/**
 * home页数据类型
 * Created by runzii on 15-11-1.
 */
public enum HomeItem {

    TAKEOUT("take_out"),
    MARKET("market"),
    DELIVERY("delivery"),
    ABOUT("aboutus"),
    SUGGEST("submit_suggestion"),
    ORDER("last_order");

    private String text;

    private HomeItem(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
