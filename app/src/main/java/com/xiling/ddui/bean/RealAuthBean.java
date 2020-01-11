package com.xiling.ddui.bean;

import java.util.List;

public class RealAuthBean {
    /**
     * memberId :
     * authStatus : 0
     * idType :
     * userName :
     * identityCard :
     * idcardFrontImg :
     * idcardBackImg :
     * idcardHeadImg :
     * authRemark :
     * checkRemark :
     * storeType :
     * storeName :
     * companyName :
     * businessLicense :
     * memberAuthStoreList :
     */

    private String memberId;
    private int authStatus;
    private String idType;
    private String userName;
    private String identityCard;
    private String idcardFrontImg;
    private String idcardBackImg;
    private String idcardHeadImg;
    private String authRemark;
    private String checkRemark;
    private String storeType;
    private String storeName;
    private String companyName;
    private String businessLicense;

    public List<MemberAuthStoreListBean> getMemberAuthStoreList() {
        return memberAuthStoreList;
    }

    public void setMemberAuthStoreList(List<MemberAuthStoreListBean> memberAuthStoreList) {
        this.memberAuthStoreList = memberAuthStoreList;
    }

    private List<MemberAuthStoreListBean> memberAuthStoreList;

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

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

   public class MemberAuthStoreListBean{
        String id;
        String memberId;
        String storeId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }
    }
}
