package com.xiling.ddmall.module.auth.model;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/5.
 */
public class CardDetailModel {

    /**
     * accountId : 0da97261b1cc40afb62078123538474c
     * phone : 18520126131
     * accountType : 1
     * bankId : 1
     * bankAccount : 12345678901234567890
     * bankUser : 余建帆111
     * alipayAccount :
     * alipayUser :
     * wechatAccount :
     * wechatUser :
     * bankName : 中国建设银行
     * bankLogo : http://img.kangerys.com/G1/M00/00/0A/CqxOrVg9NgSAZb4NAAATXJD4WcM140.png
     * accountStatus : 0
     * idcardFrontImg : 身份证正面1
     * idcardBackImg : 身份证反面1
     * idcardHeadImg : 手持身份证1
     * bankcardFrontImg : 银行卡正面1
     * bankcardProvince : 省1
     * bankcardCity : 市1
     * bankcardArea : 区1
     * bankcardAddress : 支行地址1
     * bankcardCode : 020
     * checkDate :
     * checkResult :
     * remark :
     */

    @SerializedName("accountId")
    public String accountId;
    @SerializedName("phone")
    public String phone;
    @SerializedName("accountType")
    public int accountType;
    @SerializedName("bankId")
    public String bankId;
    @SerializedName("bankAccount")
    public String bankAccount;
    @SerializedName("bankUser")
    public String bankUser;
    @SerializedName("alipayAccount")
    public String alipayAccount;
    @SerializedName("alipayUser")
    public String alipayUser;
    @SerializedName("wechatAccount")
    public String wechatAccount;
    @SerializedName("wechatUser")
    public String wechatUser;
    @SerializedName("bankName")
    public String bankName;
    @SerializedName("bankLogo")
    public String bankLogo;
    @SerializedName("accountStatus")
    public int accountStatus;
    @SerializedName("idcardFrontImg")
    public String idcardFrontImg;
    @SerializedName("idcardBackImg")
    public String idcardBackImg;
    @SerializedName("idcardHeadImg")
    public String idcardHeadImg;
    @SerializedName("bankcardFrontImg")
    public String bankcardFrontImg;
    @SerializedName("bankcardProvince")
    public String bankcardProvince;
    @SerializedName("bankcardCity")
    public String bankcardCity;
    @SerializedName("bankcardArea")
    public String bankcardArea;
    @SerializedName("bankcardAddress")
    public String bankcardAddress;
    @SerializedName("bankcardCode")
    public String bankcardCode;
    @SerializedName("checkDate")
    public String checkDate;
    @SerializedName("checkResult")
    public String checkResult;
    @SerializedName("remark")
    public String remark;
}
