package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class FamilyOrder {

    /**
     * datas : [{"headImage":"http://img.kangerys.com/G1/M00/00/00/CqxOrVehYx2Ac2QBAAA1kHVRyx0171.jpg","nickName":"Geoffrey","phone":"15626204068","orderCode":"1701487553179148","payMoney":44100,"payDate":"2017-02-22 14:37:42"},{"headImage":"http://img.kangerys.com/G1/M00/00/00/CqxOrVehYx2Ac2QBAAA1kHVRyx0171.jpg","nickName":"Geoffrey","phone":"15626204068","orderCode":"1701487723260064","payMoney":25600,"payDate":"2017-02-22 13:18:47"}]
     * pageOffset : 1
     * pageSize : 2
     * totalRecord : 11
     * totalPage : 6
     * ex : {"totalMoney":453800}
     */

    @SerializedName("pageOffset")
    public int pageOffset;
    @SerializedName("pageSize")
    public int pageSize;
    @SerializedName("totalRecord")
    public int totalRecord;
    @SerializedName("totalPage")
    public int totalPage;
    @SerializedName("ex")
    public ExEntity ex;
    @SerializedName("datas")
    public List<DatasEntity> datas;

    public static class ExEntity {
        /**
         * totalMoney : 453800
         */

        @SerializedName("totalMoney")
        public int totalMoney;
    }

    public static class DatasEntity {
        /**
         * headImage : http://img.kangerys.com/G1/M00/00/00/CqxOrVehYx2Ac2QBAAA1kHVRyx0171.jpg
         * nickName : Geoffrey
         * phone : 15626204068
         * orderCode : 1701487553179148
         * payMoney : 44100
         * payDate : 2017-02-22 14:37:42
         */

        @SerializedName("headImage")
        public String headImage;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("orderCode")
        public String orderCode;
        @SerializedName("payMoney")
        public long payMoney;
        @SerializedName("payDate")
        public String payDate;
    }
}
