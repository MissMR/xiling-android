package com.xiling.ddmall.shared.bean.body;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/27.
 */
public class ReceiveRefundGoodsBody {

    /**
     * orderCode : 2381503267340850
     * sellerRemark : 退货收到了，损坏严重，只能退你十块钱，不服看照片
     * refundMoney : 1000
     * evidenceImages : ["第一件损坏商品的照片","第二件损坏商品的照片"]
     */

    @SerializedName("orderCode")
    public String orderCode;
    @SerializedName("sellerRemark")
    public String sellerRemark;
    @SerializedName("refundMoney")
    public long refundMoney;
    @SerializedName("evidenceImages")
    public List<String> evidenceImages;

    public ReceiveRefundGoodsBody(String orderCode, String sellerRemark, long refundMoney, List<String> evidenceImages) {
        this.orderCode = orderCode;
        this.sellerRemark = sellerRemark;
        this.refundMoney = refundMoney;
        this.evidenceImages = evidenceImages;
    }
}
