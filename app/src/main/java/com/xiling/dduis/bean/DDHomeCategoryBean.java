package com.xiling.dduis.bean;

import com.xiling.ddui.bean.DDHomeBanner;

import java.io.Serializable;
import java.util.ArrayList;

public class DDHomeCategoryBean implements Serializable {

    public static String RecommendId = "999999";
    public static String WebEvent = "link";

    public boolean isRecommend() {
        if (RecommendId.equals(categoryId)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isWebEvent() {
        if (WebEvent.equals(event)) {
            return true;
        } else {
            return false;
        }
    }

    private String categoryId = "";
    private String categoryName = "";
    ArrayList<DDHomeBanner> indexBannerBeanList = new ArrayList<>();

    private String title = "";
    private String event = "";
    private String target = "";

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<DDHomeBanner> getIndexBannerBeanList() {
        return indexBannerBeanList;
    }

    public void setIndexBannerBeanList(ArrayList<DDHomeBanner> indexBannerBeanList) {
        this.indexBannerBeanList = indexBannerBeanList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
