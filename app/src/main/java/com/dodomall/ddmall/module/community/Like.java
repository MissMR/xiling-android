package com.dodomall.ddmall.module.community;


public class Like {
    public String likeId;
    public String memberId;
    public String topicId;
    public String headImage;
    public String createDate;
    public String updateDate;
    public int deleteFlag;
    public String nickName;

    public String getAvatar() {
        return  this.headImage;
    }
}
