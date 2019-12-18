package com.xiling.ddmall.module.auth;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/6/8.
 */
public class AuthModel {

    /**
     * memberId : a77b5569329f4c15a0169485c2c046b0
     * authStatus : 1
     * userName : 余建帆
     * identityCard : 441523199211236597
     * idcardFrontImg : 身份证正面
     * idcardBackImg : 身份证反面
     * idcardHeadImg : 手持身份证
     * authRemark : 申请备注
     * checkRemark :
     */

    @SerializedName("memberId")
    public String memberId;
    @SerializedName("authStatus")
    public int authStatus;
    @SerializedName("userName")
    public String userName;
    @SerializedName("identityCard")
    public String identityCard;
    @SerializedName("idcardFrontImg")
    public String idcardFrontImg;
    @SerializedName("idcardBackImg")
    public String idcardBackImg;
    @SerializedName("idcardHeadImg")
    public String idcardHeadImg;
    @SerializedName("authRemark")
    public String authRemark;
    @SerializedName("checkRemark")
    public String checkRemark;

    public String getMemberId() {
        return memberId;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public String getUserName() {
        return userName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public String getIdcardFrontImg() {
        return idcardFrontImg;
    }

    public String getIdcardBackImg() {
        return idcardBackImg;
    }

    public String getIdcardHeadImg() {
        return idcardHeadImg;
    }

    public String getAuthRemark() {
        return authRemark;
    }

    public String getCheckRemark() {
        return checkRemark;
    }
}
