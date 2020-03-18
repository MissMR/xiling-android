package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class WeekCardInfo implements Parcelable {
    /**
     * id : 117
     * incId : 10011
     * weekId : 1
     * weekType : 1
     * price : 1
     * openTime :
     * payTime : 2020-02-18 15:15:02
     * expiredTime : 2020-02-19 15:31:07
     * payStatus : 2
     * status : 2
     * useStatus : 2
     * sendMessage : 2
     * weekOrderNo : 5e4b8eede4b0b5540676e353
     * weekName : VIP会员周卡
     * weekRemark : 全场下单享受6.5折优惠价格
     */

    private String id;
    private String incId;
    private String weekId;
    private String weekType;
    private String price;
    private String openTime;
    private String payTime;
    private String expiredTime;
    private String payStatus;
    private String status;
    private String useStatus;
    private String sendMessage;
    private String weekOrderNo;
    private String weekName;
    private String weekRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncId() {
        return incId;
    }

    public void setIncId(String incId) {
        this.incId = incId;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public String getWeekType() {
        return weekType;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
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

    public WeekCardInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.incId);
        dest.writeString(this.weekId);
        dest.writeString(this.weekType);
        dest.writeString(this.price);
        dest.writeString(this.openTime);
        dest.writeString(this.payTime);
        dest.writeString(this.expiredTime);
        dest.writeString(this.payStatus);
        dest.writeString(this.status);
        dest.writeString(this.useStatus);
        dest.writeString(this.sendMessage);
        dest.writeString(this.weekOrderNo);
        dest.writeString(this.weekName);
        dest.writeString(this.weekRemark);
    }

    protected WeekCardInfo(Parcel in) {
        this.id = in.readString();
        this.incId = in.readString();
        this.weekId = in.readString();
        this.weekType = in.readString();
        this.price = in.readString();
        this.openTime = in.readString();
        this.payTime = in.readString();
        this.expiredTime = in.readString();
        this.payStatus = in.readString();
        this.status = in.readString();
        this.useStatus = in.readString();
        this.sendMessage = in.readString();
        this.weekOrderNo = in.readString();
        this.weekName = in.readString();
        this.weekRemark = in.readString();
    }

    public static final Creator<WeekCardInfo> CREATOR = new Creator<WeekCardInfo>() {
        @Override
        public WeekCardInfo createFromParcel(Parcel source) {
            return new WeekCardInfo(source);
        }

        @Override
        public WeekCardInfo[] newArray(int size) {
            return new WeekCardInfo[size];
        }
    };
}
