package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 个人资产
 */
public class UserInComeBean implements Parcelable {
    /**
     * memberId : 123qweasdzxc
     * roleId : 3
     * inviteCode : 88888888
     * headImage : http://xiling-test.oss-cn-qingdao.aliyuncs.com/app/2020-01/20200103074745711KA.
     * nickName : 一位老湿
     * phone : 13475323377
     * incomeDay : 0
     * incomeMonth : 0
     * incomeTotal : 10
     * balanceGrow : 0
     */

    private String memberId;
    private int roleId;
    private String inviteCode;
    private String headImage;
    private String nickName;
    private String phone;
    private double incomeDay;
    private double incomeMonth;
    private double incomeTotal;
    private double balanceGrow;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getIncomeDay() {
        return incomeDay;
    }

    public void setIncomeDay(double incomeDay) {
        this.incomeDay = incomeDay;
    }

    public double getIncomeMonth() {
        return incomeMonth;
    }

    public void setIncomeMonth(double incomeMonth) {
        this.incomeMonth = incomeMonth;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public double getBalanceGrow() {
        return balanceGrow;
    }

    public void setBalanceGrow(double balanceGrow) {
        this.balanceGrow = balanceGrow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberId);
        dest.writeInt(this.roleId);
        dest.writeString(this.inviteCode);
        dest.writeString(this.headImage);
        dest.writeString(this.nickName);
        dest.writeString(this.phone);
        dest.writeDouble(this.incomeDay);
        dest.writeDouble(this.incomeMonth);
        dest.writeDouble(this.incomeTotal);
        dest.writeDouble(this.balanceGrow);
    }

    public UserInComeBean() {
    }

    protected UserInComeBean(Parcel in) {
        this.memberId = in.readString();
        this.roleId = in.readInt();
        this.inviteCode = in.readString();
        this.headImage = in.readString();
        this.nickName = in.readString();
        this.phone = in.readString();
        this.incomeDay = in.readDouble();
        this.incomeMonth = in.readDouble();
        this.incomeTotal = in.readDouble();
        this.balanceGrow = in.readDouble();
    }

    public static final Parcelable.Creator<UserInComeBean> CREATOR = new Parcelable.Creator<UserInComeBean>() {
        @Override
        public UserInComeBean createFromParcel(Parcel source) {
            return new UserInComeBean(source);
        }

        @Override
        public UserInComeBean[] newArray(int size) {
            return new UserInComeBean[size];
        }
    };
}
