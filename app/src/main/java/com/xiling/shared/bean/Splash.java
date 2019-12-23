package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-08-03
 */
public class Splash implements Serializable {
    @SerializedName("backUrl")
    public String backUrl;
    @SerializedName("target")
    public String target;
    @SerializedName("event")
    public String event;
}
