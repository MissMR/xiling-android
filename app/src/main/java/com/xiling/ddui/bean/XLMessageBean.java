package com.xiling.ddui.bean;

public class XLMessageBean {
    /**
     * msgId : 28
     * businessType : 1
     * businessCode : 0691582477376738
     * isRead : 2
     * image : http://oss.xilingbm.com/product/2020-02/20200217015752912VJ.jpg
     * title : 付款成功通知
     * content : 亲爱的逄涛您的订单支付款成功，等待发货，订单号：0691582477376738
     * createDate : 2020-02-21 09:53:44
     */

    private int msgId;
    private String businessType;
    private String businessCode;
    private int isRead;
    private String image;
    private String title;
    private String content;
    private String createDate;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
