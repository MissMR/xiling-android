package com.xiling.ddui.bean;

/**
 * created by Jigsaw at 2018/9/15
 * 提现记录
 */
public class CashWithdrawRecordBean {

    /**
     * bank : 中国农业银行
     * bankCardNo : 6217002390011132312
     * withdrawalNumber : DRAW20180913011639983UJ
     * withdrawalAmount : 1000
     * afterWithdrawalBlance : 99951000
     * auditStatusStr : 等待审核
     * applyTime : 2018-09-13 09:17:03
     * maker :
     * makeTime :
     * auditRemark 审核备注 页面备注展示两个都展示
     * makeRemark 打款备注 页面备注展示两个都展示
     * bank 银行名称
     * bankCardNo 银行卡号
     * withdrawalNumber 提现流水号
     * withdrawalAmount 提现金额
     * afterWithdrawalBlance 提现后剩余的可用金额
     * auditStatusStr 提现状态
     * applyTime 申请时间
     * makeTime 可能为null
     */
    private String id;
    private String bank;
    private String bankCardNo;
    private String withdrawalNumber;
    private int withdrawalAmount;
    private int afterWithdrawalBlance;
    private String auditStatusStr;
    private String applyTime;
    private String makeTime;

    // 详情页的字段
    private String auditRemark;
    private String makeRemark;

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getMakeRemark() {
        return makeRemark;
    }

    public void setMakeRemark(String makeRemark) {
        this.makeRemark = makeRemark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getWithdrawalNumber() {
        return withdrawalNumber;
    }

    public void setWithdrawalNumber(String withdrawalNumber) {
        this.withdrawalNumber = withdrawalNumber;
    }

    public int getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(int withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public int getAfterWithdrawalBlance() {
        return afterWithdrawalBlance;
    }

    public void setAfterWithdrawalBlance(int afterWithdrawalBlance) {
        this.afterWithdrawalBlance = afterWithdrawalBlance;
    }

    public String getAuditStatusStr() {
        return auditStatusStr;
    }

    public void setAuditStatusStr(String auditStatusStr) {
        this.auditStatusStr = auditStatusStr;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }
}
