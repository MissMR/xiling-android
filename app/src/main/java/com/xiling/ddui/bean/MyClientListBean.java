package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 我的客户列表对象
 */
public class MyClientListBean {

    int pageOffset;
    int pageSize;
    int totalRecord;
    int totalPage;
    private List<DataBean> datas;

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<DataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DataBean> datas) {
        this.datas = datas;
    }

    public static class DataBean implements Parcelable {
        String memberId;
        String createDate;
        String headImage;
        String memberName;
        double monthlyConsumption;
        String roleName;
        String level3Count;
        String level2Count;
        String level1Count;


        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public double getMonthlyConsumption() {
            return monthlyConsumption/100;
        }

        public void setMonthlyConsumption(double monthlyConsumption) {
            this.monthlyConsumption = monthlyConsumption;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getLevel3Count() {
            return level3Count;
        }

        public void setLevel3Count(String level3Count) {
            this.level3Count = level3Count;
        }

        public String getLevel2Count() {
            return level2Count;
        }

        public void setLevel2Count(String level2Count) {
            this.level2Count = level2Count;
        }

        public String getLevel1Count() {
            return level1Count;
        }

        public void setLevel1Count(String level1Count) {
            this.level1Count = level1Count;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.memberId);
            dest.writeString(this.createDate);
            dest.writeString(this.headImage);
            dest.writeString(this.memberName);
            dest.writeDouble(this.monthlyConsumption);
            dest.writeString(this.roleName);
            dest.writeString(this.level3Count);
            dest.writeString(this.level2Count);
            dest.writeString(this.level1Count);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.memberId = in.readString();
            this.createDate = in.readString();
            this.headImage = in.readString();
            this.memberName = in.readString();
            this.monthlyConsumption = in.readDouble();
            this.roleName = in.readString();
            this.level3Count = in.readString();
            this.level2Count = in.readString();
            this.level1Count = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

}
