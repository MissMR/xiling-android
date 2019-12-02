package com.dodomall.ddmall.dduis.bean;

import com.dodomall.ddmall.ddui.config.UIConfig;

public class DDHomeStyleBean {

    public static DDHomeStyleBean defaultStyle() {
        DDHomeStyleBean style = new DDHomeStyleBean();
        style.setBackgroundImg("");

        style.setCategoryImg("");
        style.setMessageImg("");

        style.setLabelDefaultColour("#000000");
        style.setLabelSelectionColour(UIConfig.kMainColor);

        style.setSearchBackgroundColour("#f2f2f2");
        style.setSearchFrameColour("#f2f2f2");
        style.setSearchTextColour("#999999");
        return style;
    }

    private String labelDefaultColour = "";//文本颜色
    private String labelSelectionColour = "";//文本选中颜色
    private String searchBackgroundColour = "";//搜索框颜色
    private String searchTextColour = "";//搜索文本颜色
    private String searchFrameColour = "";//搜索
    private String backgroundImg = "";//背景
    private String categoryImg = "";//分类按钮资源
    private String messageImg = "";//消息按钮资源

    public String getLabelDefaultColour() {
        return labelDefaultColour;
    }

    public void setLabelDefaultColour(String labelDefaultColour) {
        this.labelDefaultColour = labelDefaultColour;
    }

    public String getLabelSelectionColour() {
        return labelSelectionColour;
    }

    public void setLabelSelectionColour(String labelSelectionColour) {
        this.labelSelectionColour = labelSelectionColour;
    }

    public String getSearchBackgroundColour() {
        return searchBackgroundColour;
    }

    public void setSearchBackgroundColour(String searchBackgroundColour) {
        this.searchBackgroundColour = searchBackgroundColour;
    }

    public String getSearchTextColour() {
        return searchTextColour;
    }

    public void setSearchTextColour(String searchTextColour) {
        this.searchTextColour = searchTextColour;
    }

    public String getSearchFrameColour() {
        return searchFrameColour;
    }

    public void setSearchFrameColour(String searchFrameColour) {
        this.searchFrameColour = searchFrameColour;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

    public String getMessageImg() {
        return messageImg;
    }

    public void setMessageImg(String messageImg) {
        this.messageImg = messageImg;
    }
}
