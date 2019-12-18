package com.xiling.ddmall.ddui.bean;

import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.PhoneNumberUtil;

/**
 * created by Jigsaw at 2018/9/15
 */
public class AchievementRecordBean {

    /**
     * prizeName : 伯乐奖
     * rebatePhone : 15192509019
     * prize : 500000
     * calculationDate : 2018-08-30 17:54:26
     */

    private String prizeName;
    private String rebatePhone;
    private long prize;
    private String calculationDate;
    private String orderCode;
    private int prizeType;

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public void setPrize(long prize) {
        this.prize = prize;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSecretPhone() {
        return PhoneNumberUtil.getSecretPhoneNumber(rebatePhone);
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getRebatePhone() {
        return rebatePhone;
    }

    public void setRebatePhone(String rebatePhone) {
        this.rebatePhone = rebatePhone;
    }

    public long getPrize() {
        return prize;
    }

    public String getPriceStr() {
        return String.valueOf(ConvertUtil.cent2yuan(prize));
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public String getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(String calculationDate) {
        this.calculationDate = calculationDate;
    }
}
