package com.xiling.ddmall.ddui.bean;

/**
 * created by Jigsaw at 2018/10/12
 */
public class EconomicHomeBanner {
    /**
     * banner : http://oss.dianduoduo.store/banner/2018-10/20181009162002.jpg
     * actionType : 1
     * linkUrl : https://store.dodomall.com/app_web/beshopkepper
     */

    private String banner;
    private int actionType;
    private String linkUrl;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
