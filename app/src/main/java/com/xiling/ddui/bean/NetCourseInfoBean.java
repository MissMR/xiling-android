package com.xiling.ddui.bean;

import java.io.Serializable;

public class NetCourseInfoBean implements Serializable {

    /***
     * 是否喜欢
     */
    private boolean beLike = false;

    /**
     * H5链接地址
     */
    private String h5Url = "";

    /**
     * 是否可分享
     * <p>
     * 1:可分享
     * 2:不可分享
     */
    private int share = 2;

    public boolean isBeLike() {
        return beLike;
    }

    public void setBeLike(boolean beLike) {
        this.beLike = beLike;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }
}
