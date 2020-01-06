package com.xiling.ddui.bean;

/**
 * 个人资产
 */
public class UserInComeBean {
    /**
     * memberId : 123qweasdzxc
     * roleId : 3
     * inviteCode : 88888888
     * headImage : http://xiling-test.oss-cn-qingdao.aliyuncs.com/app/2020-01/20200103074745711KA.
     * nickName : 一位老湿
     * phone : 13475323377
     * incomeDay : 0
     * incomeMonth : 0
     * incomeTotal : 10
     * balanceGrow : 0
     */

    private String memberId;
    private int roleId;
    private String inviteCode;
    private String headImage;
    private String nickName;
    private String phone;
    private double incomeDay;
    private double incomeMonth;
    private double incomeTotal;
    private double balanceGrow;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getIncomeDay() {
        return incomeDay;
    }

    public void setIncomeDay(double incomeDay) {
        this.incomeDay = incomeDay;
    }

    public double getIncomeMonth() {
        return incomeMonth;
    }

    public void setIncomeMonth(double incomeMonth) {
        this.incomeMonth = incomeMonth;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public double getBalanceGrow() {
        return balanceGrow;
    }

    public void setBalanceGrow(double balanceGrow) {
        this.balanceGrow = balanceGrow;
    }
}
