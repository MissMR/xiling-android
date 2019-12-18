package com.xiling.ddmall.module.push;

import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.bean.Tag;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/6.
 */
public class PushSkuDetailModel {

    /**
     * apiSkuVipTypePriceBeans : [{"vipType":4,"vipTypeStr":"钻石会员","price":0},{"vipType":3,"vipTypeStr":"铂金会员","price":4500},{"vipType":2,"vipTypeStr":"金卡会员","price":5500},{"vipType":1,"vipTypeStr":"尊享会员","price":6000},{"vipType":0,"vipTypeStr":"普通会员","price":6500}]
     * skuPushBean : {"skuId":"14be57381b354d3793569009608dfc85","productId":"0639376b963b43edb63c86015b4208a2","skuName":"美颜秘笈蔚蓝深海套装","intro":"","stock":0,"status":0,"saleCount":0,"retailPrice":26000,"marketPrice":29800,"thumbUrl":"http://testimg.beautysecret.cn/G1/M00/00/02/cjc2GFmNN8eANUJIAANfZa3H02k307.jpg","tags":[{"tagName":"自营"}],"maxPrice":6500,"minPrice":0}
     */

    @SerializedName("skuPushBean")
    public SkuPushBeanEntity skuPushBean;
    @SerializedName("apiSkuVipTypePriceBeans")
    public List<ApiSkuVipTypePriceBeansEntity> apiSkuVipTypePriceBeans;
    /**
     * userStoreType : 4
     * memberId : 42cb7a0728f1423197808204fa007412
     * userStoreTypeStr : 专营店主
     */

    @SerializedName("userStoreType")
    public int userStoreType;
    @SerializedName("memberId")
    public String memberId;
    @SerializedName("userStoreTypeStr")
    public String userStoreTypeStr;


    public static class SkuPushBeanEntity {
        /**
         * skuId : 14be57381b354d3793569009608dfc85
         * productId : 0639376b963b43edb63c86015b4208a2
         * skuName : 美颜秘笈蔚蓝深海套装
         * intro :
         * stock : 0
         * status : 0
         * saleCount : 0
         * retailPrice : 26000
         * marketPrice : 29800
         * thumbUrl : http://testimg.beautysecret.cn/G1/M00/00/02/cjc2GFmNN8eANUJIAANfZa3H02k307.jpg
         * tags : [{"tagName":"自营"}]
         * maxPrice : 6500
         * minPrice : 0
         */

        @SerializedName("skuId")
        public String skuId;
        @SerializedName("productId")
        public String productId;
        @SerializedName("skuName")
        public String skuName;
        @SerializedName("intro")
        public String intro;
        @SerializedName("stock")
        public int stock;
        @SerializedName("status")
        public int status;
        @SerializedName("saleCount")
        public int saleCount;
        @SerializedName("retailPrice")
        public long retailPrice;
        @SerializedName("marketPrice")
        public long marketPrice;
        @SerializedName("thumbUrl")
        public String thumbUrl;
        @SerializedName("maxPrice")
        public long maxPrice;
        @SerializedName("minPrice")
        public long minPrice;
        @SerializedName("tags")
        public List<Tag> tags;
        @SerializedName("spec")
        public String spec;
        @SerializedName("currentVipTypePrice")
        public long currentVipTypePrice;
    }

    public static class ApiSkuVipTypePriceBeansEntity {
        /**
         * vipType : 4
         * vipTypeStr : 钻石会员
         * price : 0
         */

        @SerializedName("vipType")
        public int vipType;
        @SerializedName("vipTypeStr")
        public String vipTypeStr;
        @SerializedName("price")
        public long price;
    }

    public long getVipPrice(int vipType){
        for (ApiSkuVipTypePriceBeansEntity entity : apiSkuVipTypePriceBeans) {
            if (entity.vipType == vipType) {
                return entity.price;
            }
        }
        return 0;
    }
}
