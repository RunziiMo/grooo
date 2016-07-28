package com.wenym.grooo.util;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runzii.lib.utils.Logs;
import com.wenym.grooo.model.app.Shop;
import com.wenym.grooo.model.ecnomy.Basket;
import com.wenym.grooo.model.ecnomy.Order;
import com.wenym.grooo.ui.adapters.OrderDetailAdapter;
import com.wenym.grooo.ui.adapters.PayListAdapter;
import com.wenym.grooo.ui.adapters.ShopListAdapter;

import java.util.List;

/**
 * Created by runzii on 16-7-15.
 */
public class DataBindingNamespace {

    @BindingAdapter("entries")
    public static void entries(RecyclerView recyclerView, List<Shop> shops) {
        if (recyclerView.getAdapter() == null) {
            Logs.d("set entries " + shops.size());
            recyclerView.setAdapter(new ShopListAdapter(shops));
        } else {
            ShopListAdapter adapter = (ShopListAdapter) recyclerView.getAdapter();
            adapter.setContents(shops);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("entries_basket")
    public static void entriesBasket(RecyclerView recyclerView, Basket basket) {
        if (recyclerView.getAdapter() == null)
            recyclerView.setAdapter(new PayListAdapter(basket));
        else {
            Logs.d("entries_basket调用");
            PayListAdapter adapter = (PayListAdapter) recyclerView.getAdapter();
            adapter.setBasket(basket);
        }
    }

    @BindingAdapter("entries_order_detail")
    public static void entriesBasket(RecyclerView recyclerView, List<Order.DetailBean> detailBeen) {
        if (recyclerView.getAdapter() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(new OrderDetailAdapter(detailBeen));
        } else {
            Logs.d("entries_order_detail调用");
            OrderDetailAdapter adapter = (OrderDetailAdapter) recyclerView.getAdapter();
            adapter.setDetails(detailBeen);
        }
    }

    @BindingAdapter("logo")
    public static void entries(ImageView imageView, String logo) {
        if (!TextUtils.isEmpty(logo))
            Glide.with(imageView.getContext()).load(logo).into(imageView);
    }
}
