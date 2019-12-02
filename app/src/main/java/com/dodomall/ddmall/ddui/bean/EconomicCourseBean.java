package com.dodomall.ddmall.ddui.bean;

import java.io.Serializable;

/**
 * created by Jigsaw at 2018/10/12
 */
public class EconomicCourseBean implements Serializable {
    /**
     * courseId : 6e9522551a7a4665bf2184ab2a23eabf
     * title : 新手学堂分类
     * intro : 新手学堂分类新手学堂分类新手学堂分类
     * readCount : 205
     * imageUrl : http://oss.dianduoduo.store/course/2018-10/20181010185646.jpg
     * ---课程列表图片封面图	imageUrl	string
     * ---课程简介（副标题）	intro	string
     * ---已学人数	readCount	number
     * ---课程标题	title	string
     * ---课程id	courseId	string
     */

    private String courseId;
    private String title;
    private String intro;
    private int readCount;
    private String imageUrl;

    // 课程详情的字段
    private boolean beLike;
    private String h5Url;
    private int share;

    public boolean isBeLike() {
        return beLike;
    }

    public void setBeLike(boolean beLike) {
        this.beLike = beLike;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
