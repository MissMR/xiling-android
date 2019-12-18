package com.xiling.ddmall.shared.bean;

import android.text.TextUtils;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.xiling.ddmall.ddui.bean.CommunityDataBean;
import com.xiling.ddmall.ddui.bean.ProductEvaluateBean;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    @SerializedName("productId")
    public String productId;
    @SerializedName("productName")
    public String name;
    // 销量
    @SerializedName("saleCount")
    public long saleCount;
    // 购买人数
    @SerializedName("saleIncCount")
    public int saleIncCount;

    @SerializedName("status")
    public int status;
    @SerializedName("info")
    public String desc;
    @SerializedName("content")
    public String content;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("thumbUrl")
    public String thumb;
    @SerializedName("store")
    public Store store;
    @SerializedName("intro")
    public String intro;
    // 素材总数
    @SerializedName("roundCount")
    public int roundMaterialCount;
    // 商品类型
    //CommonProduct(0, "普通产品"),
    //Gift(1, "赠品"),
    //OpenGift(2, "开店礼包"),
    //SpikeProduct(3, "秒杀产品");
    //GetFacialMaskForFree(4, "0元领面膜"),
    //TodayGrounding(5, "今日上新"),
    //TomorrowGrounding(6, "明日上新"),
    //UnsharedForNew(7, "新人专享"),
    //RebateDoubling(8, "佣金翻倍");
    @SerializedName("type")
    public int productType;
    @SerializedName("likeFlag")
    public int likeFlag;
    // 价格相关
    @SerializedName("maxPrice")
    public long maxRetailPrice;
    // 佣金收入
    @SerializedName("maxRewardPrice")
    public long rewardPrice;
    // 商品标价
    @SerializedName("minPrice")
    public long retailPrice;
    // 市场价 划横线的
    @SerializedName("maxMarketPrice")
    public long marketPrice;

    @SerializedName("spuStock")
    public int spuStock;

    // 商品测评
    @SerializedName("productEvaluate")
    public ProductEvaluateBean productEvaluate;

    // 限时抢购相关
    @SerializedName("flashSaleDetail")
    public FlashSaleDetail flashSaleDetail;
    // 限时抢购标志位
    @SerializedName("flashSaleFlag")
    public int flashSaleFlag;

    @SerializedName("properties")
    public List<Property> properties;
    @SerializedName("tags")
    public List<Tag> tags;
    @SerializedName("images")
    public List<String> images = new ArrayList<>();
    @SerializedName("skus")
    public List<SkuPvIds> skus = new ArrayList<>();
    @SerializedName("auths")
    public List<ProductAuth> auths = new ArrayList<>();
    @SerializedName("roundMaterials")
    public List<CommunityDataBean> roundMaterials = new ArrayList<>();
    // 推荐产品
    @SerializedName("productRelationList")
    public List<RelationProduct> relationProducts = new ArrayList<>();

    @SerializedName("productRelationFlag")
    public int productRelationFlag;

    public boolean hasSetRelationProduct() {
        return productRelationFlag == 1;
    }

    // 已经选择的sku信息
    private SkuPvIds selectedSkuPvIds;

    // 零售价
    public long getRetailPrice() {
        if (isFlashSaleActive()) {
            return flashSaleDetail.getRetailPrice();
        }
        return this.retailPrice;
    }

    // 返利
    public long getRewardPrice() {
        if (isFlashSaleActive()) {
            return flashSaleDetail.getMaxBrokeragePrice();
        }
        return this.rewardPrice;
    }

    // 市场价
    public long getMarketPrice() {
        if (isFlashSaleActive()) {
            return flashSaleDetail.getMaxSalePrice();
        }
        return this.marketPrice;
    }

    // 抢购活动 预售或抢购中
    public boolean isFlashSaleActive() {
        return isFlashSale() &&
                (flashSaleDetail.isBeforeFlashSale24() || flashSaleDetail.isInFlashSale());
    }

    // 普通商品库存为0
    // 抢购期间 活动商品库存为0
    public boolean hasSoldOut() {
        return spuStock <= 0
                || (isFlashSale() && flashSaleDetail.isInFlashSale() && flashSaleDetail.hasSoldOut());
    }

    public boolean isProductLiked() {
        return this.likeFlag == 1;
    }

    // 重置规格值  清除已经选择的规格值
    public void resetPropertyState() {
        for (Property p : properties) {
            p.reset();
        }
    }

    // 是否有关联商品
    public boolean hasSuggestProduct() {
        return this.relationProducts != null && this.relationProducts.size() > 0;
    }

    // 是否有商品测评
    public boolean hasProductEvaluate() {
        return this.productEvaluate != null && !TextUtils.isEmpty(this.productEvaluate.getEngineerId());
    }

    public CharSequence getRetailPriceStr() {

        long retailPrice = isFlashSaleActive() ? flashSaleDetail.getRetailPrice() : this.retailPrice;

        String price = ConvertUtil.cent2yuanNoZero(retailPrice);
        SpannableStringUtils.Builder spannableStringBuilder = SpannableStringUtils.getBuilder("￥")
                .setProportion(0.6f);
        if (price.contains(".")) {
            spannableStringBuilder.append(price.split("\\.")[0])
                    .append(".")
                    .append(price.split("\\.")[1])
                    .setProportion(0.7F);
        } else {
            spannableStringBuilder.append(price);
        }

        if (skus != null && skus.size() > 1 && retailPrice != maxRetailPrice) {
            spannableStringBuilder.append("起")
                    .setProportion(0.5F);
        }

        return spannableStringBuilder.create();
    }

    /**
     * 1、100W以下显示：实际数据；
     * 2、100W以上显示：以万为单位计数，比如100万；
     *
     * @return
     */
    public String getFormatSaleCount() {
        return ConvertUtil.formatSaleCount(saleCount);
    }

    /**
     * 购买按钮是否可点击
     * 下架
     * 库存为0时不可点击
     * 明日上新
     *
     * @return
     */
    public boolean checkProductEnable() {
        if (this.status == 0 || this.productType == 6 || hasSoldOut()) {
            return false;
        }
        return true;
    }

    /**
     * @return 商品不可购买时的 提示文字
     */
    public String getUnBuyableHintText() {
        String greyText = null;
        if (this.status == 0) {
            greyText = "已下架";
        } else if (hasSoldOut()) {
            greyText = "已抢光";
        }
        return greyText;
    }

    public boolean checkBuyable() {
        if (this.status == 0 || this.spuStock <= 0 || this.productType == 6) {
            return false;
        }
        return true;
    }

    public boolean isTomorrowGrounding() {
        return this.productType == 6;
    }

    public boolean isStoreGift() {
        return productType == 2;
    }

    public boolean isProductFree() {
        // 店主送流量 和 0元助力
        return productType == 4 || productType == 10;
    }

    public boolean isFlashSale() {
        return flashSaleFlag == 1 && !TextUtils.isEmpty(flashSaleDetail.getStartTime());
    }

    public SkuPvIds getSelectedSkuPvIds() {
        return selectedSkuPvIds;
    }

    public void setSelectedSkuPvIds(SkuPvIds selectedSkuPvIds) {
        this.selectedSkuPvIds = selectedSkuPvIds;
    }

    public static class FlashSaleDetail {

        /**
         * {
         * "retailPrice": 0,
         * "minScorePrice": 0,   最小的零售价格
         * "maxSalePrice": 0,    最大的市场价格
         * "maxBrokeragePrice": 0,   最大的返利价格
         * "focusTime": "",    关注数量
         * "extendTime": "",   推广数量
         * "inventory": 10000,   库存
         * "saleCount": 11,    销售数量
         * "startTime": "2019-04-11 00:00:00",    开始时间
         * "endTime": "2019-04-12 00:00:00",    结束时间
         * "online": 1,   上下架状态
         * "status": "",  开枪状态 1 已经点击开抢提醒   0 为点击开抢提醒
         * "saleIncCount": 0
         * },
         */
        @SerializedName("flashSpuId")
        private String flashSpuId;
        @SerializedName("minScorePrice")
        private long retailPrice;
        private long maxSalePrice;
        private long maxBrokeragePrice;
        private String focusTime;
        private String extendTime;
        private int inventory;
        private int saleCount;
        private String startTime;
        private String endTime;
        private int online;
        // 场次状态 活动状态：(1.未开始,2.预售中,3.进行中,4.已结束)
        private String status;
        // 开枪状态 1 已经点击开抢提醒   0 为点击开抢提醒
        private String focus;
        private int saleIncCount;
        // 抢购场次
        private String flashSaleId;

        private float flashInventoryRate;

        // 自行定义的
        private Date startDate;
        private Date endDate;

        public CharSequence getRetailPriceStr() {
            String price = ConvertUtil.cent2yuanNoZero(this.retailPrice);
            SpannableStringUtils.Builder spannableStringBuilder = SpannableStringUtils.getBuilder("￥")
                    .setProportion(0.6f);
            if (price.contains(".")) {
                spannableStringBuilder.append(price.split("\\.")[0])
                        .append(".")
                        .append(price.split("\\.")[1])
                        .setProportion(0.7F);
            } else {
                spannableStringBuilder.append(price);
            }
            return spannableStringBuilder.create();
        }

        // 是否已经设置了开抢提醒
        public boolean hasNotified() {
            return "1".equals(focus);
        }

        public boolean hasSoldOut() {
            return inventory <= 0;
        }

        public String getFormatStartTime() {
            checkInitDate();
            return TimeUtils.date2String(startDate, "MM月dd日HH:mm");
        }

        public void setFlashSaleStart() {
            this.status = "3";
        }

        public void setFlashSaleEnd() {
            this.status = "4";
        }

        // 抢购剩余时间
        public long getRemainingTime() {
            return endDate.getTime() - System.currentTimeMillis();
        }

        // 距离抢购开始的时间
        public long getTimeBeforeFlashSale() {
            return startDate.getTime() - System.currentTimeMillis();
        }

        //SPU活动开始前24小时进入预售模式
        //预售模式SPU只能看不能卖
        public boolean isBeforeFlashSale24() {
            return "2".equals(status);
        }

        public boolean isInFlashSale() {
            return "3".equals(status);
        }

        public boolean isAfterFlashSale() {
            return "4".equals(status);
        }

        private void checkInitDate() {
            if (startDate == null) {
                startDate = TimeUtils.string2Date(startTime);
            }
            if (endDate == null) {
                endDate = TimeUtils.string2Date(endTime);
            }
        }

        public float getFlashInventoryRate() {
            return flashInventoryRate;
        }

        public void setFlashInventoryRate(float flashInventoryRate) {
            this.flashInventoryRate = flashInventoryRate;
        }

        public String getFlashSpuId() {
            return flashSpuId;
        }

        public void setFlashSpuId(String flashSpuId) {
            this.flashSpuId = flashSpuId;
        }

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }

        public String getFlashSaleId() {
            return flashSaleId;
        }

        public void setFlashSaleId(String flashSaleId) {
            this.flashSaleId = flashSaleId;
        }

        public long getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(long retailPrice) {
            this.retailPrice = retailPrice;
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

        public String getFocusTime() {
            return focusTime;
        }

        public void setFocusTime(String focusTime) {
            this.focusTime = focusTime;
        }

        public String getExtendTime() {
            return extendTime;
        }

        public void setExtendTime(String extendTime) {
            this.extendTime = extendTime;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getSaleIncCount() {
            return saleIncCount;
        }

        public void setSaleIncCount(int saleIncCount) {
            this.saleIncCount = saleIncCount;
        }

    }


    public static class RelationProduct {

        /**
         * productId : do sed qui sint
         * productName : adipisicing aute
         * intro : voluptate ut nostr
         * minPrice : -3.5618220666167624E7
         * maxMarketPrice : -5.207469020212025E7
         * maxRewardPrice : -1.7058472502514467E7
         * thumbUrlForShopNow : non quis est
         * saleCount : 7.139169708724019E7
         * saleIncCount : -6.587785640695767E7
         */

        private String productId;
        private String productName;
        private String intro;
        private long minPrice;
        private long maxMarketPrice;
        private long maxRewardPrice;
        private String thumbUrlForShopNow;
        private int saleCount;
        private int saleIncCount;

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

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public long getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(long minPrice) {
            this.minPrice = minPrice;
        }

        public long getMaxMarketPrice() {
            return maxMarketPrice;
        }

        public void setMaxMarketPrice(long maxMarketPrice) {
            this.maxMarketPrice = maxMarketPrice;
        }

        public long getMaxRewardPrice() {
            return maxRewardPrice;
        }

        public void setMaxRewardPrice(long maxRewardPrice) {
            this.maxRewardPrice = maxRewardPrice;
        }

        public String getThumbUrlForShopNow() {
            return thumbUrlForShopNow;
        }

        public void setThumbUrlForShopNow(String thumbUrlForShopNow) {
            this.thumbUrlForShopNow = thumbUrlForShopNow;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
        }

        public int getSaleIncCount() {
            return saleIncCount;
        }

        public void setSaleIncCount(int saleIncCount) {
            this.saleIncCount = saleIncCount;
        }
    }


    /***********************************************************************************************
     之前活动团购相关老字段，未用到
     ***********************************************************************************************/


    /**
     * extType : 1
     * groupExt : {"activity":{"activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","title":"测试团购活动","rule":"团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则","orderLifeCycle":24,"joinCount":1,"joinMemberNum":2,"startDate":"2017-10-31 10:47:29","endDate":"2017-11-30 10:47:29","memberCreteDate":"2016-12-01 10:47:29"},"groupSkuList":[{"activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","productId":"7a90e4080ef94f8ba45a9903063a5633","skuId":"e6af3767684449418af03cd83d6b722a","groupPrice":45,"minBuyNum":1,"maxBuyNum":2,"groupLeaderReturn":0}],"activityInfoList":[{"orderId":"523812a29e034fc39c17a724c0d465b4","memberId":"0d3e5898d0c04bde8afec8adea40179d","headImage":"","nickName":"","activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","activityStatus":2,"activityStatusStr":"拼团成功","groupCode":"1571509764504472","groupLeaderReturn":0,"expiresDate":"2017-11-02 05:52:20","joinMemberNum":2,"createOrderNum":2,"payOrderNum":2,"payDate":"2017-11-01 19:52:20"},{"orderId":"523812a29e034fc39c17a724c0d465b4","memberId":"0d3e5898d0c04bde8afec8adea40179d","headImage":"","nickName":"","activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","activityStatus":2,"activityStatusStr":"拼团成功","groupCode":"1571509764504472","groupLeaderReturn":0,"expiresDate":"2017-11-02 05:52:20","joinMemberNum":2,"createOrderNum":2,"payOrderNum":2,"payDate":"2017-11-01 19:52:20"}]}
     */

    @SerializedName("extType")
    /**
     * 是否参与团购 2：参与
     * */
    public int extType;

    @SerializedName("groupExt")
    public GroupExtEntity groupExt;

    public GroupExtEntity.GroupSkuListEntity getGroupEntity(String skuId) {
        for (Product.GroupExtEntity.GroupSkuListEntity groupSkuListEntity : groupExt.groupSkuList) {
            if (skuId.equals(groupSkuListEntity.skuId)) {
                return groupSkuListEntity;
            }
        }
        return new GroupExtEntity.GroupSkuListEntity();
    }

    public static class GroupExtEntity implements Serializable {
        /**
         * activity : {"activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","title":"测试团购活动","rule":"团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则","orderLifeCycle":24,"joinCount":1,"joinMemberNum":2,"startDate":"2017-10-31 10:47:29","endDate":"2017-11-30 10:47:29","memberCreteDate":"2016-12-01 10:47:29"}
         * groupSkuList : [{"activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","productId":"7a90e4080ef94f8ba45a9903063a5633","skuId":"e6af3767684449418af03cd83d6b722a","groupPrice":45,"minBuyNum":1,"maxBuyNum":2,"groupLeaderReturn":0}]
         * activityInfoList : [{"orderId":"523812a29e034fc39c17a724c0d465b4","memberId":"0d3e5898d0c04bde8afec8adea40179d","headImage":"","nickName":"","activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","activityStatus":2,"activityStatusStr":"拼团成功","groupCode":"1571509764504472","groupLeaderReturn":0,"expiresDate":"2017-11-02 05:52:20","joinMemberNum":2,"createOrderNum":2,"payOrderNum":2,"payDate":"2017-11-01 19:52:20"},{"orderId":"523812a29e034fc39c17a724c0d465b4","memberId":"0d3e5898d0c04bde8afec8adea40179d","headImage":"","nickName":"","activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","activityStatus":2,"activityStatusStr":"拼团成功","groupCode":"1571509764504472","groupLeaderReturn":0,"expiresDate":"2017-11-02 05:52:20","joinMemberNum":2,"createOrderNum":2,"payOrderNum":2,"payDate":"2017-11-01 19:52:20"}]
         */

        @SerializedName("activity")
        public ActivityEntity activity;
        @SerializedName("groupSkuList")
        public List<GroupSkuListEntity> groupSkuList;
        @SerializedName("activityInfoList")
        public List<ActivityInfoListEntity> activityInfoList;

        public static class ActivityEntity {
            /**
             * activityId : b44c1c23fc1f4f6c8384e7c22ed409e5
             * title : 测试团购活动
             * rule : 团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则
             * orderLifeCycle : 24
             * joinCount : 1
             * joinMemberNum : 2
             * startDate : 2017-10-31 10:47:29
             * endDate : 2017-11-30 10:47:29
             * memberCreteDate : 2016-12-01 10:47:29
             */

            @SerializedName("activityId")
            public String activityId;
            @SerializedName("title")
            public String title;
            @SerializedName("rule")
            public String rule;
            @SerializedName("orderLifeCycle")
            public int orderLifeCycle;
            @SerializedName("joinCount")
            public int joinCount;
            @SerializedName("joinMemberNum")
            public int joinMemberNum;
            @SerializedName("startDate")
            public String startDate;
            @SerializedName("endDate")
            public String endDate;
            @SerializedName("memberCreteDate")
            public String memberCreteDate;
        }

        public static class GroupSkuListEntity {
            /**
             * activityId : b44c1c23fc1f4f6c8384e7c22ed409e5
             * productId : 7a90e4080ef94f8ba45a9903063a5633
             * skuId : e6af3767684449418af03cd83d6b722a
             * groupPrice : 45
             * minBuyNum : 1
             * maxBuyNum : 2
             * groupLeaderReturn : 0
             */

            @SerializedName("activityId")
            public String activityId;
            @SerializedName("productId")
            public String productId;
            @SerializedName("skuId")
            public String skuId;
            @SerializedName("groupPrice")
            public long groupPrice;
            @SerializedName("minBuyNum")
            public int minBuyNum;
            @SerializedName("maxBuyNum")
            public int maxBuyNum;
            @SerializedName("groupLeaderReturn")
            public int groupLeaderReturn;
        }

        public static class ActivityInfoListEntity implements Serializable {
            /**
             * orderId : 523812a29e034fc39c17a724c0d465b4
             * memberId : 0d3e5898d0c04bde8afec8adea40179d
             * headImage :
             * nickName :
             * activityId : b44c1c23fc1f4f6c8384e7c22ed409e5
             * activityStatus : 2
             * activityStatusStr : 拼团成功
             * groupCode : 1571509764504472
             * groupLeaderReturn : 0
             * expiresDate : 2017-11-02 05:52:20
             * joinMemberNum : 2
             * createOrderNum : 2
             * payOrderNum : 2
             * payDate : 2017-11-01 19:52:20
             */

            @SerializedName("orderId")
            public String orderId;
            @SerializedName("memberId")
            public String memberId;
            @SerializedName("headImage")
            public String headImage;
            @SerializedName("nickName")
            public String nickName;
            @SerializedName("activityId")
            public String activityId;
            @SerializedName("activityStatus")
            public int activityStatus;
            @SerializedName("activityStatusStr")
            public String activityStatusStr;
            @SerializedName("groupCode")
            public String groupCode;
            @SerializedName("groupLeaderReturn")
            public int groupLeaderReturn;
            @SerializedName("expiresDate")
            public String expiresDate;
            @SerializedName("joinMemberNum")
            public int joinMemberNum;
            @SerializedName("createOrderNum")
            public int createOrderNum;
            @SerializedName("payOrderNum")
            public int payOrderNum;
            @SerializedName("payDate")
            public String payDate;
        }
    }
}
