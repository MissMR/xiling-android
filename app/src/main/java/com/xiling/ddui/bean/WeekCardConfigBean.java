package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class WeekCardConfigBean implements Parcelable {
    /**
     * weekId : 1
     * weekType : 1
     * weekName : VIP会员周卡
     * weekRemark : 全场下单享受6.5折优惠价格
     * weekPrice : 1
     * expiredOpen : 1
     * expiredUnuse : 1
     * createDate : 2020-02-05 08:45:10
     * createUser : admin
     * updateDate : 2020-02-15 16:20:21
     * updateUser : admin
     */

    private int weekId;
    private int weekType;
    private String weekName;
    private String weekRemark;
    private int weekPrice;
    private int expiredOpen;
    private int expiredUnuse;
    private String createDate;
    private String createUser;
    private String updateDate;
    private String updateUser;

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

    public int getWeekPrice() {
        return weekPrice;
    }

    public void setWeekPrice(int weekPrice) {
        this.weekPrice = weekPrice;
    }

    public int getExpiredOpen() {
        return expiredOpen;
    }

    public void setExpiredOpen(int expiredOpen) {
        this.expiredOpen = expiredOpen;
    }

    public int getExpiredUnuse() {
        return expiredUnuse;
    }

    public void setExpiredUnuse(int expiredUnuse) {
        this.expiredUnuse = expiredUnuse;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.weekId);
        dest.writeInt(this.weekType);
        dest.writeString(this.weekName);
        dest.writeString(this.weekRemark);
        dest.writeInt(this.weekPrice);
        dest.writeInt(this.expiredOpen);
        dest.writeInt(this.expiredUnuse);
        dest.writeString(this.createDate);
        dest.writeString(this.createUser);
        dest.writeString(this.updateDate);
        dest.writeString(this.updateUser);
    }

    public WeekCardConfigBean() {
    }

    protected WeekCardConfigBean(Parcel in) {
        this.weekId = in.readInt();
        this.weekType = in.readInt();
        this.weekName = in.readString();
        this.weekRemark = in.readString();
        this.weekPrice = in.readInt();
        this.expiredOpen = in.readInt();
        this.expiredUnuse = in.readInt();
        this.createDate = in.readString();
        this.createUser = in.readString();
        this.updateDate = in.readString();
        this.updateUser = in.readString();
    }

    public static final Parcelable.Creator<WeekCardConfigBean> CREATOR = new Parcelable.Creator<WeekCardConfigBean>() {
        @Override
        public WeekCardConfigBean createFromParcel(Parcel source) {
            return new WeekCardConfigBean(source);
        }

        @Override
        public WeekCardConfigBean[] newArray(int size) {
            return new WeekCardConfigBean[size];
        }
    };
}
