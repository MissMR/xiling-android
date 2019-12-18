package com.xiling.ddmall.ddui.bean;

import java.io.Serializable;

public class DDSkuListBean implements Serializable {

    private DDSkuPageBean page = null;
    private int vipFlag;

    public DDSkuPageBean getPage() {
        return page;
    }

    public void setPage(DDSkuPageBean page) {
        this.page = page;
    }

    public int getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(int vipFlag) {
        this.vipFlag = vipFlag;
    }
}
