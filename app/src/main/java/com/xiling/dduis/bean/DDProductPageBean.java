package com.xiling.dduis.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DDProductPageBean implements Serializable {

    private ArrayList<DDProductBean> datas = new ArrayList<>();
    private long totalRecord = 0;
    private long totalPage = 0;
    private long pageOffset = 0;
    private long pageSize = 0;

    public ArrayList<DDProductBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<DDProductBean> datas) {
        this.datas = datas;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(long pageOffset) {
        this.pageOffset = pageOffset;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
