package com.xiling.ddmall.ddui.bean;

/**
 * created by Jigsaw at 2018/9/18
 */
public class WithdrawBean {

    /**
     * minWithdrawAmount : 100
     * bankImgUrl : http://img.zxyjsc.com/G1/M00/00/01/rBLh9lkPWb2AIt8BAAAjVmE65ms584.png
     * maxWithdrawAmount : 10000
     * safeProblem : 您的生日是？
     * balance : 99935000
     * appUrlPath : https://daohang.qq.com/?fr=hmpage
     * bankName : 中国农业银行
     * bankCardCode : 2312
     */

    private long minWithdrawAmount;
    private String bankImgUrl;
    private long maxWithdrawAmount;
    private String safeProblem;
    private long balance;
    private String appUrlPath;
    private String bankName;
    private String bankCardCode;
    private int toMiniProgram;

    // 是否可以全部提现
    public boolean checkCanAllWithDraw() {
        if (maxWithdrawAmount > 0) {
            return balance >= minWithdrawAmount && balance <= maxWithdrawAmount;
        }
        return balance >= minWithdrawAmount;
    }

    public int getToMiniProgram() {
        return toMiniProgram;
    }

    public void setToMiniProgram(int toMiniProgram) {
        this.toMiniProgram = toMiniProgram;
    }

    public void setMinWithdrawAmount(int minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public String getBankImgUrl() {
        return bankImgUrl;
    }

    public void setBankImgUrl(String bankImgUrl) {
        this.bankImgUrl = bankImgUrl;
    }


    public void setMaxWithdrawAmount(int maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public String getSafeProblem() {
        return safeProblem;
    }

    public void setSafeProblem(String safeProblem) {
        this.safeProblem = safeProblem;
    }


    public String getAppUrlPath() {
        return appUrlPath;
    }

    public void setAppUrlPath(String appUrlPath) {
        this.appUrlPath = appUrlPath;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardCode() {
        return bankCardCode;
    }

    public void setBankCardCode(String bankCardCode) {
        this.bankCardCode = bankCardCode;
    }

    public long getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(long minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public long getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(long maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
