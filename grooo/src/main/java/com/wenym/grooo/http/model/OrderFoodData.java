package com.wenym.grooo.http.model;

import com.wenym.grooo.model.ecnomy.OrderItem;

import java.util.List;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class OrderFoodData {

    private String userid;
    private String sid;
    private String roomNum;
    private String method;
    private String buildingNum;
    private String remark;
    private List<OrderItem> foodlist;

    @Override
    public String toString() {
        return "OrderFoodData{" +
                "userid='" + userid + '\'' +
                ", sid='" + sid + '\'' +
                ", roomNum='" + roomNum + '\'' +
                ", method='" + method + '\'' +
                ", buildingNum='" + buildingNum + '\'' +
                ", remark='" + remark + '\'' +
                ", foodlist=" + foodlist +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;

    }

    public List<OrderItem> getFoodlist() {
        return foodlist;
    }

    public void setFoodlist(List<OrderItem> foodlist) {
        this.foodlist = foodlist;
    }

}
