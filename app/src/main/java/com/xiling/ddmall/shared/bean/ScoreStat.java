package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.constant.AppTypes;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/15.
 */
public class ScoreStat {

    /**
     * memberScore : {"memberId":"c7550b22eb034a229ca859062fa30dfe","totalScore":1720,"turnInScore":0,"turnOutScore":0,"sumGetScore":2000,"sumUseScore":280}
     * scoreUseCofig : {"ratio":0,"minOrderMoney":0}
     * memberRatio : {"memberType":3,"joinPrice":99900,"ratio":70}
     */

    @SerializedName("memberScore")
    public MemberScoreEntity memberScore;
    @SerializedName("scoreUseCofig")
    public ScoreUseCofigEntity scoreUseCofig;
    @SerializedName("memberRatio")
    public List<MemberRatio> memberRatio;

    public static class MemberScoreEntity {
        /**
         * memberId : c7550b22eb034a229ca859062fa30dfe
         * totalScore : 1720
         * turnInScore : 0
         * turnOutScore : 0
         * sumGetScore : 2000
         * sumUseScore : 280
         */

        @SerializedName("memberId")
        public String memberId;
        @SerializedName("totalScore")
        public int totalScore;
        @SerializedName("turnInScore")
        public int turnInScore;
        @SerializedName("turnOutScore")
        public int turnOutScore;
        @SerializedName("sumGetScore")
        public int sumGetScore;
        @SerializedName("sumUseScore")
        public int sumUseScore;
    }

    public static class ScoreUseCofigEntity {
        /**
         * ratio : 0
         * minOrderMoney : 0
         */

        @SerializedName("ratio")
        public int ratio;
        @SerializedName("minOrderMoney")
        public int minOrderMoney;
    }

    public long getZunXiangPrice(){
        long joinPrice = 99999;
        for (MemberRatio memberRatio : memberRatio) {
            if (memberRatio.memberType == AppTypes.FAMILY.MEMBER_ZUNXIANG) {
                joinPrice = memberRatio.joinPrice;
            }
        }
        return joinPrice;
    }
}
