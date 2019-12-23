package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan on 2017/6/22.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017/6/22 上午10:20
 */

public class Account {

    @SerializedName("accountId")
    public String accountId;
    @SerializedName("phone")
    public String phone;
    @SerializedName("accountType")
    public long accountType;
    @SerializedName("bankId")
    public long bankId;
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
    @SerializedName("headImage")
    public String headImage;
    @SerializedName("bankName")
    public String bankName;
    @SerializedName("bankLogo")
    public String bankLogo;
    @SerializedName("accountStatus")
    public long accountStatus;
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
