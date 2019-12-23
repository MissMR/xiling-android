package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/21.
 */
public class ProductComment {
    /**
     * nickName : 顶点00
     * properties : 颜色:蓝色 尺码:X码
     * content : 这个价格实惠质量好
     * images : ["图片1","图片2","图片3"]
     * reply :
     * payDate : 2017-03-01 17:01:12
     */

    @SerializedName("headImage")
    public String avatar;
    @SerializedName("nickName")
    public String nickName;
    @SerializedName("properties")
    public String properties;
    @SerializedName("content")
    public String content;
    @SerializedName("reply")
    public String reply;
    @SerializedName("payDate")
    public String payDate;
    @SerializedName("images")
    public List<String> images;
}
