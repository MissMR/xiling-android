package com.xiling.ddui.bean;

/**
 * 商品详情，产品参数
 */
public class ProductParameterBean {
    /**
     * id : 17
     * sort : 0
     * parameterName : 测试参数1
     * parameterValue : 参数内容123123123123123
     * productId : 30ebb4142bec4b0c92ed944c1952dacf
     * createTime : 2020-04-10 19:36:03
     */

    private int id;
    private int sort;
    private String parameterName;
    private String parameterValue;
    private String productId;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
