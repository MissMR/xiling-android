package com.xiling.dduis.bean;

import com.xiling.ddui.bean.AutoClickBean;

import java.util.List;

/**
 * @author pt
 * 首页集合实体
 * @data 2019/12/24
 */
public class HomeDataBean {


    private List<SecondCategoryListBean> secondCategoryList;
    private List<BannerListBean> bannerList;
    private List<TabListBean> tabList;
    private List<ActivityListBean> activityList;
    private List<BrandHotSaleListBean> brandHotSaleList;

    public List<SecondCategoryListBean> getSecondCategoryList() {
        return secondCategoryList;
    }

    public void setSecondCategoryList(List<SecondCategoryListBean> secondCategoryList) {
        this.secondCategoryList = secondCategoryList;
    }

    public List<BannerListBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerListBean> bannerList) {
        this.bannerList = bannerList;
    }

    public List<TabListBean> getTabList() {
        return tabList;
    }

    public void setTabList(List<TabListBean> tabList) {
        this.tabList = tabList;
    }

    public List<ActivityListBean> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActivityListBean> activityList) {
        this.activityList = activityList;
    }

    public List<BrandHotSaleListBean> getBrandHotSaleList() {
        return brandHotSaleList;
    }

    public void setBrandHotSaleList(List<BrandHotSaleListBean> brandHotSaleList) {
        this.brandHotSaleList = brandHotSaleList;
    }

    public static class SecondCategoryListBean {
        /**
         * categoryId : 89069c330b8549a9bddc88177443936f
         * parentId : 4d3390f6cafe409288f7e93dace2988e
         * categoryName : 美发护发
         */

        private String categoryId;
        private String parentId;

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        private String parentName;
        private String categoryName;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

    public static class BannerListBean implements AutoClickBean {
        /**
         * localtion : 2bbc886c94f448679e870cb10394b463
         * event : native
         * target : spuDetail?spuId=0188c7784ffc42f0b9851e48ef6090b3
         * sort : 200
         * imgUrl : https://oss.dodomall.com/brand/2019-06/20190618084846714HX.jpg
         */

        private String localtion;
        private String event;
        private String target;
        private int sort;
        private String imgUrl;

        public String getLocaltion() {
            return localtion;
        }

        public void setLocaltion(String localtion) {
            this.localtion = localtion;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class TabListBean implements AutoClickBean {
        /**
         * indexActivityId : 13
         * localtion : 1
         * title : 会员特权
         * event : link
         * target : http://www.baidu.com
         * identityType : null
         * sort : 1
         * imgUrl : http://xiling-test.oss-cn-qingdao.aliyuncs.com/brand/2019-12/201912050304320567J.png
         */

        private int indexActivityId;
        private int localtion;
        private String title;
        private String event;
        private String target;
        private Object identityType;
        private int sort;
        private String imgUrl;

        public int getIndexActivityId() {
            return indexActivityId;
        }

        public void setIndexActivityId(int indexActivityId) {
            this.indexActivityId = indexActivityId;
        }

        public int getLocaltion() {
            return localtion;
        }

        public void setLocaltion(int localtion) {
            this.localtion = localtion;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public Object getIdentityType() {
            return identityType;
        }

        public void setIdentityType(Object identityType) {
            this.identityType = identityType;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class ActivityListBean implements AutoClickBean {
        /**
         * indexActivityId : 17
         * localtion : 2
         * title : 每日新品
         * event : link
         * target : http://www.baidu.com
         * identityType : null
         * sort : 1
         * imgUrl : http://xiling-test.oss-cn-qingdao.aliyuncs.com/brand/2019-12/20191205031543773D6.png
         */

        private int indexActivityId;
        private int localtion;
        private String title;
        private String event;
        private String target;
        private Object identityType;
        private int sort;
        private String imgUrl;

        public int getIndexActivityId() {
            return indexActivityId;
        }

        public void setIndexActivityId(int indexActivityId) {
            this.indexActivityId = indexActivityId;
        }

        public int getLocaltion() {
            return localtion;
        }

        public void setLocaltion(int localtion) {
            this.localtion = localtion;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public Object getIdentityType() {
            return identityType;
        }

        public void setIdentityType(Object identityType) {
            this.identityType = identityType;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class BrandHotSaleListBean implements AutoClickBean {
        /**
         * indexActivityId : 20
         * localtion : 3
         * title : 热卖1
         * event : link
         * target : http://www.baidu.com
         * identityType : null
         * sort : 1
         * imgUrl : http://xiling-test.oss-cn-qingdao.aliyuncs.com/brand/2019-12/201912050316432394G.png
         */

        private int indexActivityId;
        private int localtion;
        private String title;
        private String event;
        private String target;
        private Object identityType;
        private int sort;
        private String imgUrl;

        public int getIndexActivityId() {
            return indexActivityId;
        }

        public void setIndexActivityId(int indexActivityId) {
            this.indexActivityId = indexActivityId;
        }

        public int getLocaltion() {
            return localtion;
        }

        public void setLocaltion(int localtion) {
            this.localtion = localtion;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public Object getIdentityType() {
            return identityType;
        }

        public void setIdentityType(Object identityType) {
            this.identityType = identityType;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
