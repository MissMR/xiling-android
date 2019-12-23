package com.xiling.ddui.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DPosterBean implements Serializable {

    private ArrayList<PosterBean> list = new ArrayList<>();

    public ArrayList<PosterBean> getList() {
        return list;
    }

    public void setList(ArrayList<PosterBean> list) {
        this.list = list;
    }
}
