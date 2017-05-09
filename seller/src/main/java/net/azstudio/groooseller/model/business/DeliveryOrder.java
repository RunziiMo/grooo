package net.azstudio.groooseller.model.business;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class DeliveryOrder {

    private String shop_icon;
    private String seller_name;
    private String time;
    private String id;
    private String tophonenumber;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "shop_icon='" + shop_icon + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", time='" + time + '\'' +
                ", id='" + id + '\'' +
                ", tophonenumber='" + tophonenumber + '\'' +
                ", remark='" + remark + '\'' +
                ", shopPhoneNumber='" + shopPhoneNumber + '\'' +
                '}';
    }

    public String getTophonenumber() {
        return tophonenumber;
    }

    public void setTophonenumber(String tophonenumber) {
        this.tophonenumber = tophonenumber;
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

}
