package com.wenym.grooo.model.ecnomy;

public class Food {

    private String name;
    private String price;
    private String packageprice;
    private int id;
    private int numpermonth;
    private String image;

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

    public String getPackageprice() {
        return packageprice;
    }

    public void setPackageprice(String packageprice) {
        this.packageprice = packageprice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumpermonth() {
        return numpermonth;
    }

    public void setNumpermonth(int numpermonth) {
        this.numpermonth = numpermonth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", packageprice='" + packageprice + '\'' +
                ", id=" + id +
                ", numpermonth=" + numpermonth +
                ", image='" + image + '\'' +
                '}';
    }
}
