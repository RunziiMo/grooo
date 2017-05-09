package net.azstudio.groooseller.provider;

/**
 * Fragment传参的参数
 * Created by jpardogo on 22/02/2014.
 */
public enum ExtraArgumentKeys {
    OPEN_ACTIVITES("OPEN_ACTIVITES"),
    SHOPS("shops"),
    ISFOODORDER("what am i doing"),
    ORDERS("orders"),
    MENUS("menus");

    private String text;

    private ExtraArgumentKeys(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
