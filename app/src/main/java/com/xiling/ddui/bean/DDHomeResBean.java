package com.xiling.ddui.bean;

import java.io.Serializable;

public class DDHomeResBean implements Serializable {

    private int version = 0;
    private String downloadUrl = "";

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "{version:" + version + ",downloadUrl:" + downloadUrl + "}";
    }
}
