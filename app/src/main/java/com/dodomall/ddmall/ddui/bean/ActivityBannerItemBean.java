package com.dodomall.ddmall.ddui.bean;

import java.io.Serializable;

public class ActivityBannerItemBean implements Serializable {

    private String activityEventType = "";
    private String activityPicUrl = "";
    private String activityActionUrl = "";

    public String getActivityEventType() {
        return activityEventType;
    }

    public void setActivityEventType(String activityEventType) {
        this.activityEventType = activityEventType;
    }

    public String getActivityPicUrl() {
        return activityPicUrl;
    }

    public void setActivityPicUrl(String activityPicUrl) {
        this.activityPicUrl = activityPicUrl;
    }

    public String getActivityActionUrl() {
        return activityActionUrl;
    }

    public void setActivityActionUrl(String activityActionUrl) {
        this.activityActionUrl = activityActionUrl;
    }

    @Override
    public String toString() {
        return this.activityPicUrl;
    }
}
