package com.xiling.ddmall.ddui.bean;

/**
 * created by Jigsaw at 2018/10/12
 */
public class TopicBean {

    /**
     * id : 11
     * title : 测试视频上传
     * bannerImgUrl : https://oss.dodomall.com/community/topic/2018-10/20181010153708.png
     * hotNum : 889
     * isMyLike : 0
     * imgUrl : https://oss.dodomall.com/community/topic/2018-10/20181010153704.png
     * bannerImgUrlDetail : https://oss.dodomall.com/community/topic/2018-10/20181010153710.png
     * isHot : 1
     * content : <p style="white-space: normal;">首先来点文字首先来点文字首先来点文字首先来点文字首先来点文字首先来点文字首先来点文字首先来点文字首先来点文字</p><p style="white-space: normal;">再来个图片</p><p style="white-space: normal;"><br/></p><p><img src="https://oss.dodomall.com/ueditor/1539157086148.jpg" title="1539157086148.jpg" alt="1539157086148.jpg"/></p><p style="white-space: normal;">然后来点样式然后来点样式</p><p style="white-space: normal;">然后来点样式然后来点样式</p><p style="white-space: normal;">然后来点样式然后来点样式</p><p><br/></p><p>然后再来个视频</p><p><br/></p><p><video src="https://oss.dodomall.com/ueditor/1539157139028.mp4" width="100%" height="280" controls="controls"/></p>
     */

    // 话题列表字段
    private String topicId;
    private String title;
    private String bannerImgUrl;
    private long hotNum;
    private int isMyLike;

    // 话题详情  增加字段
    //imgUrl: 缩略图 横着滑动的地方显示imgUrl字段，其他列表都显示bannerImgUrl
    private String imgUrl;// 横向滑动列表话题 图片
    private String bannerImgUrlDetail;
    private String isHot;
    private String content;


    public boolean isFollow() {
        return this.isMyLike > 0;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerImgUrl() {
        return bannerImgUrl;
    }

    public void setBannerImgUrl(String bannerImgUrl) {
        this.bannerImgUrl = bannerImgUrl;
    }

    public long getHotNum() {
        return hotNum;
    }

    public void setHotNum(long hotNum) {
        this.hotNum = hotNum;
    }

    public int getIsMyLike() {
        return isMyLike;
    }

    public void setIsMyLike(int isMyLike) {
        this.isMyLike = isMyLike;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBannerImgUrlDetail() {
        return bannerImgUrlDetail;
    }

    public void setBannerImgUrlDetail(String bannerImgUrlDetail) {
        this.bannerImgUrlDetail = bannerImgUrlDetail;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
