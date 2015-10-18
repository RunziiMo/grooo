package com.wenym.grooo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenym.grooo.R;
import com.wenym.grooo.model.Food;
import com.wenym.grooo.model.ShoppingBasket;

import static com.wenym.grooo.R.id.food_name;

/**
 * Created by Wouldyou on 2015/6/26.
 */
public class PayListAdapter extends RecyclerView.Adapter<PayListAdapter.ViewHolder> {


    private ShoppingBasket basket;

    public PayListAdapter() {
        this.basket = ShoppingBasket.getInstance();
        basket.setAdapter(this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_paylist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0) {
            holder.foodName.setText("总价");
            holder.foodNum.setText("￥" + basket.getTotalPrice());
        } else if (position == 1) {
            holder.foodName.setText("餐盒费");
            holder.foodNum.setText("￥" + basket.getPackagePrice());
        } else {
            Food food = (Food) basket.getOrder().keySet().toArray()[position - 2];
            holder.foodName
                    .setText(food.getName());
            holder.foodNum.setText(String.valueOf(basket.getOrder().values()
                    .toArray()[position - 2].toString()));
        }
    }


    @Override
    public int getItemCount() {
        return basket.getOrder().size() + 2;
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
