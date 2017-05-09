package net.azstudio.groooseller.utils.RxEvent;

import net.azstudio.groooseller.model.http.HttpFood;

import java.util.List;

/**
 * Created by runzii on 16-5-8.
 */
public class CategoryClickEvent {

    private int position;
    private List<HttpFood> foods;

    public CategoryClickEvent(int position, List<HttpFood> foods) {
        this.position = position;
        this.foods = foods;
    }

    public int getPosition() {
        return position;
    }

    public List<HttpFood> getFoods() {
        return foods;
    }
}
