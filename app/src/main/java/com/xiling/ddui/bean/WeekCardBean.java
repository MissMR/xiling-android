package com.xiling.ddui.bean;

/**
 * 我的周卡列表
 */
public class WeekCardBean {

    /**
     * id : 143
     * incId : 10130
     * weekId : 1
     * weekType : 1
     * price : 1
     * openTime :
     * payTime : 2020-02-20 15:23:50
     * expiredTime :
     * payStatus : 2
     * status : 1
     * useStatus : 1
     * sendMessage : 1
     * weekOrderNo : 5e4e1f7de4b0019de187c5b0
     * weekName : VIP会员周卡
     * weekRemark : 全场下单享受6.5折优惠价格
     */

    private String id;
    private int incId;
    private int weekId;
    private int weekType;
    private double price;
    private String openTime;
    private String payTime;
    private String expiredTime;
    private int payStatus;
    private int status;
    private int useStatus;
    private int sendMessage;
    private String weekOrderNo;
    private String weekName;
    private String weekRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIncId() {
        return incId;
    }

    public void setIncId(int incId) {
        this.incId = incId;
    }

    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public int getWeekType() {
        return weekType;
    }

    public void setWeekType(int weekType) {
        this.weekType = weekType;
    }

    public double getPrice() {
        return price/100;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public int getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(int sendMessage) {
        this.sendMessage = sendMessage;
    }

    public String getWeekOrderNo() {
        return weekOrderNo;
    }

    public void setWeekOrderNo(String weekOrderNo) {
        this.weekOrderNo = weekOrderNo;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getWeekRemark() {
        return weekRemark;
    }

    public void setWeekRemark(String weekRemark) {
        this.weekRemark = weekRemark;
    }
}
