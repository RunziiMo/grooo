package net.azstudio.groooseller.model.business;

import net.azstudio.groooseller.model.http.HttpFood;

import java.util.List;

/**
 * Created by runzii on 16-5-8.
 */
public class Menu {

    String category;
    List<HttpFood> foods;

    public Menu(String category, List<HttpFood> foods) {
        this.category = category;
        this.foods = foods;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<HttpFood> getFoods() {
        return foods;
    }

    public void setFoods(List<HttpFood> foods) {
        this.foods = foods;
    }
}
