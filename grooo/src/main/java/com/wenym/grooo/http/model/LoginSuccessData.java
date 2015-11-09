package com.wenym.grooo.http.model;

public class LoginSuccessData {

    private String userid;
    private String username;
    private String buildingNum;
    private String email;
    private int Score;
    private String roomNum;

    @Override
    public String toString() {
        return "LoginSuccessData{" +
                "roomNum='" + roomNum + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", buildingNum='" + buildingNum + '\'' +
                ", email='" + email + '\'' +
                ", Score=" + Score +
                '}';
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
