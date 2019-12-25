package com.xiling.shared.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NewUserBean {

    /**
     * incId : 1
     * memberId : 123qweasdzxc
     * inviteCode : 88888888
     * headImage : http://xiling-test.oss-cn-qingdao.aliyuncs.com/app/2019-12/20191216090234327MP.jpg
     * phone : 13475323377
     * nickName : 一位路过的美食家
     * wechat :
     * wechatCode : http://oss.xiling.com/cabbage.jpg
     * wechatUnionId :
     * xdsWechatOpenId :
     * userName : 用户名
     * memberType : 0
     * memberTypeStr : 会员
     * authStatus : 2
     * authStatusStr :
     * roleId : 1
     * superiorIncId : 0
     * referrerMemberId :
     * createDate :
     * role : {"id":1,"roleName":"普通用户","roleLevel":10,"roleDiscount":100,"growValue":0,"weekCardPrice":""}
     * weekRoleId :
     */

    private int incId;
    private String memberId;
    private int inviteCode;
    private String headImage;
    private String phone;
    private String nickName;
    private String wechat;
    private String wechatCode;
    private String wechatUnionId;
    private String xdsWechatOpenId;
    private String userName;
    private int memberType;
    private String memberTypeStr;
    private int authStatus;
    private String authStatusStr;
    private int roleId;
    private int superiorIncId;
    private String referrerMemberId;
    private String createDate;
    private RoleBean role;
    private String weekRoleId;

    public int getIncId() {
        return incId;
    }

    public void setIncId(int incId) {
        this.incId = incId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(int inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWechatCode() {
        return wechatCode;
    }

    public void setWechatCode(String wechatCode) {
        this.wechatCode = wechatCode;
    }

    public String getWechatUnionId() {
        return wechatUnionId;
    }

    public void setWechatUnionId(String wechatUnionId) {
        this.wechatUnionId = wechatUnionId;
    }

    public String getXdsWechatOpenId() {
        return xdsWechatOpenId;
    }

    public void setXdsWechatOpenId(String xdsWechatOpenId) {
        this.xdsWechatOpenId = xdsWechatOpenId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public String getMemberTypeStr() {
        return memberTypeStr;
    }

    public void setMemberTypeStr(String memberTypeStr) {
        this.memberTypeStr = memberTypeStr;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthStatusStr() {
        return authStatusStr;
    }

    public void setAuthStatusStr(String authStatusStr) {
        this.authStatusStr = authStatusStr;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getSuperiorIncId() {
        return superiorIncId;
    }

    public void setSuperiorIncId(int superiorIncId) {
        this.superiorIncId = superiorIncId;
    }

    public String getReferrerMemberId() {
        return referrerMemberId;
    }

    public void setReferrerMemberId(String referrerMemberId) {
        this.referrerMemberId = referrerMemberId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public RoleBean getRole() {
        return role;
    }

    public void setRole(RoleBean role) {
        this.role = role;
    }

    public String getWeekRoleId() {
        return weekRoleId;
    }

    public void setWeekRoleId(String weekRoleId) {
        this.weekRoleId = weekRoleId;
    }

    public static class RoleBean {
        /**
         * id : 1
         * roleName : 普通用户
         * roleLevel : 10
         * roleDiscount : 100
         * growValue : 0.0
         * weekCardPrice :
         */

        private int id;
        private String roleName;
        private int roleLevel;
        private int roleDiscount;
        private double growValue;
        private String weekCardPrice;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public int getRoleLevel() {
            return roleLevel;
        }

        public void setRoleLevel(int roleLevel) {
            this.roleLevel = roleLevel;
        }

        public int getRoleDiscount() {
            return roleDiscount;
        }

        public void setRoleDiscount(int roleDiscount) {
            this.roleDiscount = roleDiscount;
        }

        public double getGrowValue() {
            return growValue;
        }

        public void setGrowValue(double growValue) {
            this.growValue = growValue;
        }

        public String getWeekCardPrice() {
            return weekCardPrice;
        }

        public void setWeekCardPrice(String weekCardPrice) {
            this.weekCardPrice = weekCardPrice;
        }

    }

}
