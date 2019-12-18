package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCount implements Serializable {

    @SerializedName("waitPay")
    public int waitPay;

    @SerializedName("waitShip")
    public int waitShip;

    @SerializedName("hasShip")
    public int hasShip;

    @SerializedName("hasComplete")
    public int hasComplete;

    @SerializedName("afterSales")
    public int afterSales;

    @SerializedName("waitComment")
    public int waitComment;

}
