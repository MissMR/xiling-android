package com.xiling.ddui.bean;

import java.util.List;

public class CouponListBean {
    /**
     * size : 10
     * pages : 0
     * current : 1
     * total : 0
     * hasPre : false
     * hasNext : false
     * records : []
     * ex :
     * last : false
     * first : true
     */

    private int size;
    private int pages;
    private int current;
    private int total;
    private boolean hasPre;
    private boolean hasNext;
    private String ex;
    private boolean last;
    private boolean first;
    private List<CouponBean> records;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public List<CouponBean> getRecords() {
        return records;
    }

    public void setRecords(List<CouponBean> records) {
        this.records = records;
    }
}
