package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/18.
 */
public class MemberAchievement {

    /**
     * totalRetailMoney : 4453750
     * monthTotalRetailMoney : 4453750
     * month : 201708
     */

    @SerializedName("totalRetailMoney")
    public int totalRetailMoney;
    @SerializedName("monthTotalRetailMoney")
    public int monthTotalRetailMoney;
    @SerializedName("month")
    public int month;
}
