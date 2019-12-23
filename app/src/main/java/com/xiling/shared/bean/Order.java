package com.xiling.shared.bean;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.shared.constant.OrderStatus;
import com.google.gson.annotations.SerializedName;
import com.xiling.shared.constant.AppTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-06-20
 */
public class Order implements Serializable {

    public static final String ORDER_ALL = "all";
    public static final String ORDER_WAIT_PAY = OrderStatus.waitPay.getKey();
    public static final String ORDER_PAID = OrderStatus.waitShip.getKey();
    public static final String ORDER_DISPATCHED = OrderStatus.dispatched.getKey();

    @SerializedName("storeId")
    public String storeId;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("nickName")
    public String nickName;
    @SerializedName("sobotId")
    public String sobotId;
    @SerializedName("orderMain")
    public OrderMain orderMain;
    @SerializedName("refundOrder")
    public RefundOrder refundOrder;
    @SerializedName("orderProducts")
    public List<OrderProduct> products;
    @SerializedName("groupInfo")
    public GroupInfoEntity groupInfo;
    @SerializedName("storeFlag")
    public int storeFlag;

    @SerializedName("productType")
    public int productType;

    public boolean isUseCoupon() {
        return this.orderMain.discountCoupon > 0;
    }


    // 0元购
    public boolean isOrderFree() {
        // 店主送流量 和 0元助力
        return productType == 4 || productType == 10;
    }

    // 是否是店主礼包
    public boolean isStoreGift() {
        return storeFlag == 1;
    }

    /**
     * @return 是否在订单显示团购信息
     */
    public boolean isShowGroupOrderStatus() {
        return (orderMain.status == 2) && (groupInfo != null) && (groupInfo.activityStatus == AppTypes.GROUP_BUY.STATUS_WAIT_COMPLETE);
    }

    public boolean isGroupOrder() {
        return (groupInfo != null) && !StringUtils.isEmpty(groupInfo.activityId) && orderMain.isPay;
    }


    public static class GroupInfoEntity implements Serializable {
        /**
         * orderId : 523812a29e034fc39c17a724c0d465b4
         * memberId : 0d3e5898d0c04bde8afec8adea40179d
         * headImage :
         * nickName :
         * activityId : b44c1c23fc1f4f6c8384e7c22ed409e5
         * activityStatus : 1
         * activityStatusStr : 拼团中
         * groupCode : 1571509764504472
         * groupLeaderReturn : 0
         * expiresDate : 2017-11-02 05:52:20
         * joinMemberNum : 2
         * createOrderNum : 1
         * payOrderNum : 1
         * payDate : 2017-11-01 19:52:20
         */

        @SerializedName("orderId")
        public String orderId;
        @SerializedName("memberId")
        public String memberId;
        @SerializedName("headImage")
        public String headImage;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("activityId")
        public String activityId;
        @SerializedName("activityStatus")
        public int activityStatus;
        @SerializedName("activityStatusStr")
        public String activityStatusStr;
        @SerializedName("groupCode")
        public String groupCode;
        @SerializedName("groupLeaderReturn")
        public int groupLeaderReturn;
        @SerializedName("expiresDate")
        public String expiresDate;
        @SerializedName("joinMemberNum")
        public int joinMemberNum;
        @SerializedName("createOrderNum")
        public int createOrderNum;
        @SerializedName("payOrderNum")
        public int payOrderNum;
        @SerializedName("payDate")
        public String payDate;
    }


    public static class OrderMain implements Serializable {

        @SerializedName("orderId")
        public String orderId;
        @SerializedName("memberId")
        public String memberId;
        @SerializedName("orderCode")
        public String orderCode;
        @SerializedName("orderStatus")
        public int status;
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
        @SerializedName("detail")
        public String detail;
        @SerializedName("totalMoney")
        public long totalMoney;
        @SerializedName("totalWeight")
        public int totalWeight;
        @SerializedName("freight")
        public long freight;
        @SerializedName("discountCoupon")
        public long discountCoupon;
        @SerializedName("isPay")
        public boolean isPay;
        @SerializedName("payType")
        public int payType;
        @SerializedName("payMoney")
        public int payMoney;
        @SerializedName("payDate")
        public String payDate;
        @SerializedName("payRemark")
        public String payRemark;
        @SerializedName("isReceived")
        public int isReceived;
        @SerializedName("receivedDate")
        public String receivedDate;
        @SerializedName("orderFrom")
        public int orderFrom;
        @SerializedName("buyerRemark")
        public String buyerRemark;
        @SerializedName("sellerRemark")
        public String sellerRemark;
        @SerializedName("expressId")
        public int expressId;
        @SerializedName("expressName")
        public String expressName;
        @SerializedName("expressCode")
        public String expressCode;
        @SerializedName("expressType")
        public String expressType;
        @SerializedName("shipDate")
        public String shipDate;
        @SerializedName("createDate")
        public String createDate;
        @SerializedName("deleteFlag")
        public int deleteFlag;
        @SerializedName("orderStatusStr")
        public String orderStatusStr;
        @SerializedName("payTypeStr")
        public String payTypeStr;
        @SerializedName("productMoney")
        public long productMoney;
        @SerializedName("score")
        public long score;
        /**
         * discountRate : 60
         * discountMoney : 15040
         */

        @SerializedName("discountRate")
        public int discountRate;
        @SerializedName("discountMoney")
        public long discountMoney;
        /**
         * totalProductMoney : 72200
         */

        @SerializedName("totalProductMoney")
        public long totalProductMoney;

        @SerializedName("outTime")
        public int outTime;

        public String getFullAddress() {
            return province + city + district + detail;
        }

    }

    public static class RefundOrder implements Serializable {
        public int refundType;
        public int refundStatus;
        public String refundReason;
        public String refundRemark;
        public long refundMoney;
        public long applyRefundMoney;
        public String refundGoodsExpressName;
        public String refundGoodsExpressCode;
        public List<String> refundGoodsImage = new ArrayList<>();
        public String refundId;
    }

    public long canRefundMoney(int refundType) {
        if (refundType == 1) {
            return orderMain.payMoney - orderMain.freight;
        } else {
            return orderMain.payMoney;
        }
    }
}
