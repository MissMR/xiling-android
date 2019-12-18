package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JayChan on 2017/2/22.
 */

public class PropertyValue {
    @SerializedName("propertyValueId")
    public String id;
    @SerializedName("propertyValue")
    public String value;

    @SerializedName("noSelectFlag")
    private int firstEnableSelect;

    public boolean enableFirstSelect() {
        return firstEnableSelect == 1;
    }

    // 是否可选
    public boolean enable = false;
    // 是否已选
    public boolean isSelected = false;

    public void reset() {
        setEnable(false);
        setSelected(false);
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
