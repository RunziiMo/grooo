package com.wenym.grooo.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenym.grooo.GroooApplication;
import com.wenym.grooo.R;
import com.wenym.grooo.RestaurantDetailActivity;
import com.wenym.grooo.model.Restaurant;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private boolean isWaiMai;
    private List<Restaurant> contents;

    public RestaurantListAdapter(List<Restaurant> contents, boolean isWaiMai) {
        this.contents = contents;
        this.isWaiMai = isWaiMai;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_list_item_dev, parent, false);
                return new RestaurantViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_list_item_dev, parent, false);
                return new RestaurantViewHolder(view) {
                };
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder holder, final int position) {
        final Restaurant restaurant = contents.get(position);
        ImageLoader.getInstance().displayImage(restaurant.getSellerImageURL(), holder.logo, GroooApplication.options);
        holder.time.setText("他们不让加时间");
        holder.announcement.setText(restaurant.getAnnouncement().equals("") ? "该店暂无公告" : restaurant.getAnnouncement());
        holder.buy_nums.setText("月售：" + restaurant.getNumPerMonth());
        holder.cover.setVisibility(restaurant.getStatus() ? View.GONE : View.VISIBLE);
        holder.name.setText(restaurant.getShopname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), RestaurantDetailActivity.class);
                intent.putExtra("entity", new Gson().toJson(restaurant));
                intent.putExtra("isWaiMai", isWaiMai);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                break;
        }
    }
}