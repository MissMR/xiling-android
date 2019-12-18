package com.xiling.ddmall.ddui.bean;

import java.util.List;

/**
 * created by Jigsaw at 2018/12/22
 * 团队订单
 */
public class TeamOrderBean {

    /**
     * incId : amet
     * estimateDirectRebate : -7.922414691722137E7
     * skuDetail : [{"productImage":"esse ut ad dolore id","skuId":"ullam","quantity":-8.863210341553076E7,"retailPrice":3.5601153653814256E7,"skuName":"ut culpa dolore ipsum mollit"},{"productImage":"labore veniam","skuId":"dolor dolore","quantity":-8.151340884356916E7,"retailPrice":-6.909735635966459E7,"skuName":"nulla sed dolore aliquip"}]
     * orderCode : officia esse sed velit
     * settlementStatus : exercitation consequat adipisicing
     * settlementStatusStr : irure Ut
     */

    private String incId;
    private long estimateDirectRebate;
    private String orderCode;
    private int settlementStatus;
    private String settlementStatusStr;
    private List<TeamOrderSkuBean> skuDetail;

    public String getIncId() {
        return incId;
    }

    public void setIncId(String incId) {
        this.incId = incId;
    }

    public long getEstimateDirectRebate() {
        return estimateDirectRebate;
    }

    public void setEstimateDirectRebate(long estimateDirectRebate) {
        this.estimateDirectRebate = estimateDirectRebate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

}
