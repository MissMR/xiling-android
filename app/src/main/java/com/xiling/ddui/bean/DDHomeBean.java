package com.xiling.ddui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DDHomeBean implements Serializable {

    private DDHomeResBean homeRes;
    private ArrayList<DDHomeBanner> banner;
    private HomePageBean page;
    private List<HomeBuyOrShare> buyOrShare;

    public List<HomeBuyOrShare> getBuyOrShare() {
        return buyOrShare;
    }

    public void setBuyOrShare(List<HomeBuyOrShare> buyOrShare) {
        this.buyOrShare = buyOrShare;
    }

    public HomePageBean getPage() {
        return page;
    }

    public void setPage(HomePageBean page) {
        this.page = page;
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

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        buffer.append("homeRes:" + homeRes + "\n");
        buffer.append("banner:size:" + banner.size() + "\n");
        for (DDHomeBanner banner : banner) {
            buffer.append("\t -" + banner.toString() + "\n");
        }
        return buffer.toString();
    }
}
