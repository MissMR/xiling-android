package com.xiling.ddui.bean;

public class MemberCenterInfo {
    /**
     * memberId : e2562d55af584ca0ae54146076a3ed15
     * headImage : http://thirdwx.qlogo.cn/mmopen/vi_32/H4VAG1DFhicZyg1cicT9gXIQlzFvibR3Atd0kM9ibJqPN8ZtFv85Eecejxqdq182xYnfZnygFlXRFQTsAAfVZiaMOkQ/132
     * nickName : 逄涛
     * memberRole : {"id":3,"roleName":"黑卡会员","roleLevel":30,"roleDiscount":55,"growValue":1000,"weekCardPrice":75000}
     * nextMemberRole :{}
     * growValue : 9301.0
     * growValueTotle : 9301.0
     * couponCount : 0
     * couponDate :
     * weekCard : {"cardName":"VIP卡","cardType":1,"cardPrice":45000}
     */

    private String memberId;
    private String headImage;
    private String nickName;
    private MemberRoleBean memberRole;
    private MemberRoleBean nextMemberRole;
    private double growValue;
    private double growValueTotle;
    private int couponCount;
    private String couponDate;
    private WeekCardBean weekCard;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public MemberRoleBean getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRoleBean memberRole) {
        this.memberRole = memberRole;
    }

    public MemberRoleBean getNextMemberRole() {
        return nextMemberRole;
    }

    public void setNextMemberRole(MemberRoleBean nextMemberRole) {
        this.nextMemberRole = nextMemberRole;
    }

    public double getGrowValue() {
        return growValue;
    }

    public void setGrowValue(double growValue) {
        this.growValue = growValue;
    }

    public double getGrowValueTotle() {
        return growValueTotle;
    }

    public void setGrowValueTotle(double growValueTotle) {
        this.growValueTotle = growValueTotle;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public String getCouponDate() {
        return couponDate;
    }

    public void setCouponDate(String couponDate) {
        this.couponDate = couponDate;
    }

    public WeekCardBean getWeekCard() {
        return weekCard;
    }

    public void setWeekCard(WeekCardBean weekCard) {
        this.weekCard = weekCard;
    }

    public static class MemberRoleBean {
        /**
         * id : 3
         * roleName : 黑卡会员
         * roleLevel : 30
         * roleDiscount : 55
         * growValue : 1000.0
         * weekCardPrice : 75000
         */

        private String id;
        private String roleName;
        private String roleLevel;
        private String roleDiscount;
        private String growValue;
        private String weekCardPrice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleLevel() {
            return roleLevel;
        }

        public void setRoleLevel(String roleLevel) {
            this.roleLevel = roleLevel;
        }

        public String getRoleDiscount() {
            return roleDiscount;
        }

        public void setRoleDiscount(String roleDiscount) {
            this.roleDiscount = roleDiscount;
        }

        public String getGrowValue() {
            return growValue;
        }

        public void setGrowValue(String growValue) {
            this.growValue = growValue;
        }

        public String getWeekCardPrice() {
            return weekCardPrice;
        }

        public void setWeekCardPrice(String weekCardPrice) {
            this.weekCardPrice = weekCardPrice;
        }
    }

    public static class WeekCardBean {
        /**
         * cardName : VIP卡
         * cardType : 1
         * cardPrice : 45000
         */

        private String cardName;
        private int cardType;
        private int cardPrice;

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        public int getCardPrice() {
            return cardPrice;
        }

        public void setCardPrice(int cardPrice) {
            this.cardPrice = cardPrice;
        }
    }
}
