package com.xiling.shared.bean.api;

import com.google.gson.annotations.SerializedName;
import com.xiling.shared.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * API请求结果
 * Created by JayChan on 2016/12/13.
 */
public class RequestResultList<T> implements Serializable {
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DatasEntity data;

    public boolean isNotLogin() {
        return code == Constants.NOT_LOGIN_CODE;
    }

    public boolean isFail() {
        return code == Constants.ERROR_CODE;
    }

    public boolean isSuccess() {
        return code == Constants.SUCCESS_CODE;
    }

    public class DatasEntity{
        @SerializedName("pageOffset")
        public int pageOffset;
        @SerializedName("pageSize")
        public int pageSize;
        @SerializedName("totalRecord")
        public int totalRecord;
        @SerializedName("totalPage")
        public int totalPage;
        @SerializedName("datas")
        public List<T> datas;
    }
}
