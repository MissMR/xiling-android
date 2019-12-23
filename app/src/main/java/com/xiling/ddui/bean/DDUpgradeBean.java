package com.xiling.ddui.bean;

import java.io.Serializable;

public class DDUpgradeBean implements Serializable {

    /**
     * 版本号
     */
    private String version = "0";
    /**
     * 升级状态
     * <p>
     * 0 不需要升级
     * 1 有升级不强制
     * 2 有升级必须升级
     */
    private int upgradeStatus = 0;
    /**
     * 下载地址
     */
    private String upUrl = "";
    /**
     * 升级提示
     */
    private String msg = "无";

    @Override
    public String toString() {
        return "{ version:" + version + " ,upUrl:" + upUrl + " ,upgradeStatus:" + upgradeStatus + " ,msg:" + msg + " }";
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpUrl() {
        return upUrl;
    }

    public void setUpUrl(String upUrl) {
        this.upUrl = upUrl;
    }

    public int getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(int upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
