package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/18.
 */
public class GetOrderStatusCount {

    /**
     * waitShip : 0
     * hasShip : 0
     * hasComplete : 0
     * hasClose : 0
     * afterSales : 0
     * comment : 0
     */

    @SerializedName("waitShip")
    public int waitShip;
    @SerializedName("hasShip")
    public int hasShip;
    @SerializedName("hasComplete")
    public int hasComplete;
    @SerializedName("hasClose")
    public int hasClose;
    @SerializedName("afterSales")
    public int afterSales;
    @SerializedName("comment")
    public int comment;
}
