package com.wenym.grooo.model.http;

import android.os.Parcel;
import android.os.Parcelable;

import com.wenym.grooo.model.app.Order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by runzii on 16-7-2.
 */
public class OrderForm implements Serializable{


    /**
     * building : 鹏远4号楼
     * address : 2007
     * detail : [{"id":10,"count":20}]
     */

    private String building;
    private String address;
    private String remark;
    /**
     * id : 10
     * count : 20
     */

    private List<Order.DetailBean> detail;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order.DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<Order.DetailBean> detail) {
        this.detail = detail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
