package com.wenym.grooo.model;

import android.support.v7.widget.RecyclerView;

import com.wenym.grooo.widgets.Toasts;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ShoppingBasket {
    private static ShoppingBasket basket;
    private int buyNum = 0;// 购买数量
    private float totalPrice = 0;// 购买总价
    private float packagePrice = 0;// 餐盒费
    private float discountstartprice = 0;// 减价开始价格
    private float discountprice = 0;// 减价价格
    private String discountinfo = "";// 减价信息
    private String shopid;//商家id
    private boolean status;//商家状态
    private float min_price = 0;//最小价格
    private HashMap<Food, Integer> order;
    private DecimalFormat df;
    private RecyclerView.Adapter adapter;

    private ShoppingBasket() {
        order = new HashMap<Food, Integer>();
        df = new DecimalFormat("0.0");
    }

    public static ShoppingBasket getInstance() {
        if (basket == null) {
            basket = new ShoppingBasket();
        }
        return basket;
    }

    public String getShopid() {
        return shopid;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public void addFood(Food food) {
        buyNum++;
        totalPrice += Float.parseFloat(food.getPrice());
        packagePrice += Float.parseFloat(food.getPackageprice());
        if (order.containsKey(food)) {
            order.put(food, order.get(food) + 1);
        } else {
            order.put(food, 1);
        }
    }

    public void minusFood(Food food) {

        if (order.containsKey(food)) {
            if (order.get(food) == 1) {
                order.remove(food);
            } else {
                order.put(food, order.get(food) - 1);
            }
            buyNum--;
            totalPrice -= Float.parseFloat(food.getPrice());
            packagePrice -= Float.parseFloat(food.getPackageprice());
        } else {
            Toasts.show("本来就没有");
        }
    }

    public int getFoodBuyNum(Food food) {
        if (order.containsKey(food)) {
            return order.get(food);
        } else {
            return 0;
        }
    }

    public HashMap<Food, Integer> getOrder() {
        return order;
    }

    public OrderStatus isOkToPay() {
        if (!status)
            return OrderStatus.RESTING;
        if (totalPrice >= min_price)
            return OrderStatus.OK;
        else
            return OrderStatus.NOTENOUGH;
    }

    public String getTotalPrice() {
        return df.format(totalPrice + packagePrice);
    }

    public String getPackagePrice() {
        return df.format(packagePrice);
    }

    public String getNeededPrice() {
        return df.format(min_price - totalPrice);
    }

    public void init(Restaurant restaurant) {
        discountstartprice = restaurant.getDiscountStartPrice();// 减价开始价格
        discountprice = restaurant.getDiscountPrice();// 减价价格
        min_price = restaurant.getBasePrice();
        status = restaurant.getStatus();
        shopid = restaurant.getId();
        discountinfo = "";// 减价信息
        buyNum = 0;
        totalPrice = 0;
        packagePrice = 0;
        order = new HashMap<Food, Integer>();
    }

    public enum OrderStatus {
        RESTING, NOTENOUGH, OK
    }

}
