package com.wenym.grooo.model.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by runzii on 16-7-2.
 */
public class Shop implements Parcelable {

    private String activity;
    private float basePrice;
    private String category;
    private String description;
    private int id;
    private String logo;
    private int monthSold;
    private String name;
    private String phone;
    private int rateNumber;
    private float rating;
    private int school_id;
    private int status;

    protected Shop(Parcel in) {
        activity = in.readString();
        basePrice = in.readFloat();
        category = in.readString();
        description = in.readString();
        id = in.readInt();
        logo = in.readString();
        monthSold = in.readInt();
        name = in.readString();
        phone = in.readString();
        rateNumber = in.readInt();
        rating = in.readFloat();
        school_id = in.readInt();
        status = in.readInt();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activity);
        dest.writeFloat(basePrice);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeInt(id);
        dest.writeString(logo);
        dest.writeInt(monthSold);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeInt(rateNumber);
        dest.writeFloat(rating);
        dest.writeInt(school_id);
        dest.writeInt(status);
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < logo.length(); i++) {
            if ((logo.charAt(i) + "").getBytes().length > 1) {
                try {
                    sb.append(URLEncoder.encode(logo.charAt(i) + "", "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(logo.charAt(i));
            }
        }
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMonthSold() {
        return ""+monthSold;
    }

    public void setMonthSold(int monthSold) {
        this.monthSold = monthSold;
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

    public int getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(int rateNumber) {
        this.rateNumber = rateNumber;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
