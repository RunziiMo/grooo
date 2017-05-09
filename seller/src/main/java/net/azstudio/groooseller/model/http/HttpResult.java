package net.azstudio.groooseller.model.http;

/**
 * Created by runzii on 16-5-7.
 */
public class HttpResult<T> {


    //用来模仿resultCode和resultMessage
    private int code;
    private String message;

    //用来模仿Data
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
