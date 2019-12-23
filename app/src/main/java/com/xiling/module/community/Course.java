package com.xiling.module.community;


import java.io.Serializable;
import java.util.Locale;

/**
 * Created by bigbyto on 18/07/2017.
 */

public class Course implements Serializable {

  public String banner;
  public String courseId;
  public String authorId;
  public String categoryId;
  public int courseType;
  public String courseTypeStr;
  public String thumbUrl;
  public String title;
  public String intro;
  public String audioBackgroup;
  public String audioUrl;
  public String audioSecond;
  public String content;
  public int browseCount;
  public int commentCount;
  public String createDate;
  public String updateDate;
  public int deleteFlag;

    public static final int MEDIA = 1;
    public static final int TEXT = 2;


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

    public void setAudioSecond(String audioSecond) {
        this.audioSecond = audioSecond;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setBrowseCount(int browseCount) {
        this.browseCount = browseCount;
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

    public String getAudioSecond() {
        return DateUtils.getVoiceTime(this.audioSecond);
    }



    public String getImage() {
        return thumbUrl.trim();
    }

    public String getCommentCount() {
        if (commentCount < 10000) {
            return commentCount + "";
        } else if (commentCount % 10000 == 0) {
            return commentCount / 10000 + "万";
        } else {
            return String.format(Locale.getDefault(),"%.1f万",commentCount / 10000.0);
        }
    }

    public String getBrowseCount() {
        if (browseCount < 10000) {
            return browseCount + "";
        } else if (browseCount % 10000 == 0) {
            return browseCount / 10000 + "万";
        } else {
            return String.format(Locale.getDefault(),"%.1f万",browseCount / 10000.0);
        }
    }

    public int getOldCount() {
        return commentCount;
    }
}
