package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;
import com.xiling.shared.Constants;
import com.xiling.shared.util.CarshReportUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-06-20
 */
public class Coupon implements Serializable {
    @SerializedName("couponId")
    public String couponId;
    @SerializedName("couponType")
    public int couponType;
    @SerializedName("title")
    public String title;
    @SerializedName("cost")
    public long amount;
    @SerializedName("minOrderMoney")
    public long minOrderMoney;
    @SerializedName("productId")
    public String productId;
    @SerializedName("limitStartDate")
    public Date limitStartDate;
    @SerializedName("limitEndDate")
    public Date limitEndDate;
    @SerializedName("iconUrl")
    public String thumb;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("receiveStatus")
    public boolean isReceived = Boolean.FALSE;
    @SerializedName("status")
    public boolean isUsed = Boolean.FALSE;
    public boolean isSelected = Boolean.FALSE;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coupon) {
            return ((Coupon) obj).couponId.equals(couponId);
        }
        return super.equals(obj);
    }

    public String getStatusText() {
        if (isUsed) {
            return "已使用，";
        }
        if (new Date().after(limitEndDate)) {
            return "已过期，";
        }
        return "";
    }

    public String getDateRange() {
        if (limitEndDate == null || limitStartDate== null) {
            return "";
        }
        String start = Constants.FORMAT_DATE_SIMPLE.format(limitStartDate);
        String end = Constants.FORMAT_DATE_SIMPLE.format(limitEndDate);
        return String.format("%s ~ %s", start, end);
    }

    public boolean couponCanUse() {
        if (limitEndDate == null || limitStartDate == null) {
            CarshReportUtils.post("优惠券数据异常" + couponId);
            return !isUsed;
        }
        Date now = new Date();
        return !(isUsed || now.after(limitEndDate));
    }

    public boolean userCanUse() {
        if (limitEndDate == null || limitStartDate == null) {
            CarshReportUtils.post("优惠券数据异常" + couponId);
            return false;
        }
        Date now = new Date();
        return !(isUsed || now.before(limitStartDate) || now.after(limitEndDate));
    }
}
