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
import com.wenym.grooo.model.ecnomy.DeliveryOrder;
import com.wenym.grooo.utils.OrderStatus;
import com.wenym.grooo.widgets.Toasts;

import java.util.List;

public class DeliveryOrderAdapter extends
        RecyclerSwipeAdapter<DeliveryOrderAdapter.SimpleViewHolder> {

    protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(
            this);
    private Context mContext;
    private List<DeliveryOrder> mDataset;

    public DeliveryOrderAdapter(Context context, List<DeliveryOrder> mlist) {
        this.mContext = context;
        this.mDataset = mlist;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_delivery_order, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder,
                                 final int position) {
        final DeliveryOrder order = mDataset.get(position);
        Picasso.with(viewHolder.shopIcon.getContext()).load(order.getShop_icon()).into(viewHolder.shopIcon);
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
        viewHolder.buttonCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.closeAllItems();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + order.getShopPhoneNumber()));
                mContext.startActivity(intent);
            }
        });
        viewHolder.textViewShopNamePrice.setText(order.getSeller_name() + "-"
                + order.getTophonenumber()+"收");
        viewHolder.textViewTime.setText(order.getTime());
        viewHolder.textViewRemark.setText(order.getRemark());
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
        TextView textViewShopNamePrice;
        TextView textViewTime;
        TextView textViewRemark;
        Button buttonCall;
        Button buttonShowup;
        Button buttonClose;
        ImageView shopIcon;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewShopNamePrice = (TextView) itemView
                    .findViewById(R.id.order_list_restaurant_name);
            textViewTime = (TextView) itemView
                    .findViewById(R.id.order_list_item_time);
            textViewRemark = (TextView) itemView
                    .findViewById(R.id.order_list_item_msg);
            buttonCall = (Button) itemView.findViewById(R.id.btn_callshop);
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
