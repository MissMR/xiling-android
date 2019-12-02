package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chan on 2017/6/16.
 */

public class CommonExtra implements Serializable{

    @SerializedName("profitSumMoney")
    public long profitSumMoney;
    @SerializedName("freezeSumMoney")
    public long freezeSumMoney;
    @SerializedName("availableMoney")
    public long availableMoney;
}
