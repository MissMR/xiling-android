package com.xiling.ddui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品对象，用于生成确认订单数据
 */
public class SkuListBean implements Parcelable {
    String skuId;
    int quantity;

    public SkuListBean(String skuId, int quantity) {
        this.skuId = skuId;
        this.quantity = quantity;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.skuId);
        dest.writeInt(this.quantity);
    }

    protected SkuListBean(Parcel in) {
        this.skuId = in.readString();
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<SkuListBean> CREATOR = new Parcelable.Creator<SkuListBean>() {
        @Override
        public SkuListBean createFromParcel(Parcel source) {
            return new SkuListBean(source);
        }

        @Override
        public SkuListBean[] newArray(int size) {
            return new SkuListBean[size];
        }
    };
}
