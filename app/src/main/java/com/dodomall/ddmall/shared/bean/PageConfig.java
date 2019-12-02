package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;
import com.dodomall.ddmall.shared.page.bean.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-06-07
 */
public class PageConfig {

    @SerializedName("title")
    public String title;
    @SerializedName("config")
    public List<Element> elements = new ArrayList<>();

}
