package com.xiling.module.NearStore.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/20.
 */
public class NearStoreModel implements Serializable {

    /**
     * datas : [{"memberId":"61521d1ab00548bf906c733be9fb4aea","inviteCode":0,"phone":"18800000008","storeName":"08店铺","nickName":"","thumbUrl":"http://testimg.beautysecret.cn/G1/M00/00/04/cjc2GFmgEXKAS4nzAAJUVIxUINA064.jpg","headImage":"","city":"广州市","district":"白云区","lat":23.190715,"lag":113.27152,"distance":106},{"memberId":"de0cbcaea3424b49a10fec739790ad5f","inviteCode":0,"phone":"13672651262","storeName":"三星店铺","nickName":"","thumbUrl":"http://testimg.beautysecret.cn/G1/M00/00/05/cjc2GFmufiqAdBPdAAJgsSjCpSo810.jpg","headImage":"","city":"广州市","district":"白云区","lat":23.190715,"lag":113.27152,"distance":106},{"memberId":"34a561b32c664348bb88e0fb04647b25","inviteCode":0,"phone":"12345678910","storeName":"顶点","nickName":"","thumbUrl":"http://testimg.beautysecret.cn/G1/M00/00/04/cjcLYVmhJAKALDA6AAWFxfa8Qk4844.jpg","headImage":"","city":"广州市","district":"白云区","lat":23.190223,"lag":113.272117,"distance":173},{"memberId":"5965af81a71e4ff598b6bf50389aee98","inviteCode":0,"phone":"15016147075","storeName":"陈杂货铺","nickName":"","thumbUrl":"http://testimg.beautysecret.cn/G1/M00/00/04/cjcLYVmhOimAC7AXAADzgE8OQys476.jpg","headImage":"","city":"广州市","district":"白云区","lat":23.190223,"lag":113.272117,"distance":173}]
     * pageOffset : 1
     * pageSize : 5
     * totalRecord : 12
     * totalPage : 3
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

    public static class DatasEntity implements Serializable {
        /**
         * memberId : 61521d1ab00548bf906c733be9fb4aea
         * inviteCode : 0
         * phone : 18800000008
         * storeName : 08店铺
         * nickName :
         * thumbUrl : http://testimg.beautysecret.cn/G1/M00/00/04/cjc2GFmgEXKAS4nzAAJUVIxUINA064.jpg
         * headImage :
         * city : 广州市
         * district : 白云区
         * lat : 23.190715
         * lag : 113.27152
         * distance : 106
         */

        @SerializedName("memberId")
        public String memberId;
        @SerializedName("inviteCode")
        public String inviteCode;
        @SerializedName("phone")
        public String phone;
        @SerializedName("storeName")
        public String storeName;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("thumbUrl")
        public String thumbUrl;
        @SerializedName("headImage")
        public String headImage;
        @SerializedName("city")
        public String city;
        @SerializedName("district")
        public String district;
        @SerializedName("lat")
        public double lat;
        @SerializedName("lag")
        public double lag;
        @SerializedName("distance")
        public int distance;

        public String getLocationStr() {
            return city + district;
        }

        public String getDistanceStr() {
            if (distance < 100) {
                return "<100m";
            } else if (distance < 1000) {
                return String.format("%d00m", distance / 100);
            } else {
                return String.format("%.1fkm", distance * 1.0f / 1000).replace(".0", "");
            }
        }

    }
}
