package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-08-03
 */
public class InvitationCode implements Serializable {

    @SerializedName("memberId")
    public String memberId;
    @SerializedName("inviteCode")
    public String inviteCode;
    @SerializedName("imgUrl")
    public String imgUrl;
}
