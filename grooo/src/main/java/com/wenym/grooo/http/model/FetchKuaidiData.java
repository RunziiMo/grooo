package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class FetchKuaidiData {

    private String userid;
    private String companyName;
    private String pid;
    private String toname;
    private String tophonenumber;
    private String buildingNum;
    private String roomNum;
    private String remark;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    @Override
    public String toString() {
        return "FetchKuaidiData{" +
                "userid='" + userid + '\'' +
                ", companyName='" + companyName + '\'' +
                ", pid='" + pid + '\'' +
                ", toname='" + toname + '\'' +
                ", tophonenumber='" + tophonenumber + '\'' +
                ", buildingNum='" + buildingNum + '\'' +
                ", roomNum='" + roomNum + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public String getTophonenumber() {
        return tophonenumber;
    }

    public void setTophonenumber(String tophonenumber) {
        this.tophonenumber = tophonenumber;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
