package com.xiling.ddui.bean;

import android.support.annotation.Nullable;

public class PlatformBean {
    /**
     * storeId : 101
     * storeName : 天猫
     * storeDescription :
     */

    private String storeId;
    private String storeName;
    private String storeDescription;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    boolean isSelect = false;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.getStoreId().equals(((PlatformBean)obj).getStoreId());
    }
}
