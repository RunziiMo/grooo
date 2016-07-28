package com.wenym.grooo.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ItemOrderContentBinding;
import com.wenym.grooo.model.ecnomy.Order;
import com.wenym.grooo.ui.activities.OrderDetailActivity;
import com.wenym.grooo.ui.activities.RestaurantDetailActivity;
import com.wenym.grooo.util.SmallTools;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FoodOrderAdapter extends
        RecyclerView.Adapter<FoodOrderAdapter.SimpleViewHolder> {

    private Context mContext;
    private List<Order> mDataset;

    public FoodOrderAdapter(Context context, List<Order> mlist) {
        this.mContext = context;
        this.mDataset = mlist;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_order_content, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder,
                                 final int position) {
        final Order order = mDataset.get(position);
        viewHolder.binding.setOrder(order);
        RxView.clicks(viewHolder.itemView)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    Intent intent = new Intent(viewHolder.itemView.getContext(), OrderDetailActivity.class);
                    intent.putExtra("order",SmallTools.toGsonString(order));
                    viewHolder.itemView.getContext().startActivity(intent);
                });
//        viewHolder.buttonCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mItemManger.closeAllItems();
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//                        + order.getSeller_phone()));
//                mContext.startActivity(intent);
//            }
//        });
//        viewHolder.buttonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mItemManger.closeAllItems();
//                if (order.getStatus().equals("0")) {
//                    new MaterialDialog.Builder(view.getContext()).title("确定要申请退单吗？").positiveText("确定")
//                            .negativeText("取消").callback(new MaterialDialog.ButtonCallback() {
//                        @Override
//                        public void onPositive(MaterialDialog dialog) {
//                            super.onPositive(dialog);
//                            CancelOrderData cancelOrderData = new CancelOrderData();
//                            cancelOrderData.setDid(order.getId());
//                            NetworkWrapper.MakeAPICall(cancelOrderData, mContext, new HttpCallBack() {
//                                @Override
//                                public void onSuccess(Object object) {
//                                    CancelOrderSuccessData cancelOrderSuccessData = (CancelOrderSuccessData) object;
//                                    Toasts.show(cancelOrderSuccessData.getInfo());
//                                    order.setStatus("2");
//                                    notifyDataSetChanged();
//                                }
//
//
//                                @Override
//                                public void onFailed(String reason) {
//                                    Toasts.show(reason);
//                                    if (order.getStatus().equals("0")) {
//                                        order.setStatus("1");
//                                        notifyDataSetChanged();
//                                    }
//                                }
//
//                                @Override
//                                public void onError(int statusCode) {
//                                    Toasts.show(String.valueOf(statusCode));
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onNegative(MaterialDialog dialog) {
//                            super.onNegative(dialog);
//                        }
//                    }).show();
//                } else {
//                    new MaterialDialog.Builder(view.getContext()).content("不能申请退单，商家已接单或订单已无效。").show();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        ItemOrderContentBinding binding;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
