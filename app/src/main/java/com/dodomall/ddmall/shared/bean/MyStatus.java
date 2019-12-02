package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/5.
 */
public class MyStatus {

    /**
     * couponCount : 1
     * messageCount : 0
     * bindWechatStatus : 0
     * bindBankStatus : 0
     * bindWechatStatusStr : 未绑定
     * bindBankStatusStr : 待审核
     */

    @SerializedName("couponCount")
    public int couponCount;
    @SerializedName("messageCount")
    public int messageCount;
    @SerializedName("bindWechatStatus")
    public int bindWechatStatus;
    @SerializedName("bindBankStatus")
    public int bindBankStatus;
    @SerializedName("bindWechatStatusStr")
    public String bindWechatStatusStr;
    @SerializedName("bindBankStatusStr")
    public String bindBankStatusStr;
}
