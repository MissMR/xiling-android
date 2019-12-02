package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

public class ProfitData {
    @SerializedName("profitSumMoney")
    public long profitSumMoney;
    @SerializedName("availableMoney")
    public long availableMoney;
    @SerializedName("freezeSumMoney")
    public long freezeSumMoney;

}
