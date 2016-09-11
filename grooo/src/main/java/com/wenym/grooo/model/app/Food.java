package com.wenym.grooo.model.app;

import android.databinding.ObservableInt;

public class Food {


    /**
     * category : 巴氏消毒奶
     * description :
     * id : 10972
     * logo :
     * monthSold : 0
     * name : 2-多人15天（每人赠一瓶鲜奶）
     * price : 75
     * remain : 99999
     */

    private String category;
    private String description;
    private int id;
    private String logo;
    private int monthSold;
    private String name;
    private String price;
    private int remain;

    //购买数量，本地数据，只和下单相关
    public ObservableInt buyNum = new ObservableInt(0);

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getMonthSold() {
        return monthSold;
    }

    public void setMonthSold(int monthSold) {
        this.monthSold = monthSold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}
