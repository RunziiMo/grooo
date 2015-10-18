package com.wenym.grooo.http.util;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public interface HttpCallBack {

    //操作成功，达到预期目标
    void onSuccess(Object object);

    //操作失败
    void onFailed();

    //网络连接错误
    void onError(int statusCode);
}
