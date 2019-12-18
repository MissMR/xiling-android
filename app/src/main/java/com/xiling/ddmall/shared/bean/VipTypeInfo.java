package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/1/19.
 */
public class VipTypeInfo {

    /**
     * vipType : 0
     * vipTypeStr : 普通会员
     */

    @SerializedName("vipType")
    public int vipType;
    @SerializedName("vipTypeStr")
    public String vipTypeStr;

    public VipTypeInfo(int vipType, String vipTypeStr) {
        this.vipType = vipType;
        this.vipTypeStr = vipTypeStr;
    }
}
