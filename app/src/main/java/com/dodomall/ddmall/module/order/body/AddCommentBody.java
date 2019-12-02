package com.dodomall.ddmall.module.order.body;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/9.
 */
public class AddCommentBody {

    /**
     * orderCode : 0791487461653098
     * order1Id : 1fb3acfab0694fc4af909c444e524ccb
     * content : 这个价格实惠质量好
     * images : ["图片1","图片2","图片3"]
     * descScore : 5
     */

    @SerializedName("orderCode")
    public String orderCode;
    @SerializedName("order1Id")
    public String order1Id;
    @SerializedName("content")
    public String content;
    @SerializedName("images")
    public List<String> images;
    @SerializedName("descScore")
    public int descScore;
    @SerializedName("expressScore")
    public int expressScore;
    @SerializedName("serveScore")
    public int serveScore;

    public AddCommentBody(String orderCode, String order1Id, String content, List<String> images, int descScore, int expressScore, int serveScore) {
        this.orderCode = orderCode;
        this.order1Id = order1Id;
        this.content = content;
        this.images = images;
        this.descScore = descScore;
        this.expressScore = expressScore;
        this.serveScore = serveScore;
    }
}
