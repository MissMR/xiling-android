package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CouponBean implements Parcelable {
    /**
     * id : 559
     * couponId : 1
     * name : 注册送券2599-260
     * start : 2019-12-01 00:00:00
     * end : 2020-12-01 00:00:00
     * effectiveTime : 30
     * limitEffectiveTime : true
     * getDate :
     * invalidDate : 2020-12-12 14:53:39
     * type : 3
     * typeName : 满减
     * conditions : 100
     * hasConditions : true
     * reducedPrice : 10
     * description : 注册送券2599-260*2
     */

    private String id;
    private int couponId;
    private String name;
    private String start;
    private String end;
    private int effectiveTime;
    private boolean limitEffectiveTime;
    private String getDate;
    private String invalidDate;
    private int type;
    private String typeName;
    private double conditions;
    private boolean hasConditions;
    private double reducedPrice;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public boolean isLimitEffectiveTime() {
        return limitEffectiveTime;
    }

    public void setLimitEffectiveTime(boolean limitEffectiveTime) {
        this.limitEffectiveTime = limitEffectiveTime;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getConditions() {
        return conditions;
    }

    public void setConditions(double conditions) {
        this.conditions = conditions;
    }

    public boolean isHasConditions() {
        return hasConditions;
    }

    public void setHasConditions(boolean hasConditions) {
        this.hasConditions = hasConditions;
    }

    public double getReducedPrice() {
        return reducedPrice;
    }

    public void setReducedPrice(int reducedPrice) {
        this.reducedPrice = reducedPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.couponId);
        dest.writeString(this.name);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeInt(this.effectiveTime);
        dest.writeByte(this.limitEffectiveTime ? (byte) 1 : (byte) 0);
        dest.writeString(this.getDate);
        dest.writeString(this.invalidDate);
        dest.writeInt(this.type);
        dest.writeString(this.typeName);
        dest.writeDouble(this.conditions);
        dest.writeByte(this.hasConditions ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.reducedPrice);
        dest.writeString(this.description);
    }

    public CouponBean() {
    }

    protected CouponBean(Parcel in) {
        this.id = in.readString();
        this.couponId = in.readInt();
        this.name = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.effectiveTime = in.readInt();
        this.limitEffectiveTime = in.readByte() != 0;
        this.getDate = in.readString();
        this.invalidDate = in.readString();
        this.type = in.readInt();
        this.typeName = in.readString();
        this.conditions = in.readDouble();
        this.hasConditions = in.readByte() != 0;
        this.reducedPrice = in.readDouble();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<CouponBean> CREATOR = new Parcelable.Creator<CouponBean>() {
        @Override
        public CouponBean createFromParcel(Parcel source) {
            return new CouponBean(source);
        }

        @Override
        public CouponBean[] newArray(int size) {
            return new CouponBean[size];
        }
    };
}
