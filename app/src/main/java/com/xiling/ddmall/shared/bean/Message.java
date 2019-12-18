package com.xiling.ddmall.shared.bean;

import android.text.TextUtils;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Jigsaw
 * @date 2019/1/9
 */
public class Message implements Serializable {

    @SerializedName("msgId")
    public String id;
    @SerializedName("businessType")
    public int businessType;
    @SerializedName("businessCode")
    public String businessCode;
    @SerializedName("thumbUrl")
    public String thumb;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("createDate")
    public String createDate;

    public boolean isImageMessage() {
        return !TextUtils.isEmpty(thumb);
    }

    public boolean hasDetail() {
        // 注册成功 和 团队成员新增没有跳转 -1不跳转的消息
        return businessType != 1001 && businessType != 1007 && businessType != -1;
    }

    public String getContent() {
        if (Strings.isNullOrEmpty(content)) {
            return "";
        }
        return content.replaceAll("\\n(\\s)*", "\n");
    }
}
