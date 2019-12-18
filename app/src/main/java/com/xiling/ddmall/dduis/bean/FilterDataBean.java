package com.xiling.ddmall.dduis.bean;

import java.io.Serializable;

public class FilterDataBean implements Serializable {

    private String title = "";
    private String selectId = "";
    private boolean select = false;

    public FilterDataBean() {

    }

    public FilterDataBean(String title) {
        this.title = title;
    }


    public FilterDataBean(String selectId, String title) {
        this.selectId = selectId;
        this.title = title;
    }

    public FilterDataBean(String selectId, String title, boolean select) {
        this.selectId = selectId;
        this.title = title;
        this.select = select;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelectId() {
        return selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
