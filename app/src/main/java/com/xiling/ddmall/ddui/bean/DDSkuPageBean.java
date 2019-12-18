package com.xiling.ddmall.ddui.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DDSkuPageBean implements Serializable {

    private ArrayList<ProductBean> datas = new ArrayList<>();
    private int pageOffset;
    private int pageSize;
    private int totalRecord;
    private int totalPage;

    public ArrayList<ProductBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<ProductBean> datas) {
        this.datas = datas;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
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
