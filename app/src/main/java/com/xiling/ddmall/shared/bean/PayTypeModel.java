package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/1/23.
 */
public class PayTypeModel {
    /**
     * appType : 1
     * payType : 1
     * payTypeStr : 微信支付
     * iconUrl : http://img.beautysecret.cn/G1/M00/07/D5/rBIC71pUZmSAd-KjAAANlm8Mcus576.png
     */

    @SerializedName("appType")
    public int appType;
    @SerializedName("payType")
    public int payType;
    @SerializedName("payTypeStr")
    public String payTypeStr;
    @SerializedName("iconUrl")
    public String iconUrl;

    public PayTypeModel(int appType, int payType, String payTypeStr, String iconUrl) {
        this.appType = appType;
        this.payType = payType;
        this.payTypeStr = payTypeStr;
        this.iconUrl = iconUrl;
    }
}
