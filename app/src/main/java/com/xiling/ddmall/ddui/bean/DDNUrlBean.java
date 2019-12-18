package com.xiling.ddmall.ddui.bean;

import com.xiling.ddmall.ddui.tools.URLFormatUtils;

import java.io.Serializable;

public class DDNUrlBean implements Serializable {

    private String shortUrl = "";
    private String oUrl = "";

    public String getDDShortURL() {
        return URLFormatUtils.getDDShortUrl(this.shortUrl);
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getoUrl() {
        return oUrl;
    }

    public void setoUrl(String oUrl) {
        this.oUrl = oUrl;
    }
}
