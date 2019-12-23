package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/1/15.
 */
public class WxPayModel {

    /**
     * sign : 320F46E917803D1290B73D285F06354B
     * return_code : SUCCESS
     * trade_type : APP
     * result_code : SUCCESS
     * appid : wxfc89ed67e82584ba
     * mch_id : 1496571592
     * nonce_str : rjxwG8iyxyCmlKAS
     * prepay_id : wx2018011517254511e661ed110904641495
     * return_msg : OK
     */

    @SerializedName("sign")
    public String sign;
    @SerializedName("return_code")
    public String returnCode;
    @SerializedName("trade_type")
    public String tradeType;
    @SerializedName("result_code")
    public String resultCode;
    @SerializedName("appid")
    public String appid;
    @SerializedName("mch_id")
    public String mchId;
    @SerializedName("nonce_str")
    public String nonceStr;
    @SerializedName("prepay_id")
    public String prepayId;
    @SerializedName("return_msg")
    public String returnMsg;

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(returnCode);
    }
}
