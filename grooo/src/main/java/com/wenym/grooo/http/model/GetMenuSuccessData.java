package com.wenym.grooo.http.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenym.grooo.model.Menu;

import java.util.ArrayList;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class GetMenuSuccessData {

    private ArrayList<Menu> menus;

    public GetMenuSuccessData(String response) {
        System.out.println(response);
        menus = new Gson().fromJson(response, new TypeToken<ArrayList<Menu>>() {
        }.getType());
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }
}
