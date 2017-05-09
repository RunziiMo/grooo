package net.azstudio.groooseller.model.business;

/**
 * Created by Wouldyou on 2015/6/1.
 */
public class OrderItem {

    private String id;
    private int count;

    @Override
    public String toString() {
        return "OrderItem{" +
                "id='" + id + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
