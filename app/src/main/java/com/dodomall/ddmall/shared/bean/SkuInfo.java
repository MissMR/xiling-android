package com.dodomall.ddmall.shared.bean;

import android.text.TextUtils;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.dduis.bean.DDRushSpuBean;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.google.gson.annotations.SerializedName;
import com.dodomall.ddmall.shared.constant.AppTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkuInfo implements Serializable {


    /**
     * 根据首页今日必推数据转换的SKU信息
     */
    public static SkuInfo fromRecommendData(DDProductBean spu) {
        SkuInfo sku = new SkuInfo();

        //SPU ID
        sku.productId = spu.getProductId();
        //商品名
        sku.name = spu.getProductName();
        sku.productName = spu.getProductName();
        //商品描述
        sku.desc = spu.getProductIntro();
        //库存
        sku.stock = (int) spu.getStock();

        //缩略图
        sku.thumbURL = spu.getThumbUrlForShopNow();

        //正常价格
        sku.price = spu.getMinPrice();
        //市场价
        sku.marketPrice = spu.getMaxMarketPrice();
        //优惠价格
        sku.retailPrice = spu.getMinPrice();
        //预期收益
        sku.rewardPrice = spu.getMaxRewardPrice();

        return sku;
    }

    /**
     * 根据新的SPU实体类生成用于分享的SKU信息
     *
     * @param spu SPU实体
     * @return SkuInfo SKU实体
     */
    public static SkuInfo from(DDProductBean spu) {
        SkuInfo sku = new SkuInfo();

        //SPU ID
        sku.productId = spu.getProductId();
        //商品名
        sku.name = spu.getProductName();
        sku.productName = spu.getProductName();
        //商品描述
        sku.desc = spu.getProductIntro();
        //库存
        sku.stock = (int) spu.getStock();

        //缩略图
        sku.thumbURL = spu.getThumbUrlForShopNow();

        if (spu.isFlashSale()) {

            //正常价格
            sku.price = spu.getMaxSalePrice();
            //市场价
            sku.marketPrice = spu.getMaxSalePrice();
            //优惠价格
            sku.retailPrice = spu.getMinScorePrice();
            //预期收益
            sku.rewardPrice = spu.getMaxBrokeragePrice();

        } else {
            //正常价格
            sku.price = spu.getMaxMarketPrice();
            //市场价
            sku.marketPrice = spu.getMaxMarketPrice();
            //优惠价格
            sku.retailPrice = spu.getMinScorePrice();
            //预期收益
            sku.rewardPrice = spu.getMaxRewardPrice();
        }
        return sku;
    }

    public static SkuInfo from(Product spu) {
        SkuInfo sku = new SkuInfo();

        //SPU ID
        sku.productId = spu.productId;
        //商品名
        sku.name = spu.name;
        sku.productName = spu.name;
        //商品描述
        sku.desc = spu.desc;
        //库存
        sku.stock = spu.spuStock;

        //缩略图
        sku.thumbURL = spu.thumb;

        //市场价
        sku.marketPrice = spu.getMarketPrice();
        //优惠价格
        sku.retailPrice = spu.getRetailPrice();
        //预期收益
        sku.rewardPrice = spu.getRewardPrice();

        return sku;
    }

    /**
     * 根据新的SPU实体类生成用于分享的SKU信息
     *
     * @param spu SPU实体
     * @return SkuInfo SKU实体
     */
    public static SkuInfo from(DDRushSpuBean spu) {
        SkuInfo sku = new SkuInfo();

        //SPU ID
        sku.productId = spu.getSpuId();
        //商品名
        sku.name = spu.getProductName();
        sku.productName = spu.getProductName();
        //商品描述
        sku.desc = "";
        //库存
        sku.stock = (int) spu.getInventory();

        //缩略图
        sku.thumbURL = spu.getThumbUrlForShopNow();

        //正常价格
        sku.price = spu.getMaxSalePrice();
        //市场价
        sku.marketPrice = spu.getMaxSalePrice();
        //优惠价格
        sku.retailPrice = spu.getMinScorePrice();
        //预期收益
        sku.rewardPrice = spu.getMaxBrokeragePrice();

        return sku;
    }

    @SerializedName("skuId")
    public String skuId;
    @SerializedName("productId")
    public String productId;
    @SerializedName("skuName")
    public String name;
    @SerializedName("productName")
    public String productName;
    @SerializedName("intro")
    public String desc;
    @SerializedName("stock")
    public int stock;
    // 商品标价
    @SerializedName("retailPrice")
    public long retailPrice;
    // 市场价
    @SerializedName("marketPrice")
    public long marketPrice;
    //分享赚预期收益
    @SerializedName("rewardPrice")
    public long rewardPrice = 0;
    @SerializedName("price")
    public long price;
    @SerializedName("saleCount")
    public long sales;
    @SerializedName("hasPresent")
    public boolean hasPresent;
    @SerializedName("weight")
    public long weight;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("discountStatus")
    public int discountStatus;
    @SerializedName("thumbUrl")
    public String thumb;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("sobotId")
    public String sobotId;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("hasCoupon")
    public int hasCoupon;
    @SerializedName("properties")
    public String properties;
    @SerializedName("collectStatus")
    public boolean isFav = false;
    @SerializedName("images")
    public List<String> images;
    @SerializedName("presents")
    public List<Presents> presents = new ArrayList<>();
    @SerializedName("tags")
    public List<Tag> tags = new ArrayList<>();
    @SerializedName("costPrice")
    public long costPrice;

    public String getProductUrl() {
        String url = BuildConfig.BASE_URL + "spu/" + productId;
        if (SessionUtil.getInstance().isLogin()) {
            url += "/" + SessionUtil.getInstance().getLoginUser().invitationCode;
        }
        return url;
    }

    /**
     * buyScore : 1
     */
    @SerializedName("buyScore")
    public int buyScore;
    /**
     * vipTypePrices : [{"vipType":4,"price":12000},{"vipType":3,"price":14000},{"vipType":2,"price":16000},{"vipType":1,"price":18000},{"vipType":0,"price":20000}]
     */
    @SerializedName("vipTypePrices")
    public List<CartItem.VipTypePricesEntity> vipTypePrices;
    /**
     * currentVipTypePrice : 15000
     */
    @SerializedName("currentVipTypePrice")
    public long currentVipTypePrice;
    @SerializedName("totalSaleCount")
    public long totalSaleCount;

    //收藏量
    @SerializedName("likeCount")
    public String likeCount = "1";

    /**
     * 0：已下架
     */
    @SerializedName("status")
    public int status;
    /**
     * 0、默认，1跨境购产品
     * 2、团购
     * 3、会员特卖
     */
    @SerializedName("extType")
    public int extType;
    @SerializedName("hasDifferentPrice")
    public int hasDifferentPrice;
    @SerializedName("groupSkuInfo")
    public GroupSkuInfoEntity groupSkuInfo;

    /**
     * 分享最大价
     */
    @SerializedName("maxPrice")
    public long maxPrice;
    /**
     * 分享最低价
     */
    @SerializedName("minPrice")
    public long minPrice;
    @SerializedName("spec")
    public String spec;
    /**
     * 1:跨境购
     */
    public int isCross;
    @SerializedName("userTypeStr")
    public String userTypeStr;
    @SerializedName("sellBegin")
    public String sellBegin;
    @SerializedName("sellEnd")
    public String sellEnd;
    //   红框中原来展示的是thumbUrl
    @SerializedName("thumbUrlForShopNow")
    public String thumbURL;

    @SerializedName("vipType")
    private int vipType;

    //CommonProduct(0, "普通产品"),
    //Gift(1, "赠品"),
    //OpenGift(2, "开店礼包"),
    //SpikeProduct(3, "秒杀产品");
    //GetFacialMaskForFree(4, "0元领面膜"),
    //TodayGrounding(5, "今日上新"),
    //TomorrowGrounding(6, "明日上新"),
    //UnsharedForNew(7, "新人专享"),
    //RebateDoubling(8, "佣金翻倍");
    @SerializedName("productType")
    public int productType;

    @SerializedName("server")
    private String serviceUrl = "";//客服中心链接，用来链接live800

    @SerializedName("zeroBuyFlag")
    public int zeroBuyFlag;

    @SerializedName("is_shipping_free")
    public int isShippingFree;

    public boolean canBuyFree() {
        return isProductFree() && zeroBuyFlag == 1;
    }

    public boolean isTomorrowGrounding() {
        return this.productType == 6;
    }

    public boolean isFreightFree() {
        return isShippingFree == 1;
    }

    /**
     * 是否真的可以购买
     * 1 已下架
     * 2 库存是0
     * 3 商品是明日上新
     *
     * @return
     */
    public boolean checkBuyable() {
        if (this.status == 0 || isStockEmpty() || this.productType == 6) {
            return false;
        }
        return true;
    }

    /**
     * 购买按钮是否可点击
     * 库存为0时可点击
     *
     * @return
     */
    public boolean checkEnableButton() {
        if (this.status == 0 || this.productType == 6) {
            return false;
        }
        return true;
    }

    // 库存不足
    public boolean isStockEmpty() {
        return this.stock <= 0;
    }

    /**
     * @return 商品不可购买时的 提示文字
     */
    public String getUnBuyableHintText() {
        String greyText = null;
        if (this.status == 0) {
            greyText = "已下架";
        }
        return greyText;
    }

    public boolean isStoreMaster() {
        return SessionUtil.getInstance().isLogin() && SessionUtil.getInstance().getLoginUser().isStoreMaster();
    }

    public boolean isStoreGift() {
        return productType == 2;
    }

    public boolean isProductFree() {
        // 店主送流量 和 0元助力
        return productType == 4 || productType == 10;
    }

    public static class GroupSkuInfoEntity implements Serializable {
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

    public long getTypePrice(int vipType) {
        long price = retailPrice;
        if (vipTypePrices != null && vipTypePrices.size() > 0) {
            for (CartItem.VipTypePricesEntity vipTypePrice : vipTypePrices) {
                if (vipTypePrice.vipType == vipType) {
                    price = vipTypePrice.price;
                }
            }
        }
        return price;
    }

    /**
     * @return vip 会员返利价
     */
    public long getVipRefundPrice() {
        return getTypePrice(AppTypes.FAMILY.MEMBER_ZUNXIANG) - getTypePrice(AppTypes.FAMILY.MEMBER_TIYAN);
    }

    /**
     * @return 是否显示成为店主的 view
     */
    public boolean isShowVipView() {
        return hasDifferentPrice == 1;
    }

    public String getServiceUrl() {
        if (TextUtils.isEmpty(serviceUrl)) {
            serviceUrl = "";
        }
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
