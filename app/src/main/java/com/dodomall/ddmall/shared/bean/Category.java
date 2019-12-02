package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
@Deprecated
public class Category {
    @SerializedName("categoryId")
    public String id;
    @SerializedName("parentId")
    public String parentId;
    @SerializedName("iconUrl")
    public String icon;
    @SerializedName("categoryName")
    public String name;
    @SerializedName("remark")
    public String remark;
    @SerializedName("createDate")
    public Date createDate;
    public boolean isSelected = Boolean.FALSE;
}
