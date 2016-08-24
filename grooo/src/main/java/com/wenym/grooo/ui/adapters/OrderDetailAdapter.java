package com.wenym.grooo.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ItemOrderDetailBinding;
import com.wenym.grooo.model.app.Order;

import java.util.List;

/**
 * Created by runzii on 16-5-25.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.DetailHolder> {

    List<Order.DetailBean> details;

    public OrderDetailAdapter(List<Order.DetailBean> list) {
        this.details = list;
    }

    public void setDetails(List<Order.DetailBean> details) {
        this.details = details;
        notifyDataSetChanged();
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new DetailHolder(v);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {
        Order.DetailBean bean = details.get(position);
        holder.detailBinding.setDetail(bean);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public static class DetailHolder extends RecyclerView.ViewHolder {
        ItemOrderDetailBinding detailBinding;

        public DetailHolder(View itemView) {
            super(itemView);
            detailBinding = DataBindingUtil.bind(itemView);
        }
    }
}
