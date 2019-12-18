package com.xiling.ddmall.ddui.bean;

import java.io.Serializable;

public class QuestionBean implements Serializable {

    private int qId;
    private String qTitle = "";

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }

    public String getqTitle() {
        return qTitle;
    }

    public void setqTitle(String qTitle) {
        this.qTitle = qTitle;
    }

    @Override
    public String toString() {
        return qTitle;
    }
}
