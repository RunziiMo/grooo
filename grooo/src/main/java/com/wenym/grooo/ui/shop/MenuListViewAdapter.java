package com.wenym.grooo.ui.shop;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ItemFoodListBinding;
import com.wenym.grooo.model.app.Food;
import com.wenym.grooo.util.RxEvent.FoodEvent;
import com.wenym.grooo.util.RxJava.RxBus;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by runzii on 16-8-31.
 */
public class MenuListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<String> stickers;

    private List<Food> foods;

    public MenuListViewAdapter(List<Food> foods, List<String> stickers) {
        this.foods = foods;
        this.stickers = stickers;
    }

    public void clearNumb() {
        for (Food food : foods) {
            food.buyNum.set(0);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list_header_thin, parent, false);
            holder = new HeaderHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderHolder) convertView.getTag();
        }
        holder.header.setText(stickers.get(position));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return stickers.get(position).hashCode();
    }


    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Food getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foods.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Food food = foods.get(position);
        holder.bind.setFood(food);
        holder.bind.foodListItemAdd.setOnClickListener(v -> {
            int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
            v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
//                    buyImg = new BadgeView(getActivity());
////                    buyNumView.setImageResource(R.drawable.default_photo);
//                    try {
//                        buyImg.setImageBitmap(getAddDrawBitMap(position));// 设置buyImg的图片
//                    }catch (NullPointerException e){
//                        Toasts.show("fuck");
//                    }
            food.buyNum.set(food.buyNum.get() + 1);
            notifyDataSetChanged();
            RxBus.getDefault().post(new FoodEvent(food, true));
        });
        holder.bind.foodListItemMinus.setOnClickListener(v -> {
            food.buyNum.set(food.buyNum.get() - 1);
            notifyDataSetChanged();
            RxBus.getDefault().post(new FoodEvent(food, false));
        });
        return convertView;
    }

    public class ViewHolder {

        ItemFoodListBinding bind;

        public ViewHolder(View view) {
            bind = DataBindingUtil.bind(view);
        }

        public void bind(Food food) {
            bind.setFood(food);
        }

    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        public final TextView header;

        public HeaderHolder(View view) {
            super(view);
            header = (TextView) view.findViewById(R.id.food_list_item_header);
        }

    }

}
