package com.wenym.grooo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Restaurant implements Serializable, Parcelable {

    /**
     * 版本1
     */
    private static final long serialVersionUID = 1L;
    public String SellerImageURL;// 商户logo
    public String Shopname;// 商户名称
    public int Rating;// 星星数量
    public int BasePrice;// 起送价
    public int DiscountStartPrice;
    public int DiscountPrice;
    public String id;// 商家编号
    public String NumPerMonth;// 售出数量
    public String finish_time = "他们不准我加送达时间";
    public String Description;// 商户介绍
    public String Announcement;// 商户推广信息
    public Boolean Status;// 商户是否休息
    public Boolean is_favor = false;// 是否添加关注
    public Boolean is_mins = false;// 是否有活动
    private String DeliveryClass;
    private String Class1;

    public String getClass1() {
        return Class1;
    }

    public void setClass1(String class1) {
        Class1 = class1;
    }

    public String getSellerImageURL() {
        return SellerImageURL;
    }

    public void setSellerImageURL(String sellerImageURL) {
        SellerImageURL = sellerImageURL;
    }

    public String getShopname() {
        return Shopname;
    }

    public void setShopname(String shopname) {
        Shopname = shopname;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public int getBasePrice() {
        return BasePrice;
    }

    public void setBasePrice(int basePrice) {
        BasePrice = basePrice;
    }

    public int getDiscountStartPrice() {
        return DiscountStartPrice;
    }

    public void setDiscountStartPrice(int discountStartPrice) {
        DiscountStartPrice = discountStartPrice;
    }

    public int getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        DiscountPrice = discountPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumPerMonth() {
        return NumPerMonth;
    }

    public void setNumPerMonth(String numPerMonth) {
        NumPerMonth = numPerMonth;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAnnouncement() {
        return Announcement;
    }

    public void setAnnouncement(String announcement) {
        Announcement = announcement;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public Boolean getIs_favor() {
        return is_favor;
    }

    public void setIs_favor(Boolean is_favor) {
        this.is_favor = is_favor;
    }

    public Boolean getIs_mins() {
        return is_mins;
    }

    public void setIs_mins(Boolean is_mins) {
        this.is_mins = is_mins;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "SellerImageURL='" + SellerImageURL + '\'' +
                ", Shopname='" + Shopname + '\'' +
                ", Rating=" + Rating +
                ", BasePrice=" + BasePrice +
                ", DiscountStartPrice=" + DiscountStartPrice +
                ", DiscountPrice=" + DiscountPrice +
                ", id='" + id + '\'' +
                ", NumPerMonth='" + NumPerMonth + '\'' +
                ", finish_time='" + finish_time + '\'' +
                ", Description='" + Description + '\'' +
                ", Announcement='" + Announcement + '\'' +
                ", Status=" + Status +
                ", is_favor=" + is_favor +
                ", is_mins=" + is_mins +
                ", DeliveryClass='" + DeliveryClass + '\'' +
                ", Class1='" + Class1 + '\'' +
                '}';
    }

    public String getDeliveryClass() {
        return DeliveryClass;
    }

    public void setDeliveryClass(String deliveryClass) {
        DeliveryClass = deliveryClass;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }
}
