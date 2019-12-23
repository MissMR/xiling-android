package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/22.
 */
public class RefundsOrder {

    /**
     * apiRefundOrderBean : {"refundId":"10ea8dc7bd6843ff9805346f3e35027c","refundCode":"1241511471552036","refundType":3,"refundStatus":0,"refundResult":"","refundReason":"","refundRemark":"大爷买了这个产品觉得亏了","refundGoodsImage":["ddd","dddd"],"refundGoodsExpressName":"","refundGoodsExpressCode":"","expressType":"","receivedRefundGoodsEvidence":"","applyRefundMoney":0,"refundMoney":"","refundTypeStr":"申请部分退货","refundStatusStr":"正常","orderId":"185261bb38e34572ae4a85c1e5fa3865"}
     * orderProducts : [{"order1Id":"48dfcba8197b4f9aaa7cb282a31a447e","storeId":"db7f7883f1cd438698b5080b1a064c95","productImage":"http://testimg.beautysecret.cn/G1/M00/00/00/cjc2GFl67X-AANjbAAH5Fs6-W9A000.jpg","skuId":"52836f8a977f4b3e8f259da0007edd99","quantity":1,"price":5000,"retailPrice":9000,"costPrice":0,"lineTotal":5000,"properties":"包装规格:盒 ","presentRelationProductId":"","productType":0,"refundStatus":6,"refundStatusStr":"退货中","refundId":"10ea8dc7bd6843ff9805346f3e35027c","discountCoupon":0,"score":0,"productName":"果感水果酵素滢润保湿面膜","storeName":"美颜秘笈","sobotId":"1234"}]
     */

    @SerializedName("apiRefundOrderBean")
    public ApiRefundOrderBeanEntity apiRefundOrderBean;
    @SerializedName("orderProducts")
    public List<OrderProduct> orderProducts;
    /**
     * storeBean : {"storeId":"db7f7883f1cd438698b5080b1a064c95","memberId":"42cb7a0728f1423197808204fa007412","sobotId":"1234","status":1,"statusStr":"正常营业","storeName":"美颜秘笈","bannerImage":"","thumbUrl":"http://testimg.beautysecret.cn/G1/M00/00/0A/cjcLYVoNQGOACeFWAAC0SMVOEKI549.png","contact":"张三","phone":"18800000000","province":"广东省","city":"深圳市","district":"福田区","address":"云城西路228创意园K栋1至3楼","saleProductCount":8,"saleCount":1878,"expressName":"顺丰","shipAddress":"广州","descScore":20,"expressScore":20,"serveScore":20,"wxQrCode":"http://testimg.beautysecret.cn/G1/M00/00/03/cjc2GFmVhYqADPbWAAFM2sqS3vE112.jpg","reason":""}
     */

    @SerializedName("storeBean")
    public StoreBeanEntity storeBean;

    public static class ApiRefundOrderBeanEntity {
        /**
         * refundId : 10ea8dc7bd6843ff9805346f3e35027c
         * refundCode : 1241511471552036
         * refundType : 3
         * refundStatus : 0
         * refundResult :
         * refundReason :
         * refundRemark : 大爷买了这个产品觉得亏了
         * refundGoodsImage : ["ddd","dddd"]
         * refundGoodsExpressName :
         * refundGoodsExpressCode :
         * expressType :
         * receivedRefundGoodsEvidence :
         * applyRefundMoney : 0
         * refundMoney :
         * refundTypeStr : 申请部分退货
         * refundStatusStr : 正常
         * orderId : 185261bb38e34572ae4a85c1e5fa3865
         */

        @SerializedName("refundId")
        public String refundId;
        @SerializedName("refundCode")
        public String refundCode;
        /**
         * 2: 退款
         * 1：退货
         * 3:部分退货
         * 4:部分退款
         */
        @SerializedName("refundType")
        public int refundType;
        /**
         * rejectRefundGoods(-4,"拒绝退货"),
         * rejectRefundMoney(-3,"拒绝退款"),
         * GiveUPRefundGoods(-2, "取消申请退货"),
         * GiveUPRefundMoney(-1, "取消申请退款"),
         * ApplyRefund(0,"申请中"),
         * SellerAgree(1,"商家确认"),
         * UpLoadEvidence(2,"上传凭证"),
         * SellerReceiving(3,"商家确认收货"),
         * SellerRefundMoney(4,"平台退款成功"),
         */
        @SerializedName("refundStatus")
        public int refundStatus;
        @SerializedName("refundResult")
        public String refundResult;
        @SerializedName("refundReason")
        public String refundReason;
        @SerializedName("refundRemark")
        public String refundRemark;
        @SerializedName("refundGoodsExpressName")
        public String refundGoodsExpressName;
        @SerializedName("refundGoodsExpressCode")
        public String refundGoodsExpressCode;
        @SerializedName("expressType")
        public String expressType;
        //        @SerializedName("receivedRefundGoodsEvidence")
//        public String receivedRefundGoodsEvidence;
        @SerializedName("applyRefundMoney")
        public int applyRefundMoney;
        @SerializedName("refundMoney")
        public long refundMoney;
        @SerializedName("refundTypeStr")
        public String refundTypeStr;
        @SerializedName("refundStatusStr")
        public String refundStatusStr;
        @SerializedName("orderId")
        public String orderId;
        @SerializedName("mePromptList")
        public String mePromptList;
        @SerializedName("mePromptDetail")
        public String mePromptDetail;
        @SerializedName("storePromptList")
        public String storePromptList;
        @SerializedName("storePromptDetail")
        public String storePromptDetail;
        @SerializedName("orderCode")
        public String orderCode;
        @SerializedName("createDate")
        public String createDate;
        @SerializedName("refundGoodsImage")
        public List<String> refundGoodsImage;
        /**
         * memberId : 5965af81a71e4ff598b6bf50389aee98
         * nickName : 动态
         * sellerRemark :
         * storeMemberId : 34a561b32c664348bb88e0fb04647b25
         */

        @SerializedName("memberId")
        public String memberId;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("sellerRemark")
        public String sellerRemark;
        @SerializedName("storeMemberId")
        public String storeMemberId;

        /**
         * @return 是不是申请退kuan
         */
        public boolean isRefundMoney() {
            return refundType == 2 || refundType == 4;
        }
    }

    public static class StoreBeanEntity {
        /**
         * storeId : db7f7883f1cd438698b5080b1a064c95
         * memberId : 42cb7a0728f1423197808204fa007412
         * sobotId : 1234
         * status : 1
         * statusStr : 正常营业
         * storeName : 美颜秘笈
         * bannerImage :
         * thumbUrl : http://testimg.beautysecret.cn/G1/M00/00/0A/cjcLYVoNQGOACeFWAAC0SMVOEKI549.png
         * contact : 张三
         * phone : 18800000000
         * province : 广东省
         * city : 深圳市
         * district : 福田区
         * address : 云城西路228创意园K栋1至3楼
         * saleProductCount : 8
         * saleCount : 1878
         * expressName : 顺丰
         * shipAddress : 广州
         * descScore : 20
         * expressScore : 20
         * serveScore : 20
         * wxQrCode : http://testimg.beautysecret.cn/G1/M00/00/03/cjc2GFmVhYqADPbWAAFM2sqS3vE112.jpg
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
    }
}
