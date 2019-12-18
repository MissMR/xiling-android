package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/18.
 */
public class MemberStore {


    /**
     * storeId : db7f7883f1cd438698b5080b1a064c95
     * memberId : fa0c23cefd164352a48fc30f095485ea
     * sobotId : 1234
     * status : 1
     * statusStr : 正常营业
     * storeName : 美颜秘笈
     * bannerImage :
     * thumbUrl : http://testimg.beautysecret.cn/G1/M00/00/04/cjc2GFmW3liAVnW2AAC0SMVOEKI991.png
     * contact : 张三
     * phone : 18800000000
     * province : 广东省
     * city : 深圳市
     * district : 福田区
     * address : 云城西路228创意园K栋1至3楼
     * saleProductCount : 8
     * saleCount : 402
     * expressName : 德邦
     * shipAddress : 广东
     * descScore : 0
     * expressScore : 0
     * serveScore : 0
     * wxQrCode : http://testimg.beautysecret.cn/G1/M00/00/04/cjdvUVmc6pCAf60cAAA7jsH1vJQ372.jpg
     * reason :
     */

    @SerializedName("storeId")
    public String storeId;
    @SerializedName("memberId")
    public String memberId;
    @SerializedName("sobotId")
    public String sobotId;
    @SerializedName("status")
    public int status;
    @SerializedName("statusStr")
    public String statusStr;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("bannerImage")
    public String bannerImage;
    @SerializedName("thumbUrl")
    public String thumbUrl;
    @SerializedName("contact")
    public String contact;
    @SerializedName("phone")
    public String phone;
    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("district")
    public String district;
    @SerializedName("address")
    public String address;
    @SerializedName("saleProductCount")
    public int saleProductCount;
    @SerializedName("saleCount")
    public int saleCount;
    @SerializedName("expressName")
    public String expressName;
    @SerializedName("shipAddress")
    public String shipAddress;
    @SerializedName("descScore")
    public int descScore;
    @SerializedName("expressScore")
    public int expressScore;
    @SerializedName("serveScore")
    public int serveScore;
    @SerializedName("wxQrCode")
    public String wxQrCode;
    @SerializedName("reason")
    public String reason;
    @SerializedName("shipType")
    public int shipType;
}
