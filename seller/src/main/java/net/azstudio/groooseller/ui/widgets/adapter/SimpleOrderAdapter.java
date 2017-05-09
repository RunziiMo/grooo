package net.azstudio.groooseller.ui.widgets.adapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.ItemOrderSimpleContentBinding;
import net.azstudio.groooseller.model.business.FoodOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by runzii on 16-5-25.
 */
public class SimpleOrderAdapter extends RecyclerView.Adapter<SimpleOrderAdapter.OrderHolder> {

    private int expandedPosition = -1;
    private List<FoodOrder> orders;

    public SimpleOrderAdapter() {
        this.orders = new ArrayList<>();
    }

    public void setOrders(List<FoodOrder> addedOrders) {
        orders.clear();
        orders.addAll(addedOrders);
        notifyDataSetChanged();
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_simple_content, parent, false);
        return new OrderHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        FoodOrder order = orders.get(position);
        holder.orderSimpleContentBinding.setOrder(order);
        holder.orderSimpleContentBinding.executePendingBindings();
        holder.orderSimpleContentBinding.detailList.setAdapter(new OrderDetailAdapter(order.getDetail()));

        if (position == expandedPosition) {
            holder.orderSimpleContentBinding.detailContent.setVisibility(View.VISIBLE);
        } else {
            holder.orderSimpleContentBinding.detailContent.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            // Check for an expanded view, collapse if you find one
            if (expandedPosition >= 0) {
                int prev = expandedPosition;
                notifyItemChanged(prev);
            }
            // Set the current position to "expanded"
            if (holder.getAdapterPosition() == expandedPosition)
                expandedPosition = -1;
            else
                expandedPosition = holder.getAdapterPosition();
            notifyItemChanged(expandedPosition);
        });
        RxView.clicks(holder.orderSimpleContentBinding.dial).debounce(1000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.getUsername()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    holder.orderSimpleContentBinding.dial.getContext().startActivity(intent);
                });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public static class OrderHolder extends RecyclerView.ViewHolder {
        ItemOrderSimpleContentBinding orderSimpleContentBinding;

        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            orderSimpleContentBinding = DataBindingUtil.bind(itemView);
            orderSimpleContentBinding.detailList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

}
