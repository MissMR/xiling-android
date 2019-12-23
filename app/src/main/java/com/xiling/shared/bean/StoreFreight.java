package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/23.
 */
public class StoreFreight {

    /**
     * storeId : 00187ba5cb144066b26cf3fafad93a8b
     * provinceId : 110000
     * provinceName : 北京
     * initFreight : 900
     * addFreight : 500
     */

    @SerializedName("storeId")
    public String storeId;
    @SerializedName("provinceId")
    public String provinceId;
    @SerializedName("provinceName")
    public String provinceName;
    @SerializedName("initFreight")
    public int initFreight;
    @SerializedName("addFreight")
    public int addFreight;
}
