package com.xiling.ddui.bean;

import com.xiling.ddui.activity.InfoDetailActivity;

import java.io.Serializable;

public class UIEvent implements Serializable {

    public enum Type {
        /**
         * 关闭所有的界面
         */
        CloseAllActivity,
        /**
         * 关闭安全问题相关的所有界面
         */
        CloseQuestionActivity,
        /**
         * 关闭交易密码相关的所有界面
         */
        ClosePasswordActivity,
        /**
         * 关闭绑定银行卡相关的界面
         */
        CloseBindCardActivity,

        /**
         * 增加阅读量
         */
        AddReadCount,

        /*喜欢 - 商品 - 状态变更*/
        UnLikeProduct,
        /*喜欢 - 课程 - 状态变更*/
        UnLikeLearn,
        RefreshWithdraw
    }

    private Type type;
    private String infoId;
    private InfoDetailActivity.InfoType infoType;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public InfoDetailActivity.InfoType getInfoType() {
        return infoType;
    }

    public void setInfoType(InfoDetailActivity.InfoType infoType) {
        this.infoType = infoType;
    }
}
