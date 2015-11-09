package com.wenym.grooo.http.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenym.grooo.model.app.New;

import java.util.ArrayList;

/**
 * Created by Wouldyou on 2015/5/28.
 */
public class GetNewsSuccessData {

    private ArrayList<New> news;

    public GetNewsSuccessData(String response) {
        news = new Gson().fromJson(response, new TypeToken<ArrayList<New>>() {
        }.getType());
    }

    public ArrayList<New> getNews() {
        return news;
    }

}
