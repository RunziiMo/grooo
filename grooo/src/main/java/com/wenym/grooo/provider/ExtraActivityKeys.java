package com.wenym.grooo.provider;

/**
 * 好麻烦啊
 * Created by jpardogo on 22/02/2014.
 */
public enum ExtraActivityKeys {
    SHOPKIND("shop_kind"),
    CONFIRMPAY("confirm_pay"),
    PAYINFO("pay_info"),
    FRAGMENT("fragment_kind"),
    SUGGESTION("suggestion_content");

    private String text;

    private ExtraActivityKeys(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
