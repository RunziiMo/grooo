package com.wenym.grooo.model.app;

import com.google.gson.Gson;

/**
 * 整个软件的用户类，封装了一些属性
 * Created by runzii on 15-10-30.
 */
public class Profile {

    /**
     * avatar : http://o8ihshope.bkt.clouddn.com/1?v=1465762144361
     * email : 412416565@qq.com
     * id : 1
     * nickname : Responsible
     * school : {"id":1,"name":"NEUQ"}
     * score : 155.3
     */

    private String avatar;
    private String email;
    private int id;
    private String nickname;
    private String push_id;
    /**
     * id : 1
     * name : NEUQ
     */

    private School school;
    private double score;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }
}
