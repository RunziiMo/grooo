package com.wenym.grooo.util;

import com.google.gson.Gson;
import com.wenym.grooo.model.app.Address;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 小工具，乱七八糟
 * Created by Wouldyou on 2015/5/29.
 */
public class SmallTools {

    public static String resourceIdToUri(int id) {
        return "android.resource://" + GroooAppManager.getAppContext().getPackageName() + "/" + id;
    }

    private static Gson gson = new Gson();

    public static String toGsonString(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromGson(String jsonString, Class<T> classOfT) {
        return gson.fromJson(jsonString, classOfT);
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

    public static String formatDate(Date date) {
        return format.format(date);
    }

}
