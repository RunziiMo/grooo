package net.azstudio.groooseller.model.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpFood {

    /**
     * category : 类别1
     * description : 描述
     * id : 1
     * logo : null
     * monthSold : 0
     * name : 测试商品1
     * price : 10
     */

    private String category;
    private String description;
    private Integer id;
    private String logo;
    private String monthSold;
    private String name;
    private String price;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMonthSold() {
        return monthSold;
    }

    public void setMonthSold(String monthSold) {
        this.monthSold = monthSold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
