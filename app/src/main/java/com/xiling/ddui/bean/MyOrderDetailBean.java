package com.xiling.ddui.bean;

import java.util.List;

public class MyOrderDetailBean {
    /**
     * size : 10
     * pages : 1
     * current : 1
     * total : 2
     * hasPre : false
     * hasNext : false
     * records : [{"orderId":"c1db744b253e4d919c046fd0de432692","orderCode":"0311578950267974","orderStatus":"待支付","orderStatusUs":"WAIT_PAY","details":[{"productId":"c3aedb9c06bd4672bea46e7f143df4c7","skuName":"测试产品2","productImage":"http://xiling-test.oss-cn-qingdao.aliyuncs.com/product/2020-01/20200103013222609Q7.png","productSpecification":"黑框黑灰片;,随机;","price":300,"retailPrice":300,"skuId":"66d574e4521843dba537936bfd68b22f","skuCode":"sku-3","quantity":1}],"totalQuantity":1,"totalPrice":300,"createTime":"2020-01-11 14:08:11","updateTime":"2020-01-11 14:08:11","canRemindDelivery":false,"contactUsername":"pt","contactPhone":"18354232734","contactProvince":"北京","contactCity":"北京市","contactDistrict":"东城区","contactDetail":"jxjdn","freight":1,"discountCoupon":0,"discountPrice":0,"taxes":0,"expressId":0,"expressCode":"","expressName":"","doneTime":"","shipDate":"","payType":"未支付","payMoney":301,"waitPayTimeMilli":1294914},{"orderId":"4220efd50ab64d278d5c939fdafd35a4","orderCode":"0311578950248875","orderStatus":"待支付","orderStatusUs":"WAIT_PAY","details":[{"productId":"c3aedb9c06bd4672bea46e7f143df4c7","skuName":"测试产品2","productImage":"http://xiling-test.oss-cn-qingdao.aliyuncs.com/product/2020-01/20200103013222609Q7.png","productSpecification":"黑框黑灰片;,随机;","price":300,"retailPrice":300,"skuId":"66d574e4521843dba537936bfd68b22f","skuCode":"sku-3","quantity":1}],"totalQuantity":1,"totalPrice":300,"createTime":"2020-01-11 14:07:52","updateTime":"2020-01-11 14:07:52","canRemindDelivery":false,"contactUsername":"pt","contactPhone":"18354232734","contactProvince":"北京","contactCity":"北京市","contactDistrict":"东城区","contactDetail":"jxjdn","freight":1,"discountCoupon":0,"discountPrice":0,"taxes":0,"expressId":0,"expressCode":"","expressName":"","doneTime":"","shipDate":"","payType":"未支付","payMoney":301,"waitPayTimeMilli":1275897}]
     * ex :
     * first : true
     * last : true
     */

    private int size;
    private int pages;
    private int current;
    private int total;
    private boolean hasPre;
    private boolean hasNext;
    private String ex;
    private boolean first;
    private boolean last;
    private List<XLOrderDetailsBean> records;

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

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public List<XLOrderDetailsBean> getRecords() {
        return records;
    }

    public void setRecords(List<XLOrderDetailsBean> records) {
        this.records = records;
    }


}
