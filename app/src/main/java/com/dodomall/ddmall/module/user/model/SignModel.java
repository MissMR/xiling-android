package com.dodomall.ddmall.module.user.model;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/7.
 */
public class SignModel {

    /**
     * memberId : fb547e99b7e84c64b57f1037389d3e4f
     * totalScore : 10
     */

    @SerializedName("memberId")
    public String memberId;
    @SerializedName("totalScore")
    public int totalScore;
}
