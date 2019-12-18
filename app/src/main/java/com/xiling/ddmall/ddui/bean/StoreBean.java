package com.xiling.ddmall.ddui.bean;

/**
 * created by Jigsaw at 2019/3/26
 */
public class StoreBean {
    /**
     * memberIncId : 8.295019968229198E7
     * nickName : in enim au
     * headImage : quis consequat labore
     * announcement : non officia
     * todayVisitNumber : 6.493632308301294E7
     * totalVisitNumber : 8.990276310484114E7
     * myShopNumber : null
     */

    private String memberIncId;
    private String nickName;
    private String headImage;
    private String announcement;
    private String todayVisitNumber;
    private String totalVisitNumber;

    private int myShopNumber;
    private String maxShopNumber;
    private boolean pokedFlag;

    public boolean isPokedFlag() {
        return pokedFlag;
    }

    public void setPokedFlag(boolean pokedFlag) {
        this.pokedFlag = pokedFlag;
    }

    public int getMyShopNumber() {
        return myShopNumber;
    }

    public void setMyShopNumber(int myShopNumber) {
        this.myShopNumber = myShopNumber;
    }

    public String getMaxShopNumber() {
        return maxShopNumber;
    }

    public void setMaxShopNumber(String maxShopNumber) {
        this.maxShopNumber = maxShopNumber;
    }

    public String getMemberIncId() {
        return memberIncId;
    }

    public void setMemberIncId(String memberIncId) {
        this.memberIncId = memberIncId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getTodayVisitNumber() {
        return todayVisitNumber;
    }

    public void setTodayVisitNumber(String todayVisitNumber) {
        this.todayVisitNumber = todayVisitNumber;
    }

    public String getTotalVisitNumber() {
        return totalVisitNumber;
    }

    public void setTotalVisitNumber(String totalVisitNumber) {
        this.totalVisitNumber = totalVisitNumber;
    }
}
