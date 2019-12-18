package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/17.
 */
public class MemberRatio {

    /**
     * memberType : 3
     * joinPrice : 99900
     * ratio : 70
     */

    @SerializedName("memberType")
    public int memberType;
    @SerializedName("joinPrice")
    public long joinPrice;
    @SerializedName("ratio")
    public int ratio;
}
