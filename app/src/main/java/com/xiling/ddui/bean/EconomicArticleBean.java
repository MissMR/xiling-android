package com.xiling.ddui.bean;

import java.io.Serializable;

/**
 * created by Jigsaw at 2018/10/12
 * 店多多头条 和 店主故事
 */
public class EconomicArticleBean implements Serializable {
    /**
     * articleId : e0d9b5bb21a546da9a72893c9678e6f2
     * title : 测试头条不可分享
     * imageUrl : http://oss.dianduoduo.store/course/2018-10/20181010114923.jpg
     * homeImageUrl :
     * readCount : 251
     * createDate : 2018-10-10 11:49:26.0
     */

    private String articleId;
    private String title;
    private String imageUrl;
    private String homeImageUrl;
    private int readCount;
    private String createDate;

    // 文章详情字段
    private String h5Url;
    private int share;
    private boolean isLike;

    public boolean canShare() {
        return share == 1;
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

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHomeImageUrl() {
        return homeImageUrl;
    }

    public void setHomeImageUrl(String homeImageUrl) {
        this.homeImageUrl = homeImageUrl;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
