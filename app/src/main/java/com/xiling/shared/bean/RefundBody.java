package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/24.
 */
public class RefundBody {

    public RefundBody(String refundCode, String orderCode, String sellerRemark, int refundMoney) {
        this.refundCode = refundCode;
        this.orderCode = orderCode;
        this.sellerRemark = sellerRemark;
        this.refundMoney = refundMoney;
    }

    /**
     * refundCode : 1241511471552036
     * orderCode : 1241506555390654
     * sellerRemark : aaaaa
     * refundMoney : 0
     */

    @SerializedName("refundCode")
    public String refundCode;
    @SerializedName("orderCode")
    public String orderCode;
    @SerializedName("sellerRemark")
    public String sellerRemark;
    @SerializedName("refundMoney")
    public int refundMoney;
}
