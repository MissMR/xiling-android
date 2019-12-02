package com.dodomall.ddmall.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/8/31
 */
public class HomePageBean {

    /**
     * datas : [{"skuId":"d9031f4dfc574d05817f3d7d780bf1ea","productId":"c8db8d84f5434955a96623f472cbac0b","skuName":"ceshi ","intro":"","stock":11111111,"status":0,"saleCount":0,"totalSaleCount":0,"retailPrice":222,"marketPrice":500,"thumbUrl":"http://192.168.1.161/group1/M00/00/01/wKgBoVt8DDKAVM85AAB609SIVRc284.jpg","tags":[],"currentVipTypePrice":0,"produtUrl":""},{"skuId":"9122c525af4443808a12bd418d6ec081","productId":"357fc1c5ebe4460389f5cf3165ca4ce8","skuName":"小膜漾399开店礼包","intro":"","stock":99855,"status":0,"saleCount":1043,"totalSaleCount":0,"retailPrice":1,"marketPrice":71920,"thumbUrl":"http://hhimg.weijuit.com/G1/M00/00/02/rBLd9VtkATWAX1c3AABsa72nnu4408.jpg","tags":[],"currentVipTypePrice":0,"produtUrl":""},{"skuId":"eaeaa868ba9f40908eae73ec30622fd3","productId":"9eec0b4692214a14bf50b7476ba48a10","skuName":"店主礼包22","intro":"12121212121212121212121212121212","stock":7569,"status":0,"saleCount":6,"totalSaleCount":0,"retailPrice":111100,"marketPrice":222200,"thumbUrl":"http://192.168.1.161/group1/M00/00/01/wKgBoVt8DWSAJ4wbAABDOQeuy8k584.jpg","tags":[],"currentVipTypePrice":0,"produtUrl":""},{"skuId":"a81afad4a2db48bc85b3155fe25d97d0","productId":"656dd4c8f1164f5bb4bad4be59c328fc","skuName":"测试OSS上传图片","intro":"123测试OSS上传图片测试OSS上传图片测试OSS上传图片","stock":0,"status":0,"saleCount":0,"totalSaleCount":0,"retailPrice":11100,"marketPrice":123200,"thumbUrl":"https://ddmal-web-test.oss-cn-beijing.aliyuncs.com/product/2018-08/20180828201533-1.jpg","tags":[],"currentVipTypePrice":0,"produtUrl":""}]
     * pageOffset : 1
     * pageSize : 99
     * totalRecord : 4
     * totalPage : 1
     * ex : {}
     */

    private int pageOffset;
    private int pageSize;
    private int totalRecord;
    private int totalPage;
    private ExBean ex;
    private List<ProductBean> datas;

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

    public ExBean getEx() {
        return ex;
    }

    public void setEx(ExBean ex) {
        this.ex = ex;
    }

    public List<ProductBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ProductBean> datas) {
        this.datas = datas;
    }

    public static class ExBean {
    }


}
