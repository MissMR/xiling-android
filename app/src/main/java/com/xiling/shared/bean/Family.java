package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class Family {

    /**
     * datas : [{"headImage":"http://img.kangerys.com/G1/M00/00/00/CqxOrVehYx2Ac2QBAAA1kHVRyx0171.jpg","nickName":"Geoffrey","phone":"15626204068","currMonthSaleMoney":0},{"headImage":"http://wx.qlogo.cn/mmopen/C2rEUskXQiblFYMUl9O0G05Q6pKibg7V1WpHX6CIQaic824apriabJw4r6EWxziaSt5BATrlbx1GVzwW2qjUCqtYpDvIJLjKgP1ug/0","nickName":"JayChan","phone":"13560463108","currMonthSaleMoney":0}]
     * pageOffset : 1
     * pageSize : 2
     * totalRecord : 17
     * totalPage : 9
     * ex : {}
     */

    @SerializedName("pageOffset")
    public int pageOffset;
    @SerializedName("pageSize")
    public int pageSize;
    @SerializedName("totalRecord")
    public int totalRecord;
    @SerializedName("totalPage")
    public int totalPage;
    @SerializedName("datas")
    public List<DatasEntity> datas;

    public static class DatasEntity {
        /**
         * headImage : http://img.kangerys.com/G1/M00/00/00/CqxOrVehYx2Ac2QBAAA1kHVRyx0171.jpg
         * nickName : Geoffrey
         * phone : 15626204068
         * currMonthSaleMoney : 0
         */

        @SerializedName("headImage")
        public String headImage;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("currMonthSaleMoney")
        public long currMonthSaleMoney;
        @SerializedName("memberType")
        public int memberType;
        @SerializedName("memberTypeStr")
        public String memberStr;
    }
}
