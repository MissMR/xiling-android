package com.xiling.ddmall.module.community;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Stone
 * @time 2018/1/4  14:12
 * @desc ${TODD}
 */

public class MaterialVideoModule implements Serializable{
    /**
     * libraryId : 61a7ba7fc8b04a218f76b92621a70ef6
     * authorId : c43f18f1038c444eaa9d0d873c445392
     * authorName : 张无忌
     * authorHeadImage : http://39.108.50.110/G1/M00/00/07/rBIdXFpHOYeAb7YvAAE4AALesgg203.jpg
     * status : 1
     * type : 1
     * content : 9张图片上传
     * images : ["http://39.108.50.110/G1/M00/00/07/rBIdXFpHXgWAA1NBAAE4AALesgg584.jpg","http://39.108.50.110/G1/M00/00/07/rBIdXFpHXgeAMK2VAAC0SMVOEKI168.png","http://39.108.50.110/G1/M00/00/07/rBIdXFpHXgqAbKFDAAE4AALesgg409.jpg","http://39.108.50.110/G1/M00/00/07/rBIdXFpHXg2AExZqAAAIfehP3uA829.jpg","http://39.108.50.110/G1/M00/00/07/rBIdXFpHXg-AEQnLAAAItXVXnIs747.jpg","http://39.108.50.110/G1/M00/00/08/rBIdXFpHXhKAX29qAAAIfehP3uA567.jpg","http://39.108.50.110/G1/M00/00/08/rBIdXFpHXhSACiSHAAAIfehP3uA341.jpg","http://39.108.50.110/G1/M00/00/08/rBIdXFpHXheAJQSBAAAJHdsTRho532.jpg","http://39.108.50.110/G1/M00/00/08/rBIdXFpHXhmAEPrhAAAIeGI-a1o580.jpg"]
     * mediaImage :
     * mediaTitle :
     * mediaUrl :
     * transferCount : 12
     * createDate : 2017-12-30 17:36:48
     * updateDate : 2017-12-30 17:37:16
     * deleteFlag : 0
     */

    private String libraryId;
    private String authorId;
    private String authorName;
    private String authorHeadImage;
    private int status;
    private int type;
    private int topicType;
    private String content;
    private String linkTitle;
    private String linkImage;

    public boolean isCollapsed=true;

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    private String linkUrl;

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    private String likeId;

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    private int likeStatus;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    private String topicId;
    private ArrayList<CommentModule>comments;

    public ArrayList<CommentModule> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentModule> comments) {
        this.comments = comments;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    private String commentCount;
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    private int likeCount;



    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    private String mediaImage;
    private String mediaTitle;
    private String mediaUrl;
    private int transferCount;
    private String createDate;
    private String updateDate;
    private int deleteFlag;
    private ArrayList<String> images;


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

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int type) {
        this.type = type;
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

    public class CommentModule implements Serializable{

        /**
         * commentId : 0a0644fff75646b3a79fa2d717d1ae0d
         * topicId : 1761605093f54e3698ba10d53c8785a4
         * memberId : 34a561b32c664348bb88e0fb04647b25
         * nickName : 公*4
         * headImage : http://testimg.beautysecret.cn/G1/M00/00/02/cjc2GFmMXByAJQt-AAFk_SYxw6U982.jpg
         * content : 测试
         * reply :
         * replyDate :
         * status : 1
         * createDate : 2017-09-12 17:57:08
         * updateDate : 2017-12-30 11:03:05
         * deleteFlag : 0
         */

        private String commentId;
        private String topicId;
        private String memberId;
        private String nickName;
        private String headImage;
        private String content;
        private String reply;
        private String replyDate;
        private int status;
        private String createDate;
        private String updateDate;
        private int deleteFlag;

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getNickName() {
            return nickName+"：";
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String  getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getReplyDate() {
            return replyDate;
        }

        public void setReplyDate(String replyDate) {
            this.replyDate = replyDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
