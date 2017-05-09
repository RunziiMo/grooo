package net.azstudio.groooseller.model.business;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.google.gson.Gson;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Restaurant implements Serializable, Parcelable {

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    /**
     * 版本1
     */
    private static final long serialVersionUID = 1L;
    private String SellerImageURL;// 商户logo
    private String Shopname;// 商户名称
    private int Rating;// 星星数量
    private int BasePrice;// 起送价
    private int DiscountStartPrice;
    private int DiscountPrice;
    private String id;// 商家编号
    private String NumPerMonth;// 售出数量
    private String finish_time = "他们不准我加送达时间";
    private String Description;// 商户介绍
    private String Announcement;// 商户推广信息
    private Boolean Status;// 商户是否休息
    private Boolean is_favor = false;// 是否添加关注
    private Boolean is_mins = false;// 是否有活动
    private String DeliveryClass;
    private String Class1;

    public String getClass1() {
        return Class1;
    }

    public void setClass1(String class1) {
        Class1 = class1;
    }

    public String getSellerImageURL() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < SellerImageURL.length(); i++) {
            if ((SellerImageURL.charAt(i) + "").getBytes().length > 1) {
                try {
                    sb.append(URLEncoder.encode(SellerImageURL.charAt(i) + "", "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(SellerImageURL.charAt(i));
            }
        }
        return sb.toString();
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
        return Html.fromHtml(Announcement).toString();
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

    public static Restaurant fromJson(String str) {
        return new Gson().fromJson(str, Restaurant.class);
    }

    private Restaurant(Parcel in) {
        this.Announcement = in.readString();
        this.Class1 = in.readString();
        this.DeliveryClass = in.readString();
        this.Description = in.readString();
        this.finish_time = in.readString();
        this.id = in.readString();
        this.Shopname = in.readString();
        this.NumPerMonth = in.readString();
        this.SellerImageURL = in.readString();
        this.BasePrice = in.readInt();
        this.DiscountPrice = in.readInt();
        this.DiscountStartPrice = in.readInt();
        this.Rating = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Announcement);
        dest.writeString(this.Class1);
        dest.writeString(this.DeliveryClass);
        dest.writeString(this.Description);
        dest.writeString(this.finish_time);
        dest.writeString(this.id);
        dest.writeString(this.Shopname);
        dest.writeString(this.NumPerMonth);
        dest.writeString(this.SellerImageURL);
        dest.writeInt(this.BasePrice);
        dest.writeInt(this.DiscountPrice);
        dest.writeInt(this.DiscountStartPrice);
        dest.writeInt(this.Rating);
    }

}
