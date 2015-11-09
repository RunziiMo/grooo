package com.wenym.grooo.utils;

import com.wenym.grooo.model.Building;

import java.util.ArrayList;

/**
 * 小工具，乱七八糟
 * Created by Wouldyou on 2015/5/29.
 */
public class SmallTools {

    public static String[] toArrayBuildings(ArrayList<Building> buildings) {
        String[] builds = new String[buildings.size()];
        for (int i = 0; i < buildings.size(); i++) {
            builds[i] = buildings.get(i).getText();
        }
        return builds;
    }

    public static String buildingtextToId(ArrayList<Building> buildings, String text) {
        for (int i = 0; i < buildings.size(); i++) {
            if (text.equals(buildings.get(i).getText())) {
                return buildings.get(i).getId();
            }
        }
        return null;
    }

    public static String buildingIdToText(ArrayList<Building> buildings, String num) {
        for (int i = 0; i < buildings.size(); i++) {
            if (num.equals(buildings.get(i).getId())) {
                return buildings.get(i).getText();
            }
        }
        return null;
    }

    public static String resourceIdToUri(int id) {
        return "android.resource://" + GroooAppManager.getAppContext().getPackageName() + "/" + id;
    }

}
