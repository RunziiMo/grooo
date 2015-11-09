package com.wenym.grooo.utils;

import java.util.HashMap;

/**
 * 用于查找转换状态
 * Created by runzii on 15-11-6.
 */
public class OrderStatus {

    private static HashMap<String, String> statusList;

    static {
        statusList = new HashMap<>();
        statusList.put("0", "未接单");
        statusList.put("1", "已接单");
        statusList.put("2", "已申请退单");
        statusList.put("3", "已无效");
        statusList.put("4", "已配送");
    }

    public static String getStatus(String num) {
        return statusList.get(num);
    }
}
