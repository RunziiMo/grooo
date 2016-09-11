package com.wenym.grooo.util.RxEvent;


import com.wenym.grooo.model.app.Food;

/**
 * Created by runzii on 16-5-23.
 */
public class FoodEvent {

    private final Food food;
    private final boolean isAdd;

    public FoodEvent(Food food, boolean isAdd) {
        this.food = food;
        this.isAdd = isAdd;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public Food getFood() {
        return food;
    }
}
