package com.wenym.grooo.http.model;

import com.wenym.grooo.model.BaiduPicture;

import java.util.ArrayList;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class GetBaiduPictureSuccessData {

    private String tag1;
    private String tag2;
    private int totalNum;
    private int start_index;
    private int return_number;
    private ArrayList<BaiduPicture> data;

    @Override
    public String toString() {
        return "GetPicturesRestaurantSuccessData{" +
                "tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", totalNum=" + totalNum +
                ", start_index=" + start_index +
                ", return_number=" + return_number +
                ", data=" + data +
                '}';
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getStart_index() {
        return start_index;
    }

    public void setStart_index(int start_index) {
        this.start_index = start_index;
    }

    public int getReturn_number() {
        return return_number;
    }

    public void setReturn_number(int return_number) {
        this.return_number = return_number;
    }

    public ArrayList<BaiduPicture> getData() {
        return data;
    }

    public void setData(ArrayList<BaiduPicture> data) {
        this.data = data;
    }
}
