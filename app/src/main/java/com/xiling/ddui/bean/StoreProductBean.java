package com.xiling.ddui.bean;

import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.google.gson.annotations.SerializedName;

/**
 * created by Jigsaw at 2019/3/26
 */
public class StoreProductBean {

    /**
     * flashSaleSpu : {"flashSaleEndTime":1553652896000,"flashSaleId":"dsfasdfsdfsdfsdfslkj12","flashSaleName":"活动3","flashSaleOnline":"ONLINE","flashSaleStartTime":1553565680000,"flashSaleStatus":"ON_SELL","spuId":"0a124e149cb04286aba601a7015ba860","inventory":6,"maxBrokeragePrice":4,"maxSalePrice":3,"minScorePrice":2,"retailPrice":1,"saleCount":5}
     * maxMarketPrice : 1
     * minRetailPrice : 1
     * onPriceShopActualNumber : 1
     * onSaleShopDefaultNumber : 123
     * productId : 0a124e149cb04286aba601a7015ba860
     * productIntro : 343434344343
     * productLogoImageUrl : http://oss.dianduoduo.store/product/2019-02/2019022703175788463.png
     * productName : 测试规格
     * saleCount : 11
     * status : 1
     */
    @SerializedName("flashSaleSpuDTO")
    private FlashSaleSpuBean flashSaleSpu;
    private long maxMarketPrice;
    private long minRetailPrice;
    private long maxRewardPrice;
    private int onPriceShopActualNumber;
    private int onSaleShopDefaultNumber;
    private String productId;
    private String productIntro;
    private String productLogoImageUrl;
    private String productName;
    private int saleCount;
    private int stock;
    private int status;
    private String[] fakeAvatars;

    //  商品处于限时抢购的预热、活动中、已抢光状态时，商品主图左上角显示限时抢购的标签，标题下显示进度条
    public boolean isOnFlashSaleActive() {
        return flashSaleSpu != null;
    }

    public String getFormatSaleCount() {
        return ConvertUtil.formatSaleCount(saleCount);
    }

    // 是否有商品信息区域  三头像区域
    public boolean hasProductSaleInfo() {
        return hasProductSaleInfo(SessionUtil.getInstance().isMaster());
    }

    public boolean hasProductSaleInfo(boolean isMaster) {
        if (isMaster) {
            return onPriceShopActualNumber + onSaleShopDefaultNumber > 0;
        } else {
            return saleCount > 0;
        }
    }

    public boolean isSoldOut() {
        return this.stock <= 0;
    }

    public String[] getFakeAvatars() {
        return fakeAvatars;
    }

    public void setFakeAvatars(String[] fakeAvatars) {
        this.fakeAvatars = fakeAvatars;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public FlashSaleSpuBean getFlashSaleSpu() {
        return flashSaleSpu;
    }

    public void setFlashSaleSpu(FlashSaleSpuBean flashSaleSpu) {
        this.flashSaleSpu = flashSaleSpu;
    }

    public long getMaxMarketPrice() {
        return maxMarketPrice;
    }

    public void setMaxMarketPrice(long maxMarketPrice) {
        this.maxMarketPrice = maxMarketPrice;
    }

    public long getMinRetailPrice() {
        return minRetailPrice;
    }

    public void setMinRetailPrice(long minRetailPrice) {
        this.minRetailPrice = minRetailPrice;
    }

    public long getMaxRewardPrice() {
        return maxRewardPrice;
    }

    public void setMaxRewardPrice(long maxRewardPrice) {
        this.maxRewardPrice = maxRewardPrice;
    }

    public int getOnPriceShopActualNumber() {
        return onPriceShopActualNumber;
    }

    public void setOnPriceShopActualNumber(int onPriceShopActualNumber) {
        this.onPriceShopActualNumber = onPriceShopActualNumber;
    }

    public int getOnSaleShopDefaultNumber() {
        return onSaleShopDefaultNumber;
    }

    public void setOnSaleShopDefaultNumber(int onSaleShopDefaultNumber) {
        this.onSaleShopDefaultNumber = onSaleShopDefaultNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductIntro() {
        return productIntro;
    }

    public void setProductIntro(String productIntro) {
        this.productIntro = productIntro;
    }

    public String getProductLogoImageUrl() {
        return productLogoImageUrl;
    }

    public void setProductLogoImageUrl(String productLogoImageUrl) {
        this.productLogoImageUrl = productLogoImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class FlashSaleSpuBean {
        /**
         * flashSaleEndTime : 1553652896000
         * flashSaleId : dsfasdfsdfsdfsdfslkj12
         * flashSaleName : 活动3
         * flashSaleOnline : ONLINE
         * flashSaleStartTime : 1553565680000
         * flashSaleStatus : ON_SELL
         * spuId : 0a124e149cb04286aba601a7015ba860
         * inventory : 6
         * maxBrokeragePrice : 4
         * maxSalePrice : 3
         * minScorePrice : 2
         * retailPrice : 1
         * saleCount : 5
         */
        public static final String STATUS_PRE_SELL = "PRE_SELL";
        public static final String STATUS_ON_SELL = "ON_SELL";
        public static final String STATUS_END_SELL = "END_SELL";

        private long flashSaleEndTime;
        private String flashSaleId;
        private String flashSaleName;
        private String flashSaleOnline;
        private long flashSaleStartTime;
        private String flashSaleStatus;
        private String spuId;
        // 活动库存销量
        private int inventory;
        private int maxBrokeragePrice;
        private int maxSalePrice;
        private int minScorePrice;
        private int retailPrice;
        private int saleCount;
        private String status;

        public boolean isOnFlashSelling() {
            return this.status == STATUS_ON_SELL;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getFlashSaleEndTime() {
            return flashSaleEndTime;
        }

        public void setFlashSaleEndTime(long flashSaleEndTime) {
            this.flashSaleEndTime = flashSaleEndTime;
        }

        public String getFlashSaleId() {
            return flashSaleId;
        }

        public void setFlashSaleId(String flashSaleId) {
            this.flashSaleId = flashSaleId;
        }

        public String getFlashSaleName() {
            return flashSaleName;
        }

        public void setFlashSaleName(String flashSaleName) {
            this.flashSaleName = flashSaleName;
        }

        public String getFlashSaleOnline() {
            return flashSaleOnline;
        }

        public void setFlashSaleOnline(String flashSaleOnline) {
            this.flashSaleOnline = flashSaleOnline;
        }

        public long getFlashSaleStartTime() {
            return flashSaleStartTime;
        }

        public void setFlashSaleStartTime(long flashSaleStartTime) {
            this.flashSaleStartTime = flashSaleStartTime;
        }

        public String getFlashSaleStatus() {
            return flashSaleStatus;
        }

        public void setFlashSaleStatus(String flashSaleStatus) {
            this.flashSaleStatus = flashSaleStatus;
        }

        public String getSpuId() {
            return spuId;
        }

        public void setSpuId(String spuId) {
            this.spuId = spuId;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public int getMaxBrokeragePrice() {
            return maxBrokeragePrice;
        }

        public void setMaxBrokeragePrice(int maxBrokeragePrice) {
            this.maxBrokeragePrice = maxBrokeragePrice;
        }

        public int getMaxSalePrice() {
            return maxSalePrice;
        }

        public void setMaxSalePrice(int maxSalePrice) {
            this.maxSalePrice = maxSalePrice;
        }

        public int getMinScorePrice() {
            return minScorePrice;
        }

        public void setMinScorePrice(int minScorePrice) {
            this.minScorePrice = minScorePrice;
        }

        public int getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(int retailPrice) {
            this.retailPrice = retailPrice;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
        }
    }
}
