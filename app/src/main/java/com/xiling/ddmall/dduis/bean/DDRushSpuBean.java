package com.xiling.ddmall.dduis.bean;

import java.io.Serializable;

public class DDRushSpuBean implements Serializable {

    private String id = "";
    private String flashSaleId = "";
    private String productName = "";
    private String spuId = "";
    private String flashSaleSpuId = "";
    private long retailPrice = 0;
    private long minScorePrice = 0;
    private long maxSalePrice = 0;
    private long maxBrokeragePrice = 0;
    private String thumbUrlForShopNow = "";
    private long saleCount = 0;
    private long inventory = 0;

    private float flashInventoryRate = 0f;

    //推广数量
    private long extendTime = 0;

    //关注数量
    private long focusTime = 0;
    //是否关注
    private int focus = 0;

    public boolean isNotice() {
        return focus == 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlashSaleSpuId() {
        return flashSaleSpuId;
    }

    public void setFlashSaleSpuId(String flashSaleSpuId) {
        this.flashSaleSpuId = flashSaleSpuId;
    }

    public String getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(String flashSaleId) {
        this.flashSaleId = flashSaleId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public long getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public long getMinScorePrice() {
        return minScorePrice;
    }

    public void setMinScorePrice(long minScorePrice) {
        this.minScorePrice = minScorePrice;
    }

    public long getMaxSalePrice() {
        return maxSalePrice;
    }

    public void setMaxSalePrice(long maxSalePrice) {
        this.maxSalePrice = maxSalePrice;
    }

    public long getMaxBrokeragePrice() {
        return maxBrokeragePrice;
    }

    public void setMaxBrokeragePrice(long maxBrokeragePrice) {
        this.maxBrokeragePrice = maxBrokeragePrice;
    }

    public String getThumbUrlForShopNow() {
        return thumbUrlForShopNow;
    }

    public void setThumbUrlForShopNow(String thumbUrlForShopNow) {
        this.thumbUrlForShopNow = thumbUrlForShopNow;
    }

    public long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(long saleCount) {
        this.saleCount = saleCount;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }

    public long getExtendTime() {
        return extendTime;
    }

    public void addExtendTime() {
        this.extendTime++;
    }

    public void setExtendTime(long extendTime) {
        this.extendTime = extendTime;
    }

    public long getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(long focusTime) {
        this.focusTime = focusTime;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public float getFlashInventoryRate() {
        return flashInventoryRate;
    }

    public void setFlashInventoryRate(float flashInventoryRate) {
        this.flashInventoryRate = flashInventoryRate;
    }
}
