package com.wenym.grooo.model.app;

import com.google.gson.Gson;

/**
 * 整个软件的用户类，封装了一些属性
 * Created by runzii on 15-10-30.
 */
public class AppUser {

    private String userid;
    private String username;
    private String buildingNum;
    private String email;
    private int Score;
    private String roomNum;
    private String userBuilding;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

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

    public static AppUser fromString(String from) {
        return new Gson().fromJson(from, AppUser.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
