package com.dodomall.ddmall.ddui.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DQuestionBean implements Serializable {

    private ArrayList<QuestionBean> list = new ArrayList<>();

    public ArrayList<QuestionBean> getList() {
        return list;
    }

    public void setList(ArrayList<QuestionBean> list) {
        this.list = list;
    }
}
