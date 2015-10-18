package com.wenym.grooo.http.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenym.grooo.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class GetRestaurantSuccessData {

    private ArrayList<Restaurant> restaurants;

    public GetRestaurantSuccessData(String response) {
        restaurants = new Gson().fromJson(response, new TypeToken<ArrayList<Restaurant>>() {
        }.getType());
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }
}
