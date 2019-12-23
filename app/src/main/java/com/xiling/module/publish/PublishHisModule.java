package com.xiling.module.publish;

import com.xiling.module.community.GroupCategoryModel;

import java.util.ArrayList;

/**
 * @author Stone
 * @time 2018/4/16  11:05
 * @desc ${TODD}
 */

public class PublishHisModule extends BaseModel {

    /**
     * libraryId : 253d120745624e0e90733d7b14544409
     * authorId : 34a561b32c664348bb88e0fb04647b25
     * authorName : 这里显示昵称👩‍👦
     * authorHeadImage : http://testimg.beautysecret.cn/G1/M00/00/02/cjc2GFmMXByAJQt-AAFk_SYxw6U982.jpg
     * status : 0
     * statusStr : 审核中
     * type : 1
     * content : app客户端图文消息发布2222
     * images : ["http://39.108.50.110/G1/M00/00/29/rBIdXFqnrmOAdmPSAAHg3EJpZII297.jpg","http://39.108.50.110/G1/M00/00/29/rBIdXFqnrmCAFjYBAAJEE_4-fGA003.jpg"]
     * mediaImage :
     * mediaTitle :
     * mediaUrl :
     * transferCount : 0
     * createDate : 2018-04-12 17:23:59
     * updateDate : 2018-04-12 17:23:59
     * deleteFlag : 0
     */

    private String libraryId;
    private String authorId;
    private String authorName;
    private String authorHeadImage;
    private int status;
    private String statusStr;
    private int type;
    private String content;
    private String mediaImage;
    private String mediaTitle;
    private String mediaUrl;
    private int transferCount;
    private String createDate;
    private String updateDate;
    private int deleteFlag;

    public ArrayList<GroupCategoryModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<GroupCategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    private ArrayList<String> images=new ArrayList<>();
    private ArrayList<GroupCategoryModel>categoryList;

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorHeadImage() {
        return authorHeadImage;
    }

    public void setAuthorHeadImage(String authorHeadImage) {
        this.authorHeadImage = authorHeadImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaImage() {
        return mediaImage;
    }

    public void setMediaImage(String mediaImage) {
        this.mediaImage = mediaImage;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getTransferCount() {
        return transferCount;
    }

    public void setTransferCount(int transferCount) {
        this.transferCount = transferCount;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
