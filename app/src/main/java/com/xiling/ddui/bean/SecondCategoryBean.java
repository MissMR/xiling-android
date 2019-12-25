package com.xiling.ddui.bean;

import java.util.List;

public class SecondCategoryBean {

    private List<SecondCategoryListBean> secondCategoryList;
    private List<BrandBeanListBean> brandBeanList;

    public List<SecondCategoryListBean> getSecondCategoryList() {
        return secondCategoryList;
    }

    public void setSecondCategoryList(List<SecondCategoryListBean> secondCategoryList) {
        this.secondCategoryList = secondCategoryList;
    }

    public List<BrandBeanListBean> getBrandBeanList() {
        return brandBeanList;
    }

    public void setBrandBeanList(List<BrandBeanListBean> brandBeanList) {
        this.brandBeanList = brandBeanList;
    }

    public static class SecondCategoryListBean {
        /**
         * categoryId : 7c2b1136d198413992b3c72bdc4d292d
         * iconUrl : https://oss.dodomall.com/category/2019-04/20190426103449951P9.png
         * categoryName : 面部护理
         * categoryNameShort : 面部护理
         * pathUrl :
         * parentId : 4d3390f6cafe409288f7e93dace2988e
         * remark :
         * bannerUrl :
         * secondCategoryAndBrand :
         * categoryBeans : []
         */

        private String categoryId;
        private String iconUrl;
        private String categoryName;
        private String categoryNameShort;
        private String pathUrl;
        private String parentId;
        private String remark;
        private String bannerUrl;
        private String secondCategoryAndBrand;
        private List<?> categoryBeans;

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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getBannerUrl() {
            return bannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
        }

        public String getSecondCategoryAndBrand() {
            return secondCategoryAndBrand;
        }

        public void setSecondCategoryAndBrand(String secondCategoryAndBrand) {
            this.secondCategoryAndBrand = secondCategoryAndBrand;
        }

        public List<?> getCategoryBeans() {
            return categoryBeans;
        }

        public void setCategoryBeans(List<?> categoryBeans) {
            this.categoryBeans = categoryBeans;
        }
    }

    public static class BrandBeanListBean {
        /**
         * brandId : 5d3d232efa9b4802a2ce7db0856c4d75
         * categoryId :
         * brandName : 可雅可美
         * iconUrl : https://oss.dodomall.com/brand/2019-11/201911190240062952E.png
         * remark :
         * createDate : 2019-11-19 10:40:08
         * updateDate : 2019-11-19 10:40:07
         * deleteFlag : 1
         */

        private String brandId;
        private String categoryId;
        private String brandName;
        private String iconUrl;
        private String remark;
        private String createDate;
        private String updateDate;
        private int deleteFlag;

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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public int getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(int deleteFlag) {
            this.deleteFlag = deleteFlag;
        }
    }
}
