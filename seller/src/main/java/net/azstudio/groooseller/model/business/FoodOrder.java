package net.azstudio.groooseller.model.business;


import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.provider.OrderStatus;
import net.azstudio.groooseller.utils.SmallTools;

import java.util.Date;
import java.util.List;

public class FoodOrder {


    /**
     * address : py3#2007
     * building : 鹏远3号楼
     * detail : [{"count":2,"name":"测试商品1"},{"count":1,"name":"测试商品2"}]
     * order_id : 20160506000181000154
     * price : 20
     * rating : 3
     * rating_remark : 好！
     * remark : null
     * status : 20
     * time : 2016-05-06T04:10:59
     */

    private String address;
    private String building;
    private String order_id;
    private String price;
    private Integer rating;
    private String rating_remark;
    private String remark;
    private Integer status;
    private Date time;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public FoodOrder clone() {
        FoodOrder order = new FoodOrder();
        order.setStatus(status);
        order.setOrder_id(order_id);
        return order;
    }

    /**
     * count : 2
     * name : 测试商品1
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
        return SmallTools.formatDate(this.time);
    }

    public void setTime(Date time) {
        this.time = time;
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).placeholder(R.drawable.login_logo).into(view);
    }

}
