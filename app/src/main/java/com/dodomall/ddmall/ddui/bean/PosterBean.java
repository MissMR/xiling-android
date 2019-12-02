package com.dodomall.ddmall.ddui.bean;

import java.io.Serializable;

public class PosterBean implements Serializable {

    private String imgUrl = "";
    private String style = "";

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
