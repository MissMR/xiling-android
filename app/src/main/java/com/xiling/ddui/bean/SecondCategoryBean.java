package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SecondCategoryBean {

    private ArrayList<SecondCategoryListBean> secondCategoryList;
    private ArrayList<BrandBeanListBean> brandBeanList;

    public ArrayList<SecondCategoryListBean> getSecondCategoryList() {
        return secondCategoryList;
    }

    public void setSecondCategoryList(ArrayList<SecondCategoryListBean> secondCategoryList) {
        this.secondCategoryList = secondCategoryList;
    }

    public ArrayList<BrandBeanListBean> getBrandBeanList() {
        return brandBeanList;
    }

    public void setBrandBeanList(ArrayList<BrandBeanListBean> brandBeanList) {
        this.brandBeanList = brandBeanList;
    }

    public static class SecondCategoryListBean implements Parcelable {
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
        private List<String> categoryBeans;

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

        public List<String> getCategoryBeans() {
            return categoryBeans;
        }

        public void setCategoryBeans(List<String> categoryBeans) {
            this.categoryBeans = categoryBeans;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.categoryId);
            dest.writeString(this.iconUrl);
            dest.writeString(this.categoryName);
            dest.writeString(this.categoryNameShort);
            dest.writeString(this.pathUrl);
            dest.writeString(this.parentId);
            dest.writeString(this.remark);
            dest.writeString(this.bannerUrl);
            dest.writeString(this.secondCategoryAndBrand);
            dest.writeStringList(this.categoryBeans);
        }

        public SecondCategoryListBean() {
        }

        protected SecondCategoryListBean(Parcel in) {
            this.categoryId = in.readString();
            this.iconUrl = in.readString();
            this.categoryName = in.readString();
            this.categoryNameShort = in.readString();
            this.pathUrl = in.readString();
            this.parentId = in.readString();
            this.remark = in.readString();
            this.bannerUrl = in.readString();
            this.secondCategoryAndBrand = in.readString();
            this.categoryBeans = in.createStringArrayList();
        }

        public static final Parcelable.Creator<SecondCategoryListBean> CREATOR = new Parcelable.Creator<SecondCategoryListBean>() {
            @Override
            public SecondCategoryListBean createFromParcel(Parcel source) {
                return new SecondCategoryListBean(source);
            }

            @Override
            public SecondCategoryListBean[] newArray(int size) {
                return new SecondCategoryListBean[size];
            }
        };
    }

    public static class BrandBeanListBean implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.brandId);
            dest.writeString(this.categoryId);
            dest.writeString(this.brandName);
            dest.writeString(this.iconUrl);
            dest.writeString(this.remark);
            dest.writeString(this.createDate);
            dest.writeString(this.updateDate);
            dest.writeInt(this.deleteFlag);
        }

        public BrandBeanListBean() {
        }

        protected BrandBeanListBean(Parcel in) {
            this.brandId = in.readString();
            this.categoryId = in.readString();
            this.brandName = in.readString();
            this.iconUrl = in.readString();
            this.remark = in.readString();
            this.createDate = in.readString();
            this.updateDate = in.readString();
            this.deleteFlag = in.readInt();
        }

        public static final Parcelable.Creator<BrandBeanListBean> CREATOR = new Parcelable.Creator<BrandBeanListBean>() {
            @Override
            public BrandBeanListBean createFromParcel(Parcel source) {
                return new BrandBeanListBean(source);
            }

            @Override
            public BrandBeanListBean[] newArray(int size) {
                return new BrandBeanListBean[size];
            }
        };
    }
}
