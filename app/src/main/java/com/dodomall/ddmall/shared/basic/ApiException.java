package com.dodomall.ddmall.shared.basic;

/**
 * Created by JayChan on 2016/12/15.
 */
public class ApiException extends Throwable {
    private int code;
    private String message;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
