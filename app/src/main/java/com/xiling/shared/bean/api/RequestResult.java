package com.xiling.shared.bean.api;

import com.xiling.shared.Constants;

import java.io.Serializable;

/**
 * API请求结果
 * Created by JayChan on 2016/12/13.
 */
public class RequestResult<T> implements Serializable{

    public int code;
    public String businessCode;
    public String message;
    public T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
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
