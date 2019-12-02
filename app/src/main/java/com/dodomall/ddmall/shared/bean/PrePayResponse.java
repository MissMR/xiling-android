package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-06
 */
public class PrePayResponse implements Serializable {

    @SerializedName("return_code")
    public String code;
    @SerializedName("return_msg")
    public String message;
    @SerializedName("result_code")
    public String resultCode;
    @SerializedName("appid")
    public String appId;
    @SerializedName("mch_id")
    public String mchId;
    @SerializedName("nonce_str")
    public String nonceStr;
    @SerializedName("openid")
    public String openId;
    @SerializedName("sign")
    public String sign;
    @SerializedName("prepay_id")
    public String prePayId;
    @SerializedName("trade_type")
    public String tradeType;

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(code);
    }
}
