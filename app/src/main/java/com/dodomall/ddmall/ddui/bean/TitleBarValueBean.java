package com.dodomall.ddmall.ddui.bean;

import java.io.Serializable;

public class TitleBarValueBean implements Serializable {

    public enum ValueType {
        BarAlpha,
        BarRes,
        /*右侧分享到微信按钮*/
        RightShareWXButton
    }

    private ValueType type;
    private int value;
    private float alpha;
    private boolean isDark;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isDark() {
        return isDark;
    }

    public void setDark(boolean dark) {
        isDark = dark;
    }
}
