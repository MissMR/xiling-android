package com.xiling.shared.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-06-10
 */
public class Address implements Serializable {

    @SerializedName("addressId")
    public String addressId;
    @SerializedName("contact")
    public String contacts;
    @SerializedName("phone")
    public String phone;
    @SerializedName("provinceId")
    public String provinceId;
    @SerializedName("cityId")
    public String cityId;
    @SerializedName("districtId")
    public String districtId;
    @SerializedName("provinceName")
    public String provinceName;
    @SerializedName("cityName")
    public String cityName;
    @SerializedName("districtName")
    public String districtName;
    @SerializedName("detail")
    public String detail;
    @SerializedName("isDefault")
    public boolean isDefault = false;
    @SerializedName("identityCard")
    public String identityCard;

    public String getFormatName() {
        if (TextUtils.isEmpty(contacts)) {
            return "";
        }
        return contacts.length() > 7 ? contacts.substring(0, 7) + "..." : contacts;
    }

    public String getFullRegion() {
        return String.format("%s %s %s", provinceName, cityName, districtName);
    }

    public String getFullAddress() {
        return String.format("%s %s", getFullRegion(), detail);
    }

    public String getLotteryAddress() {
        return String.format("%s  %s  %s", contacts, phone, getFullAddress());
    }
}
