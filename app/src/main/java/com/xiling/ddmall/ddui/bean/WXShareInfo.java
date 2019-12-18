package com.xiling.ddmall.ddui.bean;

import java.io.Serializable;

public class WXShareInfo implements Serializable {

    public class EventShareValue {
        //默认活动文案
        public static final int DEFAULT = 1;
        //0元购
        public static final int FREE_BUY = 2;
    }

    public class ShareValue {
        //不显示
        public static final int NONE = 0;
        //使用默认文案分享
        public static final int DEFAULT = 1;
        //每日上新
        public static final int DALIY = 2;
    }

    public WXShareInfo() {

    }

    public WXShareInfo(int shareType, String imgUrl, String title, String desc) {
        setShareType(shareType);
        setImgUrl(imgUrl);
        setTitle(title);
        setDesc(desc);
    }


    public WXShareInfo(int shareType, String imgUrl, String title, String desc, String shareUrl) {
        setShareType(shareType);
        setImgUrl(imgUrl);
        setTitle(title);
        setDesc(desc);
        setShareUrl(shareUrl);
    }

    private int shareType = 0;
    private String imgUrl = "";
    private String title = "";
    private String desc = "";
    private String shareUrl = "";

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
