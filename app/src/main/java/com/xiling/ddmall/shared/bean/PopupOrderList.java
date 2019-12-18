package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/4.
 */
public class PopupOrderList {

    /**
     * datas : [{"orderCode":"1081492464787693","nickName":"林","headImage":"http://wx.qlogo.cn/mmopen/IHcouIVOqibqXrOR92zqjhkZzWuCAPIiaJD3yIHERLZjTpKDNcbfPfl7ibw0192CbJ31AVCLWEXjJ5c1bRsYYL8DdsSTZabQo05/0","createDate":"2017-04-15 14:23:44"},{"orderCode":"1081492464470703","nickName":"林","headImage":"http://wx.qlogo.cn/mmopen/IHcouIVOqibqXrOR92zqjhkZzWuCAPIiaJD3yIHERLZjTpKDNcbfPfl7ibw0192CbJ31AVCLWEXjJ5c1bRsYYL8DdsSTZabQo05/0","createDate":"2017-04-15 14:18:24"}]
     * pageOffset : 1
     * pageSize : 2
     * totalRecord : 63
     * totalPage : 32
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
         * orderCode : 1081492464787693
         * nickName : 林
         * headImage : http://wx.qlogo.cn/mmopen/IHcouIVOqibqXrOR92zqjhkZzWuCAPIiaJD3yIHERLZjTpKDNcbfPfl7ibw0192CbJ31AVCLWEXjJ5c1bRsYYL8DdsSTZabQo05/0
         * createDate : 2017-04-15 14:23:44
         */

        @SerializedName("orderCode")
        public String orderCode;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("headImage")
        public String headImage;
        @SerializedName("createDate")
        public String createDate;
    }
}
