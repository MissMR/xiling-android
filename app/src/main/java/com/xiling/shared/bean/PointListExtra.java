package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-08-03
 */
public class PointListExtra implements Serializable {
    @SerializedName("memberId")
    public String memberId;
    @SerializedName("totalScore")
    public int totalScore;

    /**
     * turnInScore : 0
     * turnOutScore : 0
     * sumGetScore : 0
     * sumUseScore : 0
     */

    @SerializedName("turnInScore")
    public int turnInScore;
    @SerializedName("turnOutScore")
    public int turnOutScore;
    @SerializedName("sumGetScore")
    public int sumGetScore;
    @SerializedName("sumUseScore")
    public int sumUseScore;
}
