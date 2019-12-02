package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/10/12.
 */
public class ScoreModel {

    /**
     * memberId : 87910ed054494bfbb468555e6a54b5ef
     * totalScore : 44361
     * turnInScore : 200
     * turnOutScore : 0
     * sumGetScore : 2
     * sumUseScore : 6777
     * availableNum : 1000000
     */

    @SerializedName("memberId")
    public String memberId;
    @SerializedName("totalScore")
    public long totalScore;
    @SerializedName("turnInScore")
    public long turnInScore;
    @SerializedName("turnOutScore")
    public long turnOutScore;
    @SerializedName("sumGetScore")
    public long sumGetScore;
    @SerializedName("sumUseScore")
    public long sumUseScore;
    @SerializedName("availableNum")
    public long availableNum;
}
