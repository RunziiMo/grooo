package net.azstudio.groooseller.provider;

import java.util.HashMap;

/**
 * 用于查找转换状态
 * Created by runzii on 15-11-6.
 */
public class OrderStatus {

    private static HashMap<Integer, String> statusList;

    static {
        statusList = new HashMap<>();
        statusList.put(0, "未处理");
        statusList.put(10, "已接单");
        statusList.put(20, "申请退单");
        statusList.put(21, "退单完成");
        statusList.put(22, "已取消");
        statusList.put(30, "已完成");
        statusList.put(31, "已评价");
    }

    public static String getStatus(int num) {
        return statusList.get(num);
    }
}
