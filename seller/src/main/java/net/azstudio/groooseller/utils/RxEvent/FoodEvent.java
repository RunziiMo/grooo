package net.azstudio.groooseller.utils.RxEvent;

import net.azstudio.groooseller.model.http.HttpFood;

/**
 * Created by runzii on 16-5-23.
 */
public class FoodEvent {

    private HttpFood food;
    private Method method;

    public FoodEvent(HttpFood food, Method method) {
        this.food = food;
        this.method = method;
    }

    public HttpFood getFood() {
        return food;
    }

    public void setFood(HttpFood food) {
        this.food = food;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public static enum Method{
        add,delete,edit
    }
}
