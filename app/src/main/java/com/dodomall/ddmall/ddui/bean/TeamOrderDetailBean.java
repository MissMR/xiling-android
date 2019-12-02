package com.dodomall.ddmall.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/12/22
 * 团队订单详情
 */
public class TeamOrderDetailBean {

    /**
     * incId : -5.140526540774401E7
     * nickName : esse magna
     * headImage : fugiat adipisicing pariatur eu
     * estimateDirectRebate : -6.474843304974369E7
     * actualDirectRebate : -2.5286236573656946E7
     * skuDetail : [{"productImage":"velit ipsum qui","skuId":"deserunt officia","quantity":1.1912589152930275E7,"retailPrice":4.1040129320774645E7,"properties":"adipisicing sed aliqua fugiat","skuName":"mollit fugiat labore est proident"},{"productImage":"veniam incididunt","skuId":"ipsum nisi elit","quantity":-8.484156396574591E7,"retailPrice":5.029589390863061E7,"properties":"irure nisi Duis","skuName":"in reprehenderit laborum aliqua"},{"productImage":"in sit sed cillum in","skuId":"minim incididunt Excepteur non","quantity":1.0646067533603936E7,"retailPrice":-7.400461127099463E7,"properties":"eiusmod aliqua culpa minim labore","skuName":"anim ut r"}]
     * orderCode : amet in
     * orderDate : mollit laborum
     * payDate : nostrud Duis Ut
     * settlementStatusStr : Lorem ipsum ut et
     */

    private String incId;
    private String nickName;
    private String headImage;
    private long estimateDirectRebate;
    private long actualDirectRebate;
    private String orderCode;
    private String orderDate;
    private String payDate;
    private String settlementStatusStr;
    private int settlementStatus;
    private List<TeamOrderSkuBean> skuDetail;
    private String text;

    public String getIncId() {
        return incId;
    }

    public void setIncId(String incId) {
        this.incId = incId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public long getEstimateDirectRebate() {
        return estimateDirectRebate;
    }

    public void setEstimateDirectRebate(long estimateDirectRebate) {
        this.estimateDirectRebate = estimateDirectRebate;
    }

    public long getActualDirectRebate() {
        return actualDirectRebate;
    }

    public void setActualDirectRebate(long actualDirectRebate) {
        this.actualDirectRebate = actualDirectRebate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public int getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(int settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getSettlementStatusStr() {
        return settlementStatusStr;
    }

    public void setSettlementStatusStr(String settlementStatusStr) {
        this.settlementStatusStr = settlementStatusStr;
    }

    public List<TeamOrderSkuBean> getSkuDetail() {
        return skuDetail;
    }

    public void setSkuDetail(List<TeamOrderSkuBean> skuDetail) {
        this.skuDetail = skuDetail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
