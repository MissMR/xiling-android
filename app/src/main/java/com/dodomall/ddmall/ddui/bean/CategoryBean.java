package com.dodomall.ddmall.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/12/10
 * 分类
 */
public class CategoryBean {

    /**
     * categoryId : fe489cbb55e84604993985346f2894f5
     * iconUrl : http://oss.dianduoduo.store/category/2018-11/20181107071042999VX.png
     * categoryName : 美妆护肤
     * categoryNameShort : 美妆
     * pathUrl : http://h5.dianduoduo.store/app_web/list?cateid=fe489cbb55e84604993985346f2894f5
     * parentId :
     * categoryBeans : [{"categoryId":"0532980c09f54aa0a3f9fca6dd1dd24a","iconUrl":"http://oss.dianduoduo.store/category/2018-12/20181208012644420A5.jpg","categoryName":"美妆妆1","categoryNameShort":"美妆1","pathUrl":"http://h5.dianduoduo.store/app_web/list?cateid=0532980c09f54aa0a3f9fca6dd1dd24a","parentId":"fe489cbb55e84604993985346f2894f5","categoryBeans":""}]
     */

    private String categoryId;
    private String iconUrl;
    private String categoryName;
    private String categoryNameShort;
    private String pathUrl;
    private String parentId;
    private String bannerUrl;
    private List<CategoryBean> categoryBeans;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNameShort() {
        return categoryNameShort;
    }

    public void setCategoryNameShort(String categoryNameShort) {
        this.categoryNameShort = categoryNameShort;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<CategoryBean> getCategoryBeans() {
        return categoryBeans;
    }

    public void setCategoryBeans(List<CategoryBean> categoryBeans) {
        this.categoryBeans = categoryBeans;
    }

}
