package com.xiling.ddmall.shared.bean;

import com.blankj.utilcode.utils.StringUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/10/13.
 */
public class LotteryWinner implements Serializable {

    /**
     * drawId : 2004733d4fd74dea95a26b8601d19185
     * status : 1
     * statusStr : 待领取
     * useNum : 1
     * prizeName : 6
     * prizeImg : 图片
     * prizeType : 1
     * acceptPrizeDate :
     * contact :
     * phone :
     * address :
     * expressName :
     * expressCode :
     * createDate : 2017-10-11 14:41:39
     */

    @SerializedName("drawId")
    public String drawId;
    /**
     * 1 待领取
     * 2 已领取
     * 4已过期
     */
    @SerializedName("status")
    public int status;
    @SerializedName("statusStr")
    public String statusStr;
    @SerializedName("useNum")
    public int useNum;
    @SerializedName("prizeName")
    public String prizeName;
    @SerializedName("prizeImg")
    public String prizeImg;
    @SerializedName("prizeType")
    public int prizeType;
    @SerializedName("acceptPrizeDate")
    public String acceptPrizeDate;
    @SerializedName("contact")
    public String contact;
    @SerializedName("phone")
    public String phone;
    @SerializedName("address")
    public String address;
    @SerializedName("expressName")
    public String expressName;
    @SerializedName("expressCode")
    public String expressCode;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("intro")
    public String intro;


    /**
     * province : 山西省
     * city : 长治市
     * district : 平顺县
     * detail : 测试收货地址
     */

    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("district")
    public String district;
    @SerializedName("detail")
    public String detail;

    public String getLotteryAddress() {
        if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(contact)) {
            return "无";
        }else {
            return String.format("%s  %s  %s%s%s%s",contact,phone,province,city,district,detail);
        }
    }
}
