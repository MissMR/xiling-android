package com.xiling.ddmall.shared.bean.api;

import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.Constants;

import java.io.Serializable;

/**
 * API请求结果
 * Created by JayChan on 2016/12/13.
 */
public class RequestResult<T> implements Serializable {
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public T data;

    public boolean isNotLogin() {
        return code == Constants.NOT_LOGIN_CODE;
    }

    public boolean isFail() {
        return code == Constants.ERROR_CODE;
    }

    public boolean isSuccess() {
        return code == Constants.SUCCESS_CODE;
    }

}
