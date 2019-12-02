package com.dodomall.ddmall.module.user.model;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/7.
 */
public class UpMemberModel {

    /**
     * headImage : http://flyimg.kangerys.com/G1/M00/00/17/eEwZu1jt18aAetZAAAC6cnelZKU782.jpg
     * phone : 13422221111
     * nickName : wulin
     */

    @SerializedName("headImage")
    public String headImage;
    @SerializedName("phone")
    public String phone;
    @SerializedName("nickName")
    public String nickName;
}
