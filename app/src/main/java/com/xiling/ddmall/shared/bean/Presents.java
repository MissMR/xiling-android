package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/8.
 */
public class Presents {

    /**
     * skuId : 12c058e1f61747859f1ef8e6d3ea3120
     * productId : fa78dcc52f8a429b8eca85389bac2b99
     * skuName : 赠品测试
     * intro :
     * weight : 10
     * stock : 3422
     * retailPrice : 1
     * marketPrice : 1
     * totalSaleCount : 0
     * saleCount : 0
     * hasPresent : 0
     * quantity : 5
     * status : 0
     * discountStatus : 0
     * buyScore : 0
     * currentVipTypePrice : 0
     * thumbUrl : http://testimg.beautysecret.cn/G1/M00/00/05/cjcLYVmmWI-AE6lOAAAgOkC98AM555.png
     */

    @SerializedName("skuId")
    public String skuId;
    @SerializedName("productId")
    public String productId;
    @SerializedName("skuName")
    public String skuName;
    @SerializedName("intro")
    public String intro;
    @SerializedName("weight")
    public int weight;
    @SerializedName("stock")
    public int stock;
    @SerializedName("retailPrice")
    public int retailPrice;
    @SerializedName("marketPrice")
    public int marketPrice;
    @SerializedName("totalSaleCount")
    public int totalSaleCount;
    @SerializedName("saleCount")
    public int saleCount;
    @SerializedName("hasPresent")
    public int hasPresent;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("status")
    public int status;
    @SerializedName("discountStatus")
    public int discountStatus;
    @SerializedName("buyScore")
    public int buyScore;
    @SerializedName("currentVipTypePrice")
    public int currentVipTypePrice;
    @SerializedName("thumbUrl")
    public String thumbUrl;
}
