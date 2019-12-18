package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan on 2017/6/16.
 */

public class Profit {
    @SerializedName("freezeId")
    public String freezeId;
    @SerializedName("typeStr")
    public String typeStr;
    @SerializedName("nickName")
    public String nickName;
    @SerializedName("freezeProfitMoney")
    public long freezeProfitMoney;
    @SerializedName("freezeSumMoney")
    public long freezeSumMoney;
    @SerializedName("orderMoney")
    public long orderMoney;
    @SerializedName("createDate")
    public String createDate;

}
