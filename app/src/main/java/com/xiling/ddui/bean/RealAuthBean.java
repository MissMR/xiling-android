package com.xiling.ddui.bean;

import java.util.List;

public class RealAuthBean {

    /**
     * memberId : 31ec4035098a4323bfea88ad86300796
     * authStatus : 2
     * identityCard :
     * idcardFrontImg : https://oss.xilingbm.com/auth/2020-04/2020041408205680986.jpg
     * idcardBackImg : https://oss.xilingbm.com/auth/2020-04/20200414082100615XX.jpg
     * authRemark :
     * checkRemark :
     * storeType : 1
     * storeName : 喜领
     * storeId :
     * storeAddress :
     * companyAddress : 无
     * businessLicense :
     * monthSales : 1
     * memberAuthStoreList : []
     * memberAuthCategoryList : []
     */

    private String memberId;
    private int authStatus;
    private String identityCard;
    private String idcardFrontImg;
    private String idcardBackImg;
    private String authRemark;
    private String checkRemark;
    private String storeType;
    private String storeName;
    private String storeId;
    private String storeAddress;
    private String companyAddress;
    private String businessLicense;
    private String monthSales;
    private List<?> memberAuthStoreList;
    private List<?> memberAuthCategoryList;

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

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(String monthSales) {
        this.monthSales = monthSales;
    }

    public List<?> getMemberAuthStoreList() {
        return memberAuthStoreList;
    }

    public void setMemberAuthStoreList(List<?> memberAuthStoreList) {
        this.memberAuthStoreList = memberAuthStoreList;
    }

    public List<?> getMemberAuthCategoryList() {
        return memberAuthCategoryList;
    }

    public void setMemberAuthCategoryList(List<?> memberAuthCategoryList) {
        this.memberAuthCategoryList = memberAuthCategoryList;
    }
}
