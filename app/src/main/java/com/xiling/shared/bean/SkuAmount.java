package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-06-09
 */
public class SkuAmount implements Serializable {

    @SerializedName("skuId")
    public String skuId;
    @SerializedName("quantity")
    public int amount = 1;

    // 活动类型（1 无活动  2限时抢购）
    @SerializedName("activityType")
    int activityType = 1;
    @SerializedName("flashSaleId")
    String flashSaleId = "";

    public SkuAmount() {
    }

    public SkuAmount(String skuId, int amount) {
        this.skuId = skuId;
        this.amount = amount;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(String flashSaleId) {
        this.flashSaleId = flashSaleId;
    }
}
