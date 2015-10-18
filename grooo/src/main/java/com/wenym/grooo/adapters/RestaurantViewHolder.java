package com.wenym.grooo.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wenym.grooo.R;

public class RestaurantViewHolder extends ViewHolder {

    CardView restaurant;
    ImageView logo;
    TextView name;
    TextView buy_nums;// 售出份数
    RatingBar rate;
    TextView time;//送达时间
    TextView announcement;// 公告
    ImageView cover;// 遮盖的图片

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        restaurant = (CardView) itemView.findViewById(R.id.restaurant_item);
        name = (TextView) itemView.findViewById(R.id.restaurant_list_item_shopname);
        logo = (ImageView) itemView
                .findViewById(R.id.restaurant_list_item_logo);
        announcement = (TextView) itemView
                .findViewById(R.id.restaurant_list_item_announcement);
        buy_nums = (TextView) itemView
                .findViewById(R.id.restaurant_list_item_buynums);
//		rate = (RatingBar) itemView
//				.findViewById(R.id.restaurant_list_item_rate);
//		promotion = (LinearLayout) itemView
//				.findViewById(R.id.restaurant_list_item_present_promotion_container);
        time = (TextView) itemView.findViewById(R.id.restaurant_list_item_time);
        cover = (ImageView) itemView
                .findViewById(R.id.restaurant_list_item_mask);
    }

}
