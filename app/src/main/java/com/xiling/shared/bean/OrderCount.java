package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCount {

    private int orderStatus;
    private int orderCount;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
