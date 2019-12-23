package com.xiling.module.publish;

/**
 * @author Stone
 * @time 2018/4/16  11:06
 * @desc ${TODD}
 */

public class HistoryExtra extends BaseModel {

    /**
     * authorId : 34a561b32c664348bb88e0fb04647b25
     * name : 这里显示昵称👩‍👦
     * intro :
     * headImage : http://testimg.beautysecret.cn/G1/M00/00/02/cjc2GFmMXByAJQt-AAFk_SYxw6U982.jpg
     * backUrl : http://testimg.beautysecret.cn/G1/M00/00/02/cjc2GFmMXByAJQt-AAFk_SYxw6U982.jpg
     * createDate : 2018-04-12 16:07:12
     * mediaCount : 0
     * imageCount : 3
     */

    private String authorId;
    private String name;
    private String intro;
    private String headImage;
    private String backUrl;
    private String createDate;
    private int mediaCount;
    private int imageCount;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }
}
