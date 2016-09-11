package com.wenym.grooo.model.app;


import android.os.Parcel;
import android.os.Parcelable;

import com.wenym.grooo.util.OrderStatus;
import com.wenym.grooo.util.SmallTools;

import java.util.Date;
import java.util.List;

public class Order implements Parcelable {


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


    protected Order(Parcel in) {
        address = in.readString();
        building = in.readString();
        order_id = in.readString();
        price = in.readString();
        rating = in.readInt();
        rating_remark = in.readString();
        remark = in.readString();
        seller = in.readParcelable(Shop.class.getClassLoader());
        status = in.readInt();
        username = in.readString();
        detail = in.createTypedArrayList(DetailBean.CREATOR);
        time = (Date) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(building);
        dest.writeString(order_id);
        dest.writeString(price);
        dest.writeInt(rating);
        dest.writeString(rating_remark);
        dest.writeString(remark);
        dest.writeParcelable(seller, flags);
        dest.writeInt(status);
        dest.writeString(username);
        dest.writeTypedList(detail);
        dest.writeSerializable(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

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

    public static class DetailBean implements Parcelable {
        private String count;
        private String name;

        public DetailBean(){}

        protected DetailBean(Parcel in) {
            count = in.readString();
            name = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(count);
            dest.writeString(name);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DetailBean> CREATOR = new Creator<DetailBean>() {
            @Override
            public DetailBean createFromParcel(Parcel in) {
                return new DetailBean(in);
            }

            @Override
            public DetailBean[] newArray(int size) {
                return new DetailBean[size];
            }
        };

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
