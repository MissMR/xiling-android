package com.xiling.ddmall.module.community;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Stone
 * @time 2018/4/17  18:41
 * @desc ${TODD}
 */

public class CourseModule implements Serializable {

    /**
     * category : {"categoryId":"d923c1aaea2f4983a9deade217f2545f","title":"测试分类1","iconUrl":"http://39.108.50.110/G1/M00/00/32/rBIdXFrN1sCAYsKOAAAUF6U-fF8611.png","type":1}
     * courseList : [{"courseId":"8ba73c02c3f84100af13da858cae1522","authorId":"fdc85232cf1a4b6196f3bfec987faa45","categoryId":"d923c1aaea2f4983a9deade217f2545f","courseType":1,"courseTypeStr":"语音课程","thumbUrl":"http://39.108.50.110/G1/M00/00/24/rBIdXFqmOUaAaSE_AAD19G7cRUw476.jpg","title":"快速把产品卖出去的五种方法！","intro":"测试","audioBackgroup":"http://39.108.50.110/G1/M00/00/24/rBIdXFqmQTmAWsNbAADeK-LgLug825.jpg","audioUrl":"http://39.108.50.110/G1/M00/00/24/rBIdXFqmQVGASseFABHnvnZqF9c865.mp3","audioSecond":"293","content":"","browseCount":71,"commentCount":0,"createDate":"2018-03-12 16:31:49","updateDate":"2018-03-13 21:57:55","deleteFlag":0}]
     */

    private CategoryBean category;
    private ArrayList<Course> courseList;
    private int courseCount;

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public static class CategoryBean {
        /**
         * categoryId : d923c1aaea2f4983a9deade217f2545f
         * title : 测试分类1
         * iconUrl : http://39.108.50.110/G1/M00/00/32/rBIdXFrN1sCAYsKOAAAUF6U-fF8611.png
         * type : 1
         */

        private String categoryId;
        private String title;
        private String iconUrl;
        private int type;

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
    }

    public static class CourseListBean {
        /**
         * courseId : 8ba73c02c3f84100af13da858cae1522
         * authorId : fdc85232cf1a4b6196f3bfec987faa45
         * categoryId : d923c1aaea2f4983a9deade217f2545f
         * courseType : 1
         * courseTypeStr : 语音课程
         * thumbUrl : http://39.108.50.110/G1/M00/00/24/rBIdXFqmOUaAaSE_AAD19G7cRUw476.jpg
         * title : 快速把产品卖出去的五种方法！
         * intro : 测试
         * audioBackgroup : http://39.108.50.110/G1/M00/00/24/rBIdXFqmQTmAWsNbAADeK-LgLug825.jpg
         * audioUrl : http://39.108.50.110/G1/M00/00/24/rBIdXFqmQVGASseFABHnvnZqF9c865.mp3
         * audioSecond : 293
         * content :
         * browseCount : 71
         * commentCount : 0
         * createDate : 2018-03-12 16:31:49
         * updateDate : 2018-03-13 21:57:55
         * deleteFlag : 0
         */

        private String courseId;
        private String authorId;
        private String categoryId;
        private int courseType;
        private String courseTypeStr;
        private String thumbUrl;
        private String title;
        private String intro;
        private String audioBackgroup;
        private String audioUrl;
        private String audioSecond;
        private String content;
        private int browseCount;
        private int commentCount;
        private String createDate;
        private String updateDate;
        private int deleteFlag;

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

        public int getCourseType() {
            return courseType;
        }

        public void setCourseType(int courseType) {
            this.courseType = courseType;
        }

        public String getCourseTypeStr() {
            return courseTypeStr;
        }

        public void setCourseTypeStr(String courseTypeStr) {
            this.courseTypeStr = courseTypeStr;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
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

        public String getAudioBackgroup() {
            return audioBackgroup;
        }

        public void setAudioBackgroup(String audioBackgroup) {
            this.audioBackgroup = audioBackgroup;
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
        }

        public String getAudioSecond() {
            return audioSecond;
        }

        public void setAudioSecond(String audioSecond) {
            this.audioSecond = audioSecond;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getBrowseCount() {
            return browseCount;
        }

        public void setBrowseCount(int browseCount) {
            this.browseCount = browseCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
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
