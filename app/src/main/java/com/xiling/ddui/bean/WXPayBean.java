package com.xiling.ddui.bean;

public class WXPayBean {

    /**
     * sign : D8FC71881A452E33CFEE7D139CCCEA0C
     * prepayId : wx21094916544425b2cb7538171712799300
     * partnerId : 1568042081
     * appId : wxf10b84ba54f558d5
     * packageValue : Sign=WXPay
     * timeStamp : 1582249756
     * nonceStr : 1582249756577
     */

    private String sign;
    private String prepayId;
    private String partnerId;
    private String appId;
    private String packageValue;
    private String timeStamp;
    private String nonceStr;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
}
