package com.xiling.ddmall.ddui.bean;

import java.io.Serializable;

public class HomeShortcutBean implements Serializable {

    private String icon = "";
    private String title = "";
    private String url = "";
    private String categoryId = "";

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
