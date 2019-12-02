package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/19.
 */
public class NoticeListModel {

    /**
     * datas : [{"notesId":"ed104c2c7bc34aa68ef27747edb24052","title":"张碧晨演绎黑白灰经典搭配 酷帅自然又有范儿","createDate":"2017-06-24 14:58:57"},{"notesId":"fca2a0d935d244a48b8396e6bb1d47b9","title":"这是公告标题！！！！","createDate":"2017-06-22 17:59:56"},{"notesId":"0ea7412c145543b5a04862067767fd2f","title":"公共测试333333","createDate":"2017-06-17 15:06:50"},{"notesId":"3d7bf1bad12b47f0b3fef6f3b20d57a8","title":"公告测试5555555555","createDate":"2017-06-07 14:44:42"},{"notesId":"4b469b1dbffa4d53bb4f371512e5faa1","title":"公告测试4444444444444","createDate":"2017-06-07 14:44:42"},{"notesId":"58ad03c6be7b46e4857c825a3eb9a1cf","title":"公告测试222222","createDate":"2017-06-07 14:44:42"},{"notesId":"9d4332f9dc6540c3b0a66d5baeb5ac7a","title":"公告测试","createDate":"2017-06-07 14:44:42"}]
     * pageOffset : 1
     * pageSize : 15
     * totalRecord : 7
     * totalPage : 0
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
         * notesId : ed104c2c7bc34aa68ef27747edb24052
         * title : 张碧晨演绎黑白灰经典搭配 酷帅自然又有范儿
         * createDate : 2017-06-24 14:58:57
         */

        @SerializedName("notesId")
        public String notesId;
        @SerializedName("title")
        public String title;
        @SerializedName("createDate")
        public String createDate;
    }
}
