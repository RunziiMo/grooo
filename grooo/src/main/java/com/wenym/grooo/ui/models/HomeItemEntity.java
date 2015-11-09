package com.wenym.grooo.ui.models;

import com.wenym.grooo.provider.HomeItem;

import java.util.Random;

/**
 * home页的封装数据
 * Created by runzii on 15-11-1.
 */
public class HomeItemEntity {


    private HomeItem kind;
    private String homeitem_title;
    private String homeitem_content;
    private String homeitem_back;
    private String homeitem_avator;
    private int homeiten_height = new Random().nextInt(200)+120;

    public int getHomeiten_height() {
        return homeiten_height;
    }

    public void setHomeiten_height(int homeiten_height) {
        this.homeiten_height = homeiten_height;
    }

    public HomeItem getKind() {
        return kind;
    }

    public void setKind(HomeItem kind) {
        this.kind = kind;
    }

    public String getHomeitem_title() {
        return homeitem_title;
    }

    public void setHomeitem_title(String homeitem_title) {
        this.homeitem_title = homeitem_title;
    }

    public String getHomeitem_content() {
        return homeitem_content;
    }

    public void setHomeitem_content(String homeitem_content) {
        this.homeitem_content = homeitem_content;
    }

    public String getHomeitem_back() {
        return homeitem_back;
    }

    public void setHomeitem_back(String homeitem_back) {
        this.homeitem_back = homeitem_back;
    }

    public String getHomeitem_avator() {
        return homeitem_avator;
    }

    public void setHomeitem_avator(String homeitem_avator) {
        this.homeitem_avator = homeitem_avator;
    }
}
