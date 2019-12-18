package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/10/12.
 */
public class LotteryActivityModel {
    /**
     * activityId : 4555555555555
     * aname : 幸运转盘
     * rule : 规则：转一次消耗一次抽奖机会
     * num : 1
     * turnImg : 大转盘图片
     */

    @SerializedName("activityId")
    public String activityId;
    @SerializedName("aname")
    public String aname;
    @SerializedName("rule")
    public String rule;
    @SerializedName("num")
    public int num;
    @SerializedName("turnImg")
    public String turnImg;
}
