package com.wenym.grooo.model.http;

/**
 * Created by runzii on 16-7-2.
 */
public class CommentForm {


    /**
     * order_id : 20160506000181000154
     * rating : 3
     * rating_remark : 好！
     */

    private String order_id;
    private float rating;
    private String rating_remark;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getRating_remark() {
        return rating_remark;
    }

    public void setRating_remark(String rating_remark) {
        this.rating_remark = rating_remark;
    }
}
