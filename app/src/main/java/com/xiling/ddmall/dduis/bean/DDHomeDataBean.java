package com.xiling.ddmall.dduis.bean;

import com.xiling.ddmall.ddui.bean.DDHomeBanner;

import java.io.Serializable;
import java.util.ArrayList;

public class DDHomeDataBean implements Serializable {

    //首页样式
    private DDHomeStyleBean indexStyleBean;
    //标题栏右侧活动按钮
    private ArrayList<DDHomeBanner> topRight;
    //分类列表
    private ArrayList<DDHomeCategoryBean> indexCategoryBeanList = new ArrayList<>();
    //活动分类
    private ArrayList<DDHomeCategoryBean> categoryLabel = new ArrayList<>();
    //轮播图下面的五个菜单按钮
    private ArrayList<DDHomeBanner> bannerBottomFive = new ArrayList<>();
    //轮播下的三个活动按钮
    private ArrayList<DDHomeBanner> middleThree = new ArrayList<>();

    //618活动的通栏图片
    private DDHomeBanner indexConfigFor618 = null;

    public DDHomeBanner getIndexConfigFor618() {
        return indexConfigFor618;
    }

    public void setIndexConfigFor618(DDHomeBanner indexConfigFor618) {
        this.indexConfigFor618 = indexConfigFor618;
    }

    public DDHomeStyleBean getIndexStyleBean() {
        return indexStyleBean;
    }

    public void setIndexStyleBean(DDHomeStyleBean indexStyleBean) {
        this.indexStyleBean = indexStyleBean;
    }

    public ArrayList<DDHomeBanner> getTopRight() {
        return topRight;
    }

    public void setTopRight(ArrayList<DDHomeBanner> topRight) {
        this.topRight = topRight;
    }

    public ArrayList<DDHomeCategoryBean> getIndexCategoryBeanList() {
        return indexCategoryBeanList;
    }

    public void setIndexCategoryBeanList(ArrayList<DDHomeCategoryBean> indexCategoryBeanList) {
        this.indexCategoryBeanList = indexCategoryBeanList;
    }

    public ArrayList<DDHomeCategoryBean> getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(ArrayList<DDHomeCategoryBean> categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public ArrayList<DDHomeBanner> getBannerBottomFive() {
        return bannerBottomFive;
    }

    public void setBannerBottomFive(ArrayList<DDHomeBanner> bannerBottomFive) {
        this.bannerBottomFive = bannerBottomFive;
    }

    public ArrayList<DDHomeBanner> getMiddleThree() {
        return middleThree;
    }

    public void setMiddleThree(ArrayList<DDHomeBanner> middleThree) {
        this.middleThree = middleThree;
    }
}
