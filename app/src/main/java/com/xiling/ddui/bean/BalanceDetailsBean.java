package com.xiling.ddui.bean;

import java.util.List;

/**
 * 余额明细
 */
public class BalanceDetailsBean {
    List<DataBean> datas;
    int pageOffset;
    int pageSize;
    int totalRecord;
    int totalPage;

    public List<DataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DataBean> datas) {
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

    public static class DataBean{
        String id;
        double changeValue;
        int type;
        String typeContent;
        String operatingDate;

        public DataBean(String id, double changeValue, int type, String typeContent, String operatingDate) {
            this.id = id;
            this.changeValue = changeValue;
            this.type = type;
            this.typeContent = typeContent;
            this.operatingDate = operatingDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getChangeValue() {
            return changeValue;
        }

        public void setChangeValue(double changeValue) {
            this.changeValue = changeValue;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeContent() {
            return typeContent;
        }

        public void setTypeContent(String typeContent) {
            this.typeContent = typeContent;
        }

        public String getOperatingDate() {
            return operatingDate;
        }

        public void setOperatingDate(String operatingDate) {
            this.operatingDate = operatingDate;
        }
    }
}
