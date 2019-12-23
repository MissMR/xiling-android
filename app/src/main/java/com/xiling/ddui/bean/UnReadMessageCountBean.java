package com.xiling.ddui.bean;

import java.io.Serializable;

public class UnReadMessageCountBean implements Serializable {

    private int num = 0;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUnReadMessage() {
        if (num > 0) {
            if (num > 999) {
                return "999+";
            } else {
                return "" + num;
            }
        } else {
            return "";
        }
    }
}
