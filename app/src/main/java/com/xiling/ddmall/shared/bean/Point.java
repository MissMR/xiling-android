package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-08-03
 */
public class Point implements Serializable {

    @SerializedName("memberId")
    public String memberId;
    @SerializedName("totalScore")
    public int totalScore;
    @SerializedName("score")
    public int score;
    @SerializedName("type")
    public int type;
    @SerializedName("typeStr")
    public String typeStr;
    @SerializedName("createDate")
    public String createDate;
}
