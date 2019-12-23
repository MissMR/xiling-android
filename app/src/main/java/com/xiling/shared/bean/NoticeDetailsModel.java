package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/20.
 */
public class NoticeDetailsModel {


    @SerializedName("notesId")
    public String notesId;
    @SerializedName("title")
    public String title;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("content")
    public String content;
}
