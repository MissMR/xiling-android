package com.xiling.module.community;


import android.text.TextUtils;

import com.blankj.utilcode.utils.StringUtils;

import java.io.Serializable;

public class Comment implements Serializable{
    public String commentId;
    public String topicId;
    public String memberId;
    public String nickName;
    public String headImage;
    public String content;
    public String reply;
    public String replyDate;
    public String createDate;
    public String updateDate;
    public int status;
    public String courseId;

    public boolean hasReply() {
        return StringUtils.isEmpty(reply);
    }

    public String getPostTime() {
        return DateUtils.getDateString(this.createDate);
    }

    public String getReplyTime() {
        if (TextUtils.isEmpty(replyDate)) {
            return "";
        }

        return DateUtils.getDateString(replyDate);
    }
}
