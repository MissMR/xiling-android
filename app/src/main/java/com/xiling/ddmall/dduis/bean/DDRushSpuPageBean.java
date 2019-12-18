package com.xiling.ddmall.dduis.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DDRushSpuPageBean implements Serializable {

    ArrayList<DDRushSpuBean> datas = new ArrayList<>();
    int pagetOffset = 0;
    int pageSize = 0;
    int totalRecord = 0;
    int totalPage = 0;

    public ArrayList<DDRushSpuBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<DDRushSpuBean> datas) {
        this.datas = datas;
    }

    public int getPagetOffset() {
        return pagetOffset;
    }

    public void setPagetOffset(int pagetOffset) {
        this.pagetOffset = pagetOffset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
