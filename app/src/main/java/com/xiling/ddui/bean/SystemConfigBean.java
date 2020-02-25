package com.xiling.ddui.bean;

public class SystemConfigBean {
    /**
     * checkAuth : 1
     * payCardNumber : 3
     * minRecharge : 500
     */

    private String checkAuth;
    private int payCardNumber;
    private int minRecharge;

    public String getCheckAuth() {
        return checkAuth;
    }

    public void setCheckAuth(String checkAuth) {
        this.checkAuth = checkAuth;
    }

    public int getPayCardNumber() {
        return payCardNumber;
    }

    public void setPayCardNumber(int payCardNumber) {
        this.payCardNumber = payCardNumber;
    }

    public int getMinRecharge() {
        return minRecharge;
    }

    public void setMinRecharge(int minRecharge) {
        this.minRecharge = minRecharge;
    }
}
