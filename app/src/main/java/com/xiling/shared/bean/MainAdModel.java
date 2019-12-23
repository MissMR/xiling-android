package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/28.
 */
public class MainAdModel {

    /**
     * backUrl : http://flyimg.kangerys.com/G1/M00/00/74/eEwZu1k6V66ALVGgAAAqcwKfL4Y099.jpg
     * target : /creategory/
     * event : native
     */

    @SerializedName("backUrl")
    public String backUrl;
    @SerializedName("target")
    public String target;
    @SerializedName("event")
    public String event;
}
