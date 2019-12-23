package com.xiling.dduis.bean;

import com.xiling.shared.bean.Tag;

import java.io.Serializable;
import java.util.ArrayList;

public class DDProductBean implements Serializable {

    //库存
    private long stock = 0;

    //SPU ID
    private String productId = "";
    //SPU Name
    private String productName = "";
    private String productIntro = "";

    //商品标签
    private ArrayList<Tag> tags = new ArrayList<>();

    //商品销量
    private long saleCount = 0;
    //分享次数 - 推广次数
    private long extendTime = 0;

    //商品大图
    private String thumbUrl = "";
    //商品缩略图
    private String thumbUrlForShopNow = "";

    //最小会员价 2.0展示
    private long minRetailPrice = 0;
    //最大会员价
    private long maxRetailPrice = 0;
    //最小市场价 2.0展示
    private long minMarketPrice = 0;
    //最大市场价
    private long maxMarketPrice = 0;
    //最小返利
    private long minRewardPrice = 0;
    //最大返利 2.0展示
    private long maxRewardPrice = 0;

    //抢购最小会员价格
    private long minScorePrice = 0;
    //抢购最大销售价
    private long maxSalePrice = 0;
    //抢购最大佣金
    private long maxBrokeragePrice = 0;

    //非抢购最小销售价格
    private long minPrice = 0;

    //是否是抢购 0 不是 1 是
    private int isFlashSale = 0;
    //抢购状态 (1.未开始,2.预售中,3.进行中,4.已结束
    private int flashSaleStatus = 1;
    //活动商品的库存
    private long flashInventory = 0;
    //活动商品的销量
    private long flashSaleCount = 0;

    //是否设置抢购提醒 0未设置提醒 ,1 已设置提醒
    private int remind = 0;
    //已关注人数
    private int focusTime = 0;

    public boolean isNotice() {
        return remind == 1;
    }

    //首页必推数据专用的是否抢光状态
    private int allStockFlag = 0;

    //场次ID
    private String fstId = "";
    //活动SPU ID，不是SPU ID
    private String flashSpuId = "";

    private float flashInventoryRate = 0f;

    public int getAllStockFlag() {
        return allStockFlag;
    }

    public void setAllStockFlag(int allStockFlag) {
        this.allStockFlag = allStockFlag;
    }

    public long getFlashInventory() {
        return flashInventory;
    }

    public void setFlashInventory(long flashInventory) {
        this.flashInventory = flashInventory;
    }

    public long getFlashSaleCount() {
        return flashSaleCount;
    }

    public void setFlashSaleCount(long flashSaleCount) {
        this.flashSaleCount = flashSaleCount;
    }

    public boolean isFlashSale() {
        return this.isFlashSale == 1;
    }

    public boolean isRushEnable() {
        return isFlashSale() && flashSaleStatus > 2;
    }

    public int getIsFlashSale() {
        return isFlashSale;
    }

    public void setIsFlashSale(int isFlashSale) {
        this.isFlashSale = isFlashSale;
    }

    public int getFlashSaleStatus() {
        return flashSaleStatus;
    }

    public void setFlashSaleStatus(int flashSaleStatus) {
        this.flashSaleStatus = flashSaleStatus;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
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

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductIntro() {
        return productIntro;
    }

    public void setProductIntro(String productIntro) {
        this.productIntro = productIntro;
    }

    public long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(long saleCount) {
        this.saleCount = saleCount;
    }

    public long getExtendTime() {
        return extendTime;
    }

    public void setExtendTime(long extendTime) {
        this.extendTime = extendTime;
    }

    public void addExtendTime() {
        this.extendTime++;
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

    public long getMinRetailPrice() {
        return minRetailPrice;
    }

    public void setMinRetailPrice(long minRetailPrice) {
        this.minRetailPrice = minRetailPrice;
    }

    public long getMaxRetailPrice() {
        return maxRetailPrice;
    }

    public void setMaxRetailPrice(long maxRetailPrice) {
        this.maxRetailPrice = maxRetailPrice;
    }

    public long getMinMarketPrice() {
        return minMarketPrice;
    }

    public void setMinMarketPrice(long minMarketPrice) {
        this.minMarketPrice = minMarketPrice;
    }

    public long getMaxMarketPrice() {
        return maxMarketPrice;
    }

    public void setMaxMarketPrice(long maxMarketPrice) {
        this.maxMarketPrice = maxMarketPrice;
    }

    public long getMinRewardPrice() {
        return minRewardPrice;
    }

    public void setMinRewardPrice(long minRewardPrice) {
        this.minRewardPrice = minRewardPrice;
    }

    public long getMaxRewardPrice() {
        return maxRewardPrice;
    }

    public void setMaxRewardPrice(long maxRewardPrice) {
        this.maxRewardPrice = maxRewardPrice;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getFstId() {
        return fstId;
    }

    public void setFstId(String fstId) {
        this.fstId = fstId;
    }

    public int getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(int focusTime) {
        this.focusTime = focusTime;
    }

    public String getFlashSpuId() {
        return flashSpuId;
    }

    public void setFlashSpuId(String flashSpuId) {
        this.flashSpuId = flashSpuId;
    }

    public float getFlashInventoryRate() {
        return flashInventoryRate;
    }

    public void setFlashInventoryRate(float flashInventoryRate) {
        this.flashInventoryRate = flashInventoryRate;
    }
}
