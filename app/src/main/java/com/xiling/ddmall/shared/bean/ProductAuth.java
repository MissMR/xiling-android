package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-10
 */
public class ProductAuth implements Serializable {

    @SerializedName("iconUrl")
    public String icon;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
}
