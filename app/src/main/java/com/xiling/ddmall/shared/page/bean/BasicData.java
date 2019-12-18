package com.xiling.ddmall.shared.page.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BasicData implements Serializable {

    @SerializedName("event")
    public String event;
    @SerializedName("target")
    public String target;
    @SerializedName("image")
    public String image;
    @SerializedName("showTimer")
    public boolean showTimer;
    @SerializedName("beginTime")
    public String beginTime;
    @SerializedName("endTime")
    public String endTime;
    @SerializedName("label")
    public String label;
    @SerializedName("icon")
    public String icon;
    @SerializedName("skuId")
    public String skuId;
}
