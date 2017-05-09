package net.azstudio.groooseller.utils;


import android.net.Uri;

import net.azstudio.groooseller.model.business.Menu;
import net.azstudio.groooseller.model.http.HttpFood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小工具，乱七八糟
 * Created by Wouldyou on 2015/5/29.
 */
public class SmallTools {

    public static List<Menu> toMenuMap(List<HttpFood> httpFoods) {
        Map<String, List<HttpFood>> menuMap = new HashMap<>();
        for (HttpFood food : httpFoods) {
            if (menuMap.containsKey(food.getCategory()))
                menuMap.get(food.getCategory()).add(food);
            else {
                menuMap.put(food.getCategory(), new ArrayList<HttpFood>());
                menuMap.get(food.getCategory()).add(food);
            }
        }
        List<Menu> menuList = new ArrayList<>();
        for (Map.Entry<String, List<HttpFood>> entry : menuMap.entrySet())
            menuList.add(new Menu(entry.getKey(), entry.getValue()));
        return menuList;
    }

    public static void addFoodInMenu(List<Menu> menus, HttpFood food) {
        boolean added = false;
        for (Menu menu : menus) {
            if (menu.getCategory().equals(food.getCategory())) {
                menu.getFoods().add(food);
                added = true;
            }
        }
        if (!added) {
            List<HttpFood> newList = new ArrayList<>();
            newList.add(food);
            Menu menu = new Menu(food.getCategory(), newList);
            menus.add(menu);
        }
    }

    public static void deleteFoodInMenu(List<Menu> menus, HttpFood food) {
        for (Menu menu : menus) {
            if (menu.getCategory().equals(food.getCategory())) {
                menu.getFoods().remove(food);
                if (menu.getFoods().size() == 0)
                    menus.remove(menu);
            }
        }
    }

    public static Uri resourceIdToUri(int id) {
        return Uri.parse("android.resource://" + AppManager.getAppContext().getPackageName() + "/" + id);
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

    public static String formatDate(Date date) {
        return format.format(date);
    }

}
