package com.xiling.ddui.bean;

import java.io.Serializable;

/**
 * created by Jigsaw at 2018/9/17
 */
public class UserAuthBean implements Serializable {

    /**
     * memberId : 553cc45fe09843ca986f32aa8fc79117
     * authStatus : 2
     * idType : 0
     * userName : 王国丰
     * identityCard : 410521198707038013
     * idcardFrontImg : https://oss.dodomall.com/app/2018-09/20180910152239-1536564180839650.jpg
     * idcardBackImg : https://oss.dodomall.com/app/2018-09/20180910133945-1536558007267986.jpg
     * idcardHeadImg :
     * authRemark :
     * checkRemark : 测试通过
     * safeKeyFlag : 1
     * safeKeyStr : 已经设置
     * safeTypeStr : 您父亲的姓名是？
     * cashPasswordFlag : 1
     * cashPasswordStr : 已经设置
     * bankId : 2
     * bankName : 中国农业银行
     * bankLogo : http://img.zxyjsc.com/G1/M00/00/01/rBLh9lkPWb2AIt8BAAAjVmE65ms584.png
     * bankAccount : 6217002390011139760
     * bankcardAddress : 青岛市市南区海航万邦支行
     * bankAccountFlag : 1
     * bankAccountStr : 已经设置
     */

    private String memberId;
    private int authStatus;
    private int idType;
    private String userName;
    private String identityCard;
    private String idcardFrontImg;
    private String idcardBackImg;
    private String idcardHeadImg;
    private String authRemark;
    private String checkRemark;
    private int safeKeyFlag;
    private String safeKeyStr;
    private String safeTypeStr;
    private int cashPasswordFlag;
    private String cashPasswordStr;
    private int bankId;
    private String bankName;
    private String bankLogo;
    private String bankAccount;
    private String bankcardAddress;
    private int bankAccountFlag;
    private String bankAccountStr;

    // 是否可提现  实名认证&绑定银行卡&设置交易密码&设置安全问题
    public boolean canCashWithdraw() {
        return isPassedAuth() && isBindedBankCard() && isSetTradePassword() && isSetSecurityQuestion();
    }

    // 是否绑定银行卡
    public boolean isBindedBankCard() {
        return bankAccountFlag == 1;
    }

    // 是否实名认证
    public boolean isPassedAuth() {
        return this.authStatus == 2;
    }

    // 是否设置安全问题
    public boolean isSetSecurityQuestion() {
        return this.safeKeyFlag == 1;
    }

    // 是否设置交易密码
    public boolean isSetTradePassword() {
        return this.cashPasswordFlag == 1;
    }


    public String getSecretIdentityCardNumber() {
        StringBuilder stringBuilder = new StringBuilder(identityCard.substring(0, 6));
        stringBuilder.append("********");
        stringBuilder.append(identityCard.substring(identityCard.length() - 4));
        return stringBuilder.toString();
    }

    public String getSecretBankCardNumber() {
        return String.format("**** **** **** **** %s", bankAccount.substring(bankAccount.length() - 4));
    }

    public String getSecretUserName() {

        if (userName.length() <= 1) {
            return "*";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < userName.length() - 1; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.append(userName.charAt(userName.length() - 1)).toString();

    }

    /**
     * 1，实名认证：若未认证，显示：未认证（字体红色显示）
     * 若已认证，显示规则：只显示名字最后一个字，如**璐；
     * 若审核失败，显示：审核失败（字体红色显示）
     *
     * @return
     */
    public String getAuthStr() {
        return getAuthStr(this.authStatus);
    }

    public static String getAuthStr(int authStatus) {
        String result;
        switch (authStatus) {
            case 0:
                // 未提交
                result = "未认证";
                break;
            case 1:
                // 审核中
                result = "等待审核";
                break;
            case 2:
                // 已认证
                result = "已认证";
                break;
            default:
                result = "审核失败";
        }
        return result;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getIdcardFrontImg() {
        return idcardFrontImg;
    }

    public void setIdcardFrontImg(String idcardFrontImg) {
        this.idcardFrontImg = idcardFrontImg;
    }

    public String getIdcardBackImg() {
        return idcardBackImg;
    }

    public void setIdcardBackImg(String idcardBackImg) {
        this.idcardBackImg = idcardBackImg;
    }

    public String getIdcardHeadImg() {
        return idcardHeadImg;
    }

    public void setIdcardHeadImg(String idcardHeadImg) {
        this.idcardHeadImg = idcardHeadImg;
    }

    public String getAuthRemark() {
        return authRemark;
    }

    public void setAuthRemark(String authRemark) {
        this.authRemark = authRemark;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }

    public int getSafeKeyFlag() {
        return safeKeyFlag;
    }

    public void setSafeKeyFlag(int safeKeyFlag) {
        this.safeKeyFlag = safeKeyFlag;
    }

    public String getSafeKeyStr() {
        return safeKeyStr;
    }

    public void setSafeKeyStr(String safeKeyStr) {
        this.safeKeyStr = safeKeyStr;
    }

    public String getSafeTypeStr() {
        return safeTypeStr;
    }

    public void setSafeTypeStr(String safeTypeStr) {
        this.safeTypeStr = safeTypeStr;
    }

    public int getCashPasswordFlag() {
        return cashPasswordFlag;
    }

    public void setCashPasswordFlag(int cashPasswordFlag) {
        this.cashPasswordFlag = cashPasswordFlag;
    }

    public String getCashPasswordStr() {
        return cashPasswordStr;
    }

    public void setCashPasswordStr(String cashPasswordStr) {
        this.cashPasswordStr = cashPasswordStr;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankcardAddress() {
        return bankcardAddress;
    }

    public void setBankcardAddress(String bankcardAddress) {
        this.bankcardAddress = bankcardAddress;
    }

    public int getBankAccountFlag() {
        return bankAccountFlag;
    }

    public void setBankAccountFlag(int bankAccountFlag) {
        this.bankAccountFlag = bankAccountFlag;
    }

    public String getBankAccountStr() {
        return bankAccountStr;
    }

    public void setBankAccountStr(String bankAccountStr) {
        this.bankAccountStr = bankAccountStr;
    }
}
