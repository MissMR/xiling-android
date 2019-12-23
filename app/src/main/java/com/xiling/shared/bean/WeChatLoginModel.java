package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/7/30.
 */
public class WeChatLoginModel implements Serializable {
    /**
     * registerStatus : 0
     * access_token : 12_GOk0hHu2_AaNGsKAY3VoqXif630zVNpEdKs_SfFdtVRqzvVc0E0BIilu-S0on7ELQds7qZm0gvdKLjUvWERZTcHy3kgIKzB8GP6qp24lMHY
     * openid : oNqMA1ePFYfsVt2vNpm8Jw4-9POI
     * unionid : oKKN61QTwIkfwQ-aZ8wSAzRRFUQk
     * nickName : test
     * headImage : http://thirdwx.qlogo.cn/mmopen/vi_32/vNh0OeYBx4J9XEUeicIWZ1icXbTHRbdflmLFEUPRcHugMz5Xn99pcm8YSqp0TOuM1uYH7L27mWBvwKzG7Djz3mibA/132
     */

    // 0未注册  1注册了  2注册了未绑定手机号
    @SerializedName("registerStatus")
    public int registerStatus;
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("openid")
    public String openid;
    @SerializedName("unionid")
    public String unionid;
    @SerializedName("nickName")
    public String nickName;
    @SerializedName("headImage")
    public String headImage;
}
