package com.xiling.ddui.bean;

public class BankListBean {
    String id; //银行卡列表使用id
    String bankId;//添加、删除银行卡使用bankId
    String bankLogo;
    int bankLogoGround;
    String bankName;
    String bankCardNumber;
    String bankBackground;
    String cardUser;

    public int getBankLogoGround() {
        return bankLogoGround;
    }

    public void setBankLogoGround(int bankLogoGround) {
        this.bankLogoGround = bankLogoGround;
    }


    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankBackground() {
        return bankBackground;
    }

    public void setBankBackground(String bankBackground) {
        this.bankBackground = bankBackground;
    }

    public String getCardUser() {
        return cardUser;
    }

    public void setCardUser(String cardUser) {
        this.cardUser = cardUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
}
