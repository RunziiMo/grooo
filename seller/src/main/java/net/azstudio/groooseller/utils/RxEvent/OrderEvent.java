package net.azstudio.groooseller.utils.RxEvent;

import net.azstudio.groooseller.model.business.FoodOrder;
import net.azstudio.groooseller.model.http.HttpFood;

/**
 * Created by runzii on 16-5-23.
 */
public class OrderEvent {

    private FoodOrder order;

    public FoodOrder getOrder() {
        return order;
    }

    public OrderEvent(FoodOrder order) {
        this.order = order;
    }
}
