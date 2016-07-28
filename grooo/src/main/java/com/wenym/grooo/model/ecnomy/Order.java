package com.wenym.grooo.model.ecnomy;


import android.os.Parcel;
import android.os.Parcelable;

import com.wenym.grooo.util.SmallTools;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.util.OrderStatus;

import java.util.Date;
import java.util.List;

public class Order {


    /**
     * address : 5022
     * building : 鹏远3号楼
     * detail : [{"count":1,"name":"无骨麻辣鸭"}]
     * order_id : 15017
     * price : 12
     * rating : 0
     * rating_remark :
     * remark :
     * seller : {"activity":"","basePrice":0,"category":"外卖","description":"","id":2833,"logo":"http://7vzt7o.com1.z0.glb.clouddn.com/Grooo/seller/韩式石锅拌饭.jpg","monthSold":0,"name":"韩式石锅拌饭(六餐)","phone":"13833524396","rateNumber":3,"rating":5,"school_id":1,"status":0}
     * status : 10
     * time : 2015-12-16 17:31:55
     * username : 18712785090
     */

    private String address;
    private String building;
    private String order_id;
    private String price;
    private int rating;
    private String rating_remark;
    private String remark;
    /**
     * activity :
     * basePrice : 0
     * category : 外卖
     * description :
     * id : 2833
     * logo : http://7vzt7o.com1.z0.glb.clouddn.com/Grooo/seller/韩式石锅拌饭.jpg
     * monthSold : 0
     * name : 韩式石锅拌饭(六餐)
     * phone : 13833524396
     * rateNumber : 3
     * rating : 5
     * school_id : 1
     * status : 0
     */

    private Shop seller;
    private int status;
    private Date time;
    private String username;
    /**
     * count : 1
     * name : 无骨麻辣鸭
     */

    private List<DetailBean> detail;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRating_remark() {
        return rating_remark;
    }

    public void setRating_remark(String rating_remark) {
        this.rating_remark = rating_remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Shop getSeller() {
        return seller;
    }

    public void setSeller(Shop seller) {
        this.seller = seller;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusString() {
        return OrderStatus.getStatus(status);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return SmallTools.formatDate(time).toString();
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        private String count;
        private String name;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
