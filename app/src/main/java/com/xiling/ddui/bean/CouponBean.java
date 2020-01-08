package com.xiling.ddui.bean;

public class CouponBean {
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
    private int conditions;
    private boolean hasConditions;
    private int reducedPrice;
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

    public int getConditions() {
        return conditions;
    }

    public void setConditions(int conditions) {
        this.conditions = conditions;
    }

    public boolean isHasConditions() {
        return hasConditions;
    }

    public void setHasConditions(boolean hasConditions) {
        this.hasConditions = hasConditions;
    }

    public int getReducedPrice() {
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
}
