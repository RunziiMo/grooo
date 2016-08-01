package com.wenym.grooo.model.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by runzii on 16-7-2.
 */
public class CommentForm implements Parcelable {


    /**
     * order_id : 20160506000181000154
     * rating : 3
     * rating_remark : 好！
     */

    private String order_id;
    private int rating;
    private String rating_remark;

    public CommentForm() {
    }

    protected CommentForm(Parcel in) {
        order_id = in.readString();
        rating = in.readInt();
        rating_remark = in.readString();
    }

    public static final Creator<CommentForm> CREATOR = new Creator<CommentForm>() {
        @Override
        public CommentForm createFromParcel(Parcel in) {
            return new CommentForm(in);
        }

        @Override
        public CommentForm[] newArray(int size) {
            return new CommentForm[size];
        }
    };

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRatingString() {
        return String.valueOf(rating);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(order_id);
        parcel.writeInt(rating);
        parcel.writeString(rating_remark);
    }

    @Override
    public String toString() {
        return "CommentForm{" +
                "order_id='" + order_id + '\'' +
                ", rating=" + rating +
                ", rating_remark='" + rating_remark + '\'' +
                '}';
    }
}
