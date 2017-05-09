package net.azstudio.groooseller.provider;

/**
 * home页数据类型
 * Created by runzii on 15-11-1.
 */
public enum HomeItem {

    TAKEOUT("take_out"),
    MARKET("market"),
    DELIVERY("delivery"),
    SUGGEST("submit_suggestion"),
    ABOUT("aboutus"),
    LOSTTHING("fint_lostthing"),
    NEWS("grooo_news"),
    FAVORITES("favorote_shop"),
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
