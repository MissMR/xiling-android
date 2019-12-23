package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-06-06
 */
public class Page implements Serializable {

    @SerializedName("pageId")
    public String id;
    @SerializedName("pageName")
    public String name;

    public Page() {
    }

    public Page(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
