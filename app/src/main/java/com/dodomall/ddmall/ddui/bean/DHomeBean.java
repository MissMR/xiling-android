package com.dodomall.ddmall.ddui.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DHomeBean implements Serializable {

    private ArrayList<DHomeCategoryBean> category = new ArrayList<>();

    private DDHomeResBean homeRes;
    private ArrayList<DDHomeBanner> banner = new ArrayList<>();

    public ArrayList<DHomeCategoryBean> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<DHomeCategoryBean> category) {
        this.category = category;
    }

    public DDHomeResBean getHomeRes() {
        return homeRes;
    }

    public void setHomeRes(DDHomeResBean homeRes) {
        this.homeRes = homeRes;
    }

    public ArrayList<DDHomeBanner> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<DDHomeBanner> banner) {
        this.banner = banner;
    }
}
