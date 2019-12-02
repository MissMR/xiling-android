package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-19
 */
public class OrderComment implements Serializable {

    @SerializedName("order1Id")
    public String order1Id;
    @SerializedName("orderId")
    public String orderId;
    @SerializedName("memberId")
    public String memberId;
    @SerializedName("orderCode")
    public String orderCode;
    @SerializedName("skuId")
    public String skuId;
    @SerializedName("productId")
    public String productId;
    @SerializedName("productImage")
    public String thumb;
    @SerializedName("skuName")
    public String name;
    @SerializedName("properties")
    public String properties;
    @SerializedName("price")
    public long price;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("lineTotal")
    public long lineTotal;
    @SerializedName("commentStatus")
    public int commentStatus;
    /**
     * 实际成交价
     */
    @SerializedName("realPrice")
    public long realPrice;
    /**
     * 市场价
     */
    @SerializedName("marketPrice")
    public long marketPrice;
}
