package net.azstudio.groooseller.model.app;

import com.google.gson.Gson;

/**
 * 整个软件的用户类，封装了一些属性
 * Created by runzii on 15-10-30.
 */
public class ShopInfo {

    private int id;
    private String name = "";
    private String phone = "";
    private String logo;
    private String basePrice;
    private String description = "";
    private String activity = "";
    private String rating;
    private String rateNumber;
    private String monthSold;
    private int status;

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo == null ? "" : logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(String rateNumber) {
        this.rateNumber = rateNumber;
    }

    public String getMonthSold() {
        return monthSold;
    }

    public void setMonthSold(String monthSold) {
        this.monthSold = monthSold;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


}
