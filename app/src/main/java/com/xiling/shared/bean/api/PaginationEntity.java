package com.xiling.shared.bean.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 分页实体类
 * Created by JayChan on 2016/12/13.
 */
public class PaginationEntity<T, R> implements Serializable {

    @SerializedName("pageOffset")
    public int page;
    @SerializedName("pageSize")
    public int pageSize;
    @SerializedName("totalRecord")
    public int total;
    @SerializedName("totalPage")
    public int totalPage;
    @SerializedName("datas")
    public ArrayList<T> list;
    @SerializedName("ex")
    public R ex;
}
