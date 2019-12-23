package com.xiling.ddui.bean;

/**
 * created by Jigsaw at 2018/10/12
 * 早晚安语 实体类
 */
public class HelloBean {

    /**
     * id : 10
     * author_id : 0
     * publish_time : 2018-10-25 18:18:00
     * type : 1
     * content : 吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧吧v吧吧
     * ggggggg
     * images_url : https://oss.dodomall.com/greeting/2018-10/20181009184355.jpg
     * down_num : 0
     * down_num_real : 0
     * share_num : 0
     * share_num_real : 0
     * create_time : 2018-10-09 18:43:55
     * update_time : 2018-10-10 11:54:28
     * publish_time_month : 10
     * publish_time_day : 25
     */

    private String id;
    private String author_id;
    private String publish_time;
    private int type;
    private String content;
    private String images_url;
    private long down_num;
    private long down_num_real;
    private long share_num;
    private long share_num_real;
    private String create_time;
    private String update_time;
    private int publish_time_month;
    private int publish_time_day;

    private int qrCodeType = 0;
    private String qrCodeCls = "0";       //字符串类型，0标识不生成二维码，1标识生成
    private String qrCodeUrl = "";          //二维码跳转路径

    /**
     * 是否创建合成二维码
     */
    public boolean isCreateQR() {
        return (this.qrCodeType > 0);
    }

    public int getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(int qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public String getQrCodeCls() {
        return qrCodeCls;
    }

    public void setQrCodeCls(String qrCodeCls) {
        this.qrCodeCls = qrCodeCls;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getTotalDownloadCount() {
        return String.valueOf(down_num + down_num_real);
    }

    public String getTotalShareCount() {
        return String.valueOf(share_num + share_num_real);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
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

    public String getImages_url() {
        return images_url;
    }

    public void setImages_url(String images_url) {
        this.images_url = images_url;
    }

    public long getDown_num() {
        return down_num;
    }

    public void setDown_num(long down_num) {
        this.down_num = down_num;
    }

    public long getDown_num_real() {
        return down_num_real;
    }

    public void setDown_num_real(long down_num_real) {
        this.down_num_real = down_num_real;
    }

    public long getShare_num() {
        return share_num;
    }

    public void setShare_num(long share_num) {
        this.share_num = share_num;
    }

    public long getShare_num_real() {
        return share_num_real;
    }

    public void setShare_num_real(long share_num_real) {
        this.share_num_real = share_num_real;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getPublish_time_month() {
        return publish_time_month;
    }

    public void setPublish_time_month(int publish_time_month) {
        this.publish_time_month = publish_time_month;
    }

    public int getPublish_time_day() {
        return publish_time_day;
    }

    public void setPublish_time_day(int publish_time_day) {
        this.publish_time_day = publish_time_day;
    }
}
