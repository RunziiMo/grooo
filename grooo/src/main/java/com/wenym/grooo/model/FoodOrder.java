package com.wenym.grooo.model;

public class FoodOrder {

    private String SellerImageURL;
    private String seller_name;
    private String time;
    private String totalPrice;
    private String status;
    private String id;
    private String remark;
    private String seller_phone;

    public String getSellerImageURL() {
        return SellerImageURL;
    }

    public void setSellerImageURL(String sellerImageURL) {
        SellerImageURL = sellerImageURL;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    @Override
    public String toString() {
        return "FoodOrder{" +
                "SellerImageURL='" + SellerImageURL + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", time='" + time + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", remark='" + remark + '\'' +
                ", seller_phone='" + seller_phone + '\'' +
                '}';
    }
}
