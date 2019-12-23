package com.xiling.shared.bean;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-15
 */
public class OrderProduct implements Serializable {

    @SerializedName("storeId")
    public String storeId;
    @SerializedName("productImage")
    public String thumb;
    @SerializedName("skuId")
    public String skuId;
    @SerializedName("productId")
    public String spuId;
    @SerializedName("order1Id")
    public String order1Id;
    @SerializedName("skuName")
    public String skuName;
    @SerializedName("quantity")
    public int quantity;
    /**
     * 会员购买等级购买价
     */
    @SerializedName("price")
    public long price;
    @SerializedName("costPrice")
    public long costPrice;
    /**
     * 实收款小计
     */
    @SerializedName("lineTotal")
    public long lineTotal;
    @SerializedName("properties")
    public String properties;
    @SerializedName("productType")
    public int productType;
    @SerializedName("productName")
    public String name;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("sobotId")
    public String sobotId;
    @SerializedName("presents")
    public List<OrderProduct> presents;
    /**
     * 零售价（最低会员等级价）
     * retailPrice : 18800
     */
    @SerializedName("retailPrice")
    public long retailPrice;
    /**
     * 实际成交价
     */
    @SerializedName("realPrice")
    public long realPrice;
    /**
     * 最多退款金额
     */
    @SerializedName("realTotal")
    public long realtotal;
    /**
     * normal(0, ""),
     * ReturnMoneying(5, "退款中"),//退款中
     * ReturnGooding(6, "退货中"),//退货中
     * ReturnMoneyClose(7, "退款成功"),
     * ReturnGoodClose(8, "退款成功"),//退货完成
     * ReturnMoneyCancel(10,"退款取消"),
     * ReturnGoodCancel(11,"退货取消"),
     * ReturnMoneyReject(12,"退款拒绝"),
     * ReturnGoodReject(13,"退货拒绝");
     */
    @SerializedName("refundStatus")
    public int refundStatus;
    @SerializedName("refundStatusStr")
    public String refundStatusStr;
    /**
     * 市场价
     */
    @SerializedName("marketPrice")
    public long marketPrice;

    private boolean mSelect;

    @SuppressLint("DefaultLocale")
    public String getAmountString() {
        return String.format("×  %d", quantity);
    }

    @SerializedName("refundId")
    public String refundId;

    public void setSelect(boolean select) {
        mSelect = select;
    }

    public boolean isSelect() {
        return mSelect;
    }
}
