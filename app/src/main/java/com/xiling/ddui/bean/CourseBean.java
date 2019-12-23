package com.xiling.ddui.bean;

import java.io.Serializable;

public class CourseBean implements Serializable {


    private String courseId;//课程ID
    private String authorId;//作者ID
    private String categoryId;//分类ID
    private String courseType;//课程类型ID
    private String courseTypeStr;//课程类型文本
    private String imageUrl;//图片地址
    private String title;//标题
    private String intro;//描述
    private String commentCount;//评论数
    private String readCountReal;//阅读数
    private String likeCount;//喜欢数

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseTypeStr() {
        return courseTypeStr;
    }

    public void setCourseTypeStr(String courseTypeStr) {
        this.courseTypeStr = courseTypeStr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getReadCountReal() {
        return readCountReal;
    }

    public void setReadCountReal(String readCountReal) {
        this.readCountReal = readCountReal;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }
}
