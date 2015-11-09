package com.wenym.grooo.model.ecnomy;

import java.util.ArrayList;
import java.util.HashMap;

public class LingshiClass {

    private HashMap<String, ArrayList<Menu>> classes;

    public LingshiClass(ArrayList<Menu> menus) {
        classes = new HashMap<String, ArrayList<Menu>>();
        for (int i = 0; i < menus.size(); i++) {
            String bigClass = menus.get(i).getFoodclass().split("-")[0];
            if (!classes.containsKey(bigClass)) {
                classes.put(bigClass, new ArrayList<Menu>());
            }
            classes.get(bigClass).add(menus.get(i));
        }
    }

    public HashMap<String, ArrayList<Menu>> getClasses() {
        return classes;
    }
}
