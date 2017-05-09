package net.azstudio.groooseller.ui.widgets.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.http.NetworkWrapper;
import net.azstudio.groooseller.model.business.FoodOrder;
import net.azstudio.groooseller.provider.OrderStatus;
import net.azstudio.groooseller.utils.RxEvent.OrderEvent;
import net.azstudio.groooseller.utils.RxJava.RxBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by runzii on 16-5-25.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private int expandedPosition = -1;
    private List<FoodOrder> orders;

    public OrderAdapter(List<FoodOrder> orders) {
        this.orders = orders;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_content, parent, false);
        return new OrderHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        FoodOrder order = orders.get(position);
        holder.building.setText(order.getBuilding());
        holder.address.setText(order.getAddress());
        holder.remark.setVisibility(order.getRemark() == null ? View.GONE : View.VISIBLE);
        holder.remark.setText(order.getRemark() == null ? "" : order.getRemark());
        holder.time.setText(order.getTime());
        holder.price.setText(order.getPrice());
        holder.status.setText(OrderStatus.getStatus(order.getStatus()));
        holder.detail.setAdapter(new OrderDetailAdapter(order.getDetail()));
        holder.confirm.setVisibility((order.getStatus() == 0 || order.getStatus() == 20) ? View.VISIBLE : View.GONE);
        holder.cancel.setVisibility(order.getStatus() == 0 ? View.VISIBLE : View.GONE);

        if (position == expandedPosition) {
            holder.content.setVisibility(View.VISIBLE);
        } else {
            holder.content.setVisibility(View.GONE);
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
        RxView.clicks(holder.confirm).debounce(1000, TimeUnit.MILLISECONDS).flatMap(aVoid -> {
            FoodOrder tempOrder = order.clone();
            tempOrder.setStatus(order.getStatus() == 0 ? 10 : 21);
            return NetworkWrapper.get().editOrder(tempOrder);
        }).subscribe(body -> {
            order.setStatus(order.getStatus() == 0 ? 10 : 21);
            RxBus.getDefault().post(new OrderEvent(order));
            orders.remove(order);
            notifyItemRemoved(position);
        });
        RxView.clicks(holder.cancel).debounce(1000, TimeUnit.MILLISECONDS).flatMap(aVoid -> {
            FoodOrder tempOrder = order.clone();
            tempOrder.setStatus(22);
            return NetworkWrapper.get().editOrder(tempOrder);
        }).subscribe(body -> {
            order.setStatus(22);
            RxBus.getDefault().post(new OrderEvent(order));
            orders.remove(order);
            notifyItemRemoved(position);
        });
        RxView.clicks(holder.dial).debounce(1000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.getUsername()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    holder.dial.getContext().startActivity(intent);
                });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public static class OrderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_item_avater)
        ImageView logo;
        @BindView(R.id.order_item_building)
        TextView building;
        @BindView(R.id.order_item_address)
        TextView address;
        @BindView(R.id.order_item_remark)
        TextView remark;
        @BindView(R.id.order_item_time)
        TextView time;
        @BindView(R.id.order_item_price)
        TextView price;
        @BindView(R.id.order_item_status)
        TextView status;
        @BindView(R.id.order_item_confirm)
        Button confirm;
        @BindView(R.id.order_item_cancel)
        Button cancel;
        @BindView(R.id.order_item_dial)
        ImageButton dial;
        @BindView(R.id.order_item_detail_list)
        RecyclerView detail;
        @BindView(R.id.order_item_detail_content)
        View content;

        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            detail.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

}
