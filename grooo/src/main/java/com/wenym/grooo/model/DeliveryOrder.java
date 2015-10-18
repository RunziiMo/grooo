package com.wenym.grooo.model;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class DeliveryOrder {

    private String shop_icon;
    private String seller_name;
    private String time;
    private String totalPrice = "未知";
    private String order_status = "1";
    private String id;
    private String PhoneNumber;
    private String remark;
    private String shopPhoneNumber = "18712788313";

    public String getShop_icon() {
        return shop_icon;
    }

    public void setShop_icon(String shop_icon) {
        this.shop_icon = shop_icon;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShopPhoneNumber() {
        return shopPhoneNumber;
    }

    public void setShopPhoneNumber(String shopPhoneNumber) {
        this.shopPhoneNumber = shopPhoneNumber;
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "shop_icon='" + shop_icon + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", time='" + time + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", order_status='" + order_status + '\'' +
                ", id='" + id + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", remark='" + remark + '\'' +
                ", remark='" + remark + '\'' +
                ", shopPhoneNumber='" + shopPhoneNumber + '\'' +
                '}';
    }
}
