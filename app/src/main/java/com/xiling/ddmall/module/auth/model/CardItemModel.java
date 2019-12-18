package com.xiling.ddmall.module.auth.model;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/5.
 */
public class CardItemModel {

    /**
     * bankId : 1
     * bankName : 中国建设银行
     * bankLogo : http://img.kangerys.com/G1/M00/00/0A/CqxOrVg9NgSAZb4NAAATXJD4WcM140.png
     */

    @SerializedName("bankId")
    public String bankId;
    @SerializedName("bankName")
    public String bankName;
    @SerializedName("bankLogo")
    public String bankLogo;

    public CardItemModel(String bankId, String bankName) {
        this.bankId = bankId;
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return bankName;
    }
}
