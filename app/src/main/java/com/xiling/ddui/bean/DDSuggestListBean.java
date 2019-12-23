package com.xiling.ddui.bean;

import com.xiling.dduis.bean.DDProductBean;

import java.io.Serializable;
import java.util.ArrayList;

public class DDSuggestListBean implements Serializable {

    private ArrayList<DDProductBean> datas = new ArrayList<>();
    private int totalPage = 0;
    private int totalRecord = 0;
    private int pageOffset = 0;
    private int pageSize = 0;

    public ArrayList<DDProductBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<DDProductBean> datas) {
        this.datas = datas;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
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
}
