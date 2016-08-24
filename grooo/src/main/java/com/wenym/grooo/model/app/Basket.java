package com.wenym.grooo.model.app;

import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.model.http.OrderForm;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public enum Basket  {

    INSTANCE;

    private static Basket basket;
    private int buyNum = 0;// 购买数量
    private float totalPrice = 0;// 购买总价
    private int shopid;//商家id
    private boolean status;//商家状态
    private float min_price = 0;//最小价格
    private HashMap<Food, Integer> order;
    private DecimalFormat df;

    private Basket() {
        order = new HashMap<>();
        df = new DecimalFormat("0.0");
    }

    public int getShopid() {
        return shopid;
    }

    public void addFood(Food food) {
        buyNum++;
        totalPrice +=Float.parseFloat(food.getPrice());
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


    public HashMap<Food, Integer> getOrderMap() {
        return order;
    }

    public OrderForm getOrder() {
        OrderForm form = new OrderForm();
        form.setAddress(AppPreferences.get().getAddress().getAddress());
        form.setBuilding(AppPreferences.get().getAddress().getBuilding());
        List<Order.DetailBean> beanList = new LinkedList<>();
        for (Food food : order.keySet()) {
            Order.DetailBean bean = new Order.DetailBean();
            bean.setName(food.getName());
            bean.setCount(order.get(food).toString());
            beanList.add(bean);
        }
        form.setDetail(beanList);
        return form;
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
        return df.format(totalPrice);
    }

    public String getNeededPrice() {
        return df.format(min_price - totalPrice);
    }

    public void init(Shop shop) {
        min_price = shop.getBasePrice();
        status = shop.getStatus() == 0 ? false : true;
        shopid = shop.getId();
        buyNum = 0;
        totalPrice = 0;
        order = new HashMap<>();
    }

    public enum OrderStatus {
        RESTING, NOTENOUGH, OK
    }

}
