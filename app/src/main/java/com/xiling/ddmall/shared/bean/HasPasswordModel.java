package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/12/13.
 */
public class HasPasswordModel {
    /**
     * status : true
     * code : 1
     * msg : 有密码
     * data :
     */

    @SerializedName("status")
    public boolean status;
    @SerializedName("code")
    public String code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public String data;
}
