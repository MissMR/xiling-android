package com.xiling.dduis.bean;

import java.io.Serializable;

public class DDCartFlashSaleBean implements Serializable {

    //佣金
    private long brokerage = 0;
    //活动佣金
    private long activityBrokerage = 0;
    //活动库存
    private long inventory = 0;

    //销量
    private long saleCount = 0;
    //显示抢购状态
    private int flashSaleStatusValue = 0;
    //spu - 库存
    private long spuInventory = 0;
    //spu最大商品零售价
    private long maxSalePrice = 0;
    //活动价
    private long activityPrice = 0;

    //活动状态
    private int status = 0;
    //活动开始时间
    private long startTime = 0;
    //活动结束时间
    private long endTime = 0;
    //是否上线
    private String online = "";

    //活动名字
    private String name = "";

    public long getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(long brokerage) {
        this.brokerage = brokerage;
    }

    public long getActivityBrokerage() {
        return activityBrokerage;
    }

    public void setActivityBrokerage(long activityBrokerage) {
        this.activityBrokerage = activityBrokerage;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }

    public long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(long saleCount) {
        this.saleCount = saleCount;
    }

    public int getFlashSaleStatusValue() {
        return flashSaleStatusValue;
    }

    public void setFlashSaleStatusValue(int flashSaleStatusValue) {
        this.flashSaleStatusValue = flashSaleStatusValue;
    }

    public long getSpuInventory() {
        return spuInventory;
    }

    public void setSpuInventory(long spuInventory) {
        this.spuInventory = spuInventory;
    }

    public long getMaxSalePrice() {
        return maxSalePrice;
    }

    public void setMaxSalePrice(long maxSalePrice) {
        this.maxSalePrice = maxSalePrice;
    }

    public long getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(long activityPrice) {
        this.activityPrice = activityPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
