package com.wenym.grooo.provider;

/**
 * Fragment传参的参数
 * Created by jpardogo on 22/02/2014.
 */
public enum ExtraArgumentKeys {
    OPEN_ACTIVITES("OPEN_ACTIVITES"),
    SHOPS("shops"),
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
