package com.wenym.grooo.http.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenym.grooo.model.app.Building;

import java.util.ArrayList;

/**
 * Created by Wouldyou on 2015/5/28.
 */
public class InitBuildingsSuccessData {

    private ArrayList<Building> buildings;

    public InitBuildingsSuccessData(String response) {
        buildings = new Gson().fromJson(response, new TypeToken<ArrayList<Building>>() {
        }.getType());
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    @Override
    public String toString() {
        return new Gson().toJson(buildings);
    }
}
