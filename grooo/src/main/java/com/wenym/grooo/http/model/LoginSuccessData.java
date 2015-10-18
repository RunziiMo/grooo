package com.wenym.grooo.http.model;

public class LoginSuccessData {

    private String userBuilding;
    private String buildingNum;
    private String roomNum;
    private String userid;

    public String getUserBuilding() {
        return userBuilding;
    }

    public void setUserBuilding(String userBuilding) {
        this.userBuilding = userBuilding;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "LoginSuccessData{" +
                "userBuilding='" + userBuilding + '\'' +
                ", buildingNum='" + buildingNum + '\'' +
                ", roomNum='" + roomNum + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
