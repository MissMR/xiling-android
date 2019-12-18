package com.xiling.ddmall.shared.bean;

import android.text.TextUtils;

import com.xiling.ddmall.dduis.bean.DDCartFlashSaleBean;
import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.constant.AppTypes;

import java.util.ArrayList;
import java.util.List;

public class CartItem {

    @SerializedName("skuId")
    public String skuId;
    @SerializedName("skuName")
    public String name;
    @SerializedName("stock")
    public int stock;
    @SerializedName("retailPrice")
    public long retailPrice;
    @SerializedName("marketPrice")
    public long marketPrice;
    @SerializedName("quantity")
    public int amount;
    @SerializedName("hasCoupon")
    public int hasCoupon;
    @SerializedName("thumbUrl")
    public String thumb;
    @SerializedName("properties")
    public String properties;
    @SerializedName("presents")
    public List<CartItem> presents = new ArrayList<>();
    public boolean isSelected = false;

    @SerializedName("status")
    public int status;

    // 限时抢购活动Id
    @SerializedName("flashSaleId")
    public String flashSaleId;

    @SerializedName("flashSaleSkuDTO")
    public DDCartFlashSaleBean flashSaleSkuDTO;

    public boolean isRush() {  //判断是否是抢购
        if (!TextUtils.isEmpty(flashSaleId) && flashSaleSkuDTO != null) {
            return true;
        }
        return false;
    }


    /**
     * 是否可购买
     */
    public boolean isPurchasable() {
        //判断是否是抢购
        if (!TextUtils.isEmpty(flashSaleId) && flashSaleSkuDTO != null) {
            int rushStatus = flashSaleSkuDTO.getFlashSaleStatusValue();
            long rushStock = flashSaleSkuDTO.getInventory();
            //抢购中且库存大于1的时候允许结算
            if (rushStatus == 3 && rushStock > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            //判断商品库存
            return (this.stock > 0 && this.status != 0);
        }
    }

    /**
     * discountStatus : 0
     */

    @SerializedName("discountStatus")
    public int discountStatus;
    /**
     * vipTypePrices : [{"vipType":4,"price":12000},{"vipType":3,"price":14000},{"vipType":2,"price":16000},{"vipType":1,"price":18000},{"vipType":0,"price":20000}]
     */
    @SerializedName("vipTypePrices")
    public List<VipTypePricesEntity> vipTypePrices;
    /**
     * currentVipTypePrice : 22000
     */

    @SerializedName("currentVipTypePrice")
    public long currentVipTypePrice;
    /**
     * buyScore : 1
     */
    @SerializedName("buyScore")
    public int buyScore;
    @SerializedName("productType")
    public int productType;
    /**
     * productId : 71b97ea19ea542a59afacbce49cbf256
     */

    @SerializedName("productId")
    public String productId;

    /**
     * 0、默认，1跨境购产品
     */
    @SerializedName("extType")
    public int extType;
    /**
     * 1:跨境购
     */
    public int isCross;
    public long tempPrice;

    public static class VipTypePricesEntity {
        /**
         * vipType : 4
         * price : 12000
         */
        @SerializedName("vipType")
        public int vipType;
        @SerializedName("price")
        public long price;
        @SerializedName("vipTypeStr")
        public String vipTypeStr;
    }

    /**
     * 计算积分时用到的价格
     */
    public long scorePrice;

    public long getTypePrice(int vipType) {
        long price = retailPrice;
        if (vipType != AppTypes.FAMILY.MEMBER_NORMAL && vipTypePrices != null) {
            for (VipTypePricesEntity vipTypePrice : vipTypePrices) {
                if (vipTypePrice.vipType == AppTypes.FAMILY.MEMBER_ZUNXIANG) {
                    price = vipTypePrice.price;
                }
            }
        }
        return price;
    }

    /**
     * @return 会员折扣的钱(需要自己判断当前会员等级不是普通会员)
     */
    public long getVipRadioMoney() {
        return retailPrice - getTypePrice(AppTypes.FAMILY.MEMBER_ZUNXIANG);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CartItem) {
            return ((CartItem) obj).skuId.equals(skuId);
        }
        return obj instanceof SkuInfo && ((SkuInfo) obj).skuId.equals(skuId);
    }
}
