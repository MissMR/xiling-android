package com.dodomall.ddmall.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/9/15
 * 返回结果是数组的映射
 */
public class ListResultBean<T> {

    /**
     * datas : [{"id":64696,"headImage":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83er9KtoF2P9UzLIO7oAL4iaoHRv1OdcHJATmN209nKMqd6fh3rnSezgPA6O4zibiaxgjAz0DI40oHicEbA/132","nickName":"荆汉青","roleName":"店主","roleLevel":5,"phone":"187****5967","createDate":"2018-09-07"}]
     * pageOffset : 1
     * pageSize : 10
     * totalRecord : 3
     * totalPage : 1
     * ex : {}
     */

    private int pageOffset;
    private int pageSize;
    private int totalRecord;
    private int totalPage;
    private Object ex;
    private List<T> datas;

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

    public Object getEx() {
        return ex;
    }

    public void setEx(Object ex) {
        this.ex = ex;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

}
