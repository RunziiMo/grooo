package com.wenym.grooo.http.model;

import com.wenym.grooo.model.DeliveryOrder;
import com.wenym.grooo.model.FoodOrder;

import java.util.List;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class GetOrderSuccessData {
    private List<FoodOrder> restaurant;
    private List<DeliveryOrder> delivery;

    public List<FoodOrder> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<FoodOrder> restaurant) {
        this.restaurant = restaurant;
    }

    public List<DeliveryOrder> getDelivery() {
        return delivery;
    }

    public void setDelivery(List<DeliveryOrder> delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "GetOrderSuccessData{" +
                "restaurant=" + restaurant +
                ", delivery=" + delivery +
                '}';
    }
}
