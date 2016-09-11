package com.wenym.grooo.ui.shop;

import android.databinding.ObservableMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenym.grooo.R;
import com.wenym.grooo.model.app.Food;

import static com.wenym.grooo.R.id.food_name;

/**
 * Created by Wouldyou on 2015/6/26.
 */
public class PayListAdapter extends RecyclerView.Adapter<PayListAdapter.ViewHolder> {

    private Food[] foods;
    private Integer[] numbs;

    public PayListAdapter(ObservableMap<Food, Integer> orders) {
        foods = new Food[orders.size()];
        numbs = new Integer[orders.size()];
        orders.keySet().toArray(foods);
        orders.values().toArray(numbs);
    }

    public void setBasket(ObservableMap<Food, Integer> orders) {
        foods = new Food[orders.size()];
        numbs = new Integer[orders.size()];
        orders.keySet().toArray(foods);
        orders.values().toArray(numbs);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_paylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.foodName.setText(foods[position].getName());
        holder.foodNum.setText(String.valueOf(numbs[position]));
    }


    @Override
    public int getItemCount() {
        return foods.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodNum;
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            foodName = (TextView) itemView.findViewById(food_name);
            foodNum = (TextView) itemView.findViewById(R.id.food_num);
        }
    }

}
