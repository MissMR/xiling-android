package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/7/27.
 */
public class ServiceCity {

    /**
     * province : 广东省
     * city : 广州市
     * county : 天河区
     */

    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("county")
    public String county;
}
