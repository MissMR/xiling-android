package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/3.
 */
public class GetSecondKillProductListModel {

    /**
     * pageOffset : 1
     * pageSize : 15
     * totalRecord : 2
     * totalPage : 1
     */

    @SerializedName("pageOffset")
    public int pageOffset;
    @SerializedName("pageSize")
    public int pageSize;
    @SerializedName("totalRecord")
    public int totalRecord;
    @SerializedName("totalPage")
    public int totalPage;
    @SerializedName("datas")
    public List<InstantData.Product> datas;

}
