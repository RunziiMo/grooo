package com.wenym.grooo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.CancelOrderData;
import com.wenym.grooo.http.model.CancelOrderSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.ecnomy.FoodOrder;
import com.wenym.grooo.utils.OrderStatus;
import com.wenym.grooo.widgets.Toasts;

import java.util.List;

public class OrderListAdapter extends
        RecyclerSwipeAdapter<OrderListAdapter.SimpleViewHolder> {

    protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(
            this);
    private Context mContext;
    private List<FoodOrder> mDataset;

    public OrderListAdapter(Context context, List<FoodOrder> mlist) {
        this.mContext = context;
        this.mDataset = mlist;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.orderlist_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder,
                                 final int position) {
        final FoodOrder foodOrder = mDataset.get(position);
        Picasso.with(viewHolder.shopIcon.getContext()).load(foodOrder.getSellerImageURL()).into(viewHolder.shopIcon);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.buttonShowup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mItemManger.openItem(position);
            }
        });
        viewHolder.buttonClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mItemManger.closeItem(position);
            }
        });
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.BounceInLeft).duration(500).delay(100)
                        .playOn(layout.findViewById(R.id.btn_close));
            }
        });
        viewHolder.buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.closeAllItems();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + foodOrder.getSeller_phone()));
                mContext.startActivity(intent);
            }
        });
        viewHolder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.closeAllItems();
                if (foodOrder.getStatus().equals("0")) {
                    new MaterialDialog.Builder(view.getContext()).title("确定要申请退单吗？").positiveText("确定")
                            .negativeText("取消").callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            CancelOrderData cancelOrderData = new CancelOrderData();
                            cancelOrderData.setDid(foodOrder.getId());
                            HttpUtils.MakeAPICall(cancelOrderData, mContext, new HttpCallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    CancelOrderSuccessData cancelOrderSuccessData = (CancelOrderSuccessData) object;
                                    Toasts.show(cancelOrderSuccessData.getInfo());
                                    foodOrder.setStatus("2");
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailed() {
                                    Toasts.show("申请退单失败");
                                    if (foodOrder.getStatus().equals("0")) {
                                        foodOrder.setStatus("1");
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onError(int statusCode) {
                                    Toasts.show(String.valueOf(statusCode));
                                }
                            });
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    }).show();
                } else {
                    new MaterialDialog.Builder(view.getContext()).content("不能申请退单，商家已接单或订单已无效。").show();
                }
            }
        });
        viewHolder.textViewShopNamePrice.setText(foodOrder.getSeller_name() + "-￥"
                + foodOrder.getTotalPrice());
        viewHolder.textViewStatus.setText(OrderStatus.getStatus(foodOrder.getStatus()));
        viewHolder.textViewTime.setText(foodOrder.getTime());
        viewHolder.textViewRemark.setText(foodOrder.getRemark());
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewStatus;
        TextView textViewShopNamePrice;
        TextView textViewTime;
        TextView textViewRemark;
        Button buttonCall;
        Button buttonCancel;
        Button buttonShowup;
        Button buttonClose;
        ImageView shopIcon;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewStatus = (TextView) itemView
                    .findViewById(R.id.order_list_item_status);
            textViewShopNamePrice = (TextView) itemView
                    .findViewById(R.id.order_list_restaurant_name);
            textViewTime = (TextView) itemView
                    .findViewById(R.id.order_list_item_time);
            textViewRemark = (TextView) itemView
                    .findViewById(R.id.order_list_item_msg);
            buttonCall = (Button) itemView.findViewById(R.id.btn_callshop);
            buttonCancel = (Button) itemView.findViewById(R.id.btn_cancel);
            buttonShowup = (Button) itemView.findViewById(R.id.btn_showup);
            buttonClose = (Button) itemView.findViewById(R.id.btn_close);
            buttonShowup.setBackground(new IconicsDrawable(itemView.getContext()
                    , GoogleMaterial.Icon.gmd_keyboard_arrow_left).colorRes(R.color.colorPrimary).sizeDp(6));
            buttonClose.setBackground(new IconicsDrawable(itemView.getContext()
                    , GoogleMaterial.Icon.gmd_keyboard_arrow_right).color(Color.WHITE).sizeDp(6));
            shopIcon = (ImageView) itemView
                    .findViewById(R.id.order_list_shop_icon);
        }
    }
}
