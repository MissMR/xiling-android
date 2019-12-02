package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/6.
 */
public class ExpressDetails {


    @SerializedName("message")
    public String message;
    @SerializedName("nu")
    public String nu;
    @SerializedName("ischeck")
    public String ischeck;
    @SerializedName("condition")
    public String condition;
    @SerializedName("com")
    public String com;
    @SerializedName("status")
    public String status;
    @SerializedName("state")
    public String state;
    @SerializedName("data")
    public List<DataEntity> data;

    public static class DataEntity {
        @SerializedName("time")
        public String time;
        @SerializedName("ftime")
        public String ftime;
        @SerializedName("context")
        public String context;
        @SerializedName("location")
        public String location;
    }
}
