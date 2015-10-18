package com.wenym.grooo.model;

import java.util.ArrayList;

public class Menu {

    private String foodclass;

    private ArrayList<Food> foodlist;

    public String getFoodclass() {
        return foodclass;
    }

    public void setFoodclass(String foodclass) {
        this.foodclass = foodclass;
    }

    public ArrayList<Food> getFoodlist() {
        return foodlist;
    }

    public void setFoodlist(ArrayList<Food> foodlist) {
        this.foodlist = foodlist;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "foodclass='" + foodclass + '\'' +
                ", foodlist=" + foodlist +
                '}';
    }
}
