package com.xiling.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/8/31
 */
public class ProductBean {

    /**
     * skuId : d9031f4dfc574d05817f3d7d780bf1ea
     * productId : c8db8d84f5434955a96623f472cbac0b
     * skuName : ceshi
     * intro :
     * stock : 11111111
     * status : 0
     * saleCount : 0
     * totalSaleCount : 0
     * retailPrice : 222
     * marketPrice : 500
     * thumbUrl : http://192.168.1.161/group1/M00/00/01/wKgBoVt8DDKAVM85AAB609SIVRc284.jpg
     * tags : []
     * currentVipTypePrice : 0
     * produtUrl :
     */

    private String skuId;
    private String productId;
    private String skuName;
    private String intro;

    private long stock;

    private int status;

    private long saleCount;
    private long totalSaleCount;

    private long retailPrice;
    private long marketPrice;
    private long rewardPrice;

    private String thumbUrl;
    private String thumbUrlForShopNow;

    private int currentVipTypePrice;
    private String produtUrl;

    private List<?> tags;

    // byOrShare
    private String event;
    private String target;
    private String image;
    private String type;

    //0 未售罄  已售罄
    private int allStockFlag = 0;

    public static ProductBean convert(HomeBuyOrShare share) {
        ProductBean bean = new ProductBean();
        bean.setEvent(share.getEvent());
        bean.setTarget(share.getTarget());
        bean.setImage(share.getImage());
        bean.setType(share.getType());
        return bean;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbUrlForShopNow() {
        return thumbUrlForShopNow;
    }

    public void setThumbUrlForShopNow(String thumbUrlForShopNow) {
        this.thumbUrlForShopNow = thumbUrlForShopNow;
    }

    public int getCurrentVipTypePrice() {
        return currentVipTypePrice;
    }

    public void setCurrentVipTypePrice(int currentVipTypePrice) {
        this.currentVipTypePrice = currentVipTypePrice;
    }

    public String getProdutUrl() {
        return produtUrl;
    }

    public void setProdutUrl(String produtUrl) {
        this.produtUrl = produtUrl;
    }

    public List<?> getTags() {
        return tags;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(long saleCount) {
        this.saleCount = saleCount;
    }

    public long getTotalSaleCount() {
        return totalSaleCount;
    }

    public void setTotalSaleCount(long totalSaleCount) {
        this.totalSaleCount = totalSaleCount;
    }

    public long getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public long getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public long getRewardPrice() {
        return rewardPrice;
    }

    public void setRewardPrice(long rewardPrice) {
        this.rewardPrice = rewardPrice;
    }

    public int getAllStockFlag() {
        return allStockFlag;
    }

    public void setAllStockFlag(int allStockFlag) {
        this.allStockFlag = allStockFlag;
    }
}
