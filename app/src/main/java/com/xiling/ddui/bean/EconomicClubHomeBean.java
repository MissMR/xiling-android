package com.xiling.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/10/12
 */
public class EconomicClubHomeBean {

    private List<EconomicHomeBanner> homeBannerList;
    private List<EconomicCourseCategoryBean> courseCategoryDetail;
    private List<EconomicArticleBean> newsDetail;
    private List<EconomicArticleBean> storyDetail;

    public List<EconomicHomeBanner> getHomeBannerList() {
        return homeBannerList;
    }

    public void setHomeBannerList(List<EconomicHomeBanner> homeBannerList) {
        this.homeBannerList = homeBannerList;
    }

    public List<EconomicCourseCategoryBean> getCourseCategoryDetail() {
        return courseCategoryDetail;
    }

    public void setCourseCategoryDetail(List<EconomicCourseCategoryBean> courseCategoryDetail) {
        this.courseCategoryDetail = courseCategoryDetail;
    }

    public List<EconomicArticleBean> getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(List<EconomicArticleBean> newsDetail) {
        this.newsDetail = newsDetail;
    }

    public List<EconomicArticleBean> getStoryDetail() {
        return storyDetail;
    }

    public void setStoryDetail(List<EconomicArticleBean> storyDetail) {
        this.storyDetail = storyDetail;
    }


}
