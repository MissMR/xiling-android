package com.dodomall.ddmall.module.auth;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/24.
 */
public class SubmitAuthBody {

    /**
     * idcardFrontImg : 身份证正面
     * idcardBackImg : 身份证反面
     * idcardHeadImg : 手持身份证
     * userName : 余建帆
     * identityCard : 441523199211236597
     * authRemark : 申请备注
     */

    @SerializedName("idcardFrontImg")
    public String idcardFrontImg;
    @SerializedName("idcardBackImg")
    public String idcardBackImg;
    @SerializedName("idcardHeadImg")
    public String idcardHeadImg;
    @SerializedName("userName")
    public String userName;
    @SerializedName("identityCard")
    public String identityCard;
    @SerializedName("authRemark")
    public String authRemark;

    public SubmitAuthBody(String idcardFrontImg, String idcardBackImg, String idcardHeadImg, String userName, String identityCard, String authRemark) {
        this.idcardFrontImg = idcardFrontImg;
        this.idcardBackImg = idcardBackImg;
        this.idcardHeadImg = idcardHeadImg;
        this.userName = userName;
        this.identityCard = identityCard;
        this.authRemark = authRemark;
    }
}
