package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/22.
 */
public class GetHiCardPay {
    /**
     * version : V001
     * organNo : 44014266
     * hicardMerchNo : 104401824100001
     * payType : 009
     * merchOrderNo : 24315036283911221503401014
     * hicardOrderNo : 4161708220088506
     * isHtml :
     * html :
     * qrURL : https://qr.alipay.com/bax01573qg4alhfktthm6064
     * showPage : 0
     * amount : 601
     * createTime :
     * payInfo : {}
     * respCode : 00
     * respMsg : 成功
     * sign : 74d0ed9a33d74cea5c941ef6dfb6655f
     */

    @SerializedName("version")
    public String version;
    @SerializedName("organNo")
    public String organNo;
    @SerializedName("hicardMerchNo")
    public String hicardMerchNo;
    @SerializedName("payType")
    public String payType;
    @SerializedName("merchOrderNo")
    public String merchOrderNo;
    @SerializedName("hicardOrderNo")
    public String hicardOrderNo;
    @SerializedName("isHtml")
    public String isHtml;
    @SerializedName("html")
    public String html;
    @SerializedName("qrURL")
    public String qrURL;
    @SerializedName("showPage")
    public String showPage;
    @SerializedName("amount")
    public String amount;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("respCode")
    public String respCode;
    @SerializedName("respMsg")
    public String respMsg;
    @SerializedName("sign")
    public String sign;

    /**
     * mwebUrl : https://gzwkzf.cmbc.com.cn/payment-gate-web/wxH5Pay?url=https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx2017091317493811bf02f6840618964738&package=3664530438
     */

    @SerializedName("mwebUrl")
    public String mwebUrl;
}
