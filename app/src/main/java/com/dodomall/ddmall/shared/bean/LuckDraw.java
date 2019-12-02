package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/10/12.
 */
public class LuckDraw {

    /**
     * productType : 1
     * pname : 6
     * prizeImg : 图片
     * index : 5
     */

    @SerializedName("productType")
    public int productType;
    @SerializedName("pname")
    public String pname;
    @SerializedName("prizeImg")
    public String prizeImg;
    @SerializedName("index")
    public int index;
}
