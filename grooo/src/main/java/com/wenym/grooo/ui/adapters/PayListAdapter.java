package com.wenym.grooo.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenym.grooo.R;
import com.wenym.grooo.model.ecnomy.Food;
import com.wenym.grooo.model.ecnomy.Basket;

import static com.wenym.grooo.R.id.food_name;

/**
 * Created by Wouldyou on 2015/6/26.
 */
public class PayListAdapter extends RecyclerView.Adapter<PayListAdapter.ViewHolder> {

    private Basket basket;

    public PayListAdapter(Basket basket) {
        this.basket = basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
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

        if (position == 0) {
            holder.foodName.setText("总价");
            holder.foodNum.setText("￥" + basket.getTotalPrice());
        } else {
            Food food = (Food) basket.getOrderMap().keySet().toArray()[position - 1];
            holder.foodName
                    .setText(food.getName());
            holder.foodNum.setText(String.valueOf(basket.getOrderMap().values()
                    .toArray()[position - 1].toString()));
        }
    }


    @Override
    public int getItemCount() {
        return basket.getOrder().getDetail().size() + 1;
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
