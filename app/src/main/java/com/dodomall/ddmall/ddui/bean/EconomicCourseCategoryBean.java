package com.dodomall.ddmall.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/10/12
 */
public class EconomicCourseCategoryBean {
    /**
     * categoryId : ba6bfcfa6e8446738e526b5af077361a
     * title : 新手学堂
     * iconUrl :
     * type : 0
     * courseDetail : [{"courseId":"6e9522551a7a4665bf2184ab2a23eabf","title":"新手学堂分类","intro":"新手学堂分类新手学堂分类新手学堂分类","readCount":205,"imageUrl":"http://oss.dianduoduo.store/course/2018-10/20181010185646.jpg"},{"courseId":"eda980e86a534a4a88a15c0ad4c75845","title":"测试精品课程-分享","intro":"测试精品课程-分享测试精品课程-分享测试精品课程-分享测试精品课程-分享","readCount":373,"imageUrl":"http://oss.dianduoduo.store/course/2018-10/20181010154946.jpg"}]
     */

    private String categoryId;
    private String title;
    private String iconUrl;
    // 1 小图  否则大图
    private int type;
    // 商学院首页 分类带课程列表
    private List<EconomicCourseBean> courseDetail;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<EconomicCourseBean> getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(List<EconomicCourseBean> courseDetail) {
        this.courseDetail = courseDetail;
    }


}
