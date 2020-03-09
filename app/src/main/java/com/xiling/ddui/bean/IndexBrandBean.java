package com.xiling.ddui.bean;

import com.xiling.dduis.bean.HomeRecommendDataBean;

import java.util.List;

public class IndexBrandBean {
    /**
     * brandId : 48ad447b5dc6482dbf933fff2f2d766b
     * categoryId :
     * indexUrl : http://oss.xilingbm.com/brand/2020-03/202003060931437722F.png
     * showIndex :
     * brandName : 汤臣倍健
     * iconUrl :
     * categoryUrl :
     * remark :
     * content :
     * createDate :
     * updateDate :
     * deleteFlag :
     */

    private String brandId;
    private String categoryId;
    private String indexUrl;
    private String showIndex;
    private String brandName;
    private String iconUrl;
    private String categoryUrl;
    private String remark;
    private String content;
    private String createDate;
    private String updateDate;
    private String deleteFlag;


    public List<HomeRecommendDataBean.DatasBean> getIndexBrandBeanList() {
        return indexBrandBeanList;
    }

    public void setIndexBrandBeanList(List<HomeRecommendDataBean.DatasBean> indexBrandBeanList) {
        this.indexBrandBeanList = indexBrandBeanList;
    }

    private List<HomeRecommendDataBean.DatasBean> indexBrandBeanList;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(String showIndex) {
        this.showIndex = showIndex;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
