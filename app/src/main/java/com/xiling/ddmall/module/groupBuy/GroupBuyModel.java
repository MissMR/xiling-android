package com.xiling.ddmall.module.groupBuy;

import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.bean.Product;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/2.
 */
public class GroupBuyModel {

    /**
     * groupActivity : {"activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","title":"测试团购活动","rule":"团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则","orderLifeCycle":10,"joinCount":55,"joinMemberNum":2,"startDate":"2017-10-31 10:47:29","endDate":"2017-12-23 10:47:29","memberCreteDate":"2016-12-01 10:47:29"}
     * joinGroupActivityInfo : {"orderId":"523812a29e034fc39c17a724c0d465b4","memberId":"0d3e5898d0c04bde8afec8adea40179d","headImage":"http://flyimg.kangerys.com/G1/M00/00/6A/eEzAjVloh7mAFHPwAACVlV6m-v8366.jpg","nickName":"我是昵称","activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","activityStatus":2,"activityStatusStr":"拼团成功","groupCode":"1571509764504472","groupLeaderReturn":0,"expiresDate":"2017-11-02 05:52:20","joinMemberNum":2,"createOrderNum":2,"payOrderNum":2,"payDate":"2017-11-01 19:52:20"}
     * joinMember : [{"groupCode":"1571509764504472","memberId":"0d3e5898d0c04bde8afec8adea40179d","orderId":"523812a29e034fc39c17a724c0d465b4","headImage":"http://flyimg.kangerys.com/G1/M00/00/6A/eEzAjVloh7mAFHPwAACVlV6m-v8366.jpg","nickName":"我是昵称","activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","status":1,"payDate":"2017-11-01 19:52:15","role":1},{"groupCode":"1571509764504472","memberId":"d8f33a4926ae47289f846c6a825944fe","orderId":"35819efda2f3433ba94b80926292698b","headImage":"http://flyimg.kangerys.com/G1/M00/00/6B/eEzAjVltvBmAB87SAAAJxz-vX9c032.jpg","nickName":"大大6666","activityId":"b44c1c23fc1f4f6c8384e7c22ed409e5","status":1,"payDate":"2017-11-01 19:58:23","role":0}]
     */

    @SerializedName("groupActivity")
    public GroupActivityEntity groupActivity;
    @SerializedName("joinGroupActivityInfo")
    public JoinGroupActivityInfoEntity joinGroupActivityInfo;
    @SerializedName("joinMember")
    public List<JoinMemberEntity> joinMember;
    @SerializedName("spuDetail")
    public Product product;

    public static class GroupActivityEntity {
        /**
         * activityId : b44c1c23fc1f4f6c8384e7c22ed409e5
         * title : 测试团购活动
         * rule : 团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则团购活动规则
         * orderLifeCycle : 10
         * joinCount : 55
         * joinMemberNum : 2
         * startDate : 2017-10-31 10:47:29
         * endDate : 2017-12-23 10:47:29
         * memberCreteDate : 2016-12-01 10:47:29
         */

        @SerializedName("activityId")
        public String activityId;
        @SerializedName("title")
        public String title;
        @SerializedName("rule")
        public String rule;
        @SerializedName("orderLifeCycle")
        public int orderLifeCycle;
        @SerializedName("joinCount")
        public int joinCount;
        @SerializedName("joinMemberNum")
        public int joinMemberNum;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("memberCreteDate")
        public String memberCreteDate;
    }

    public static class JoinGroupActivityInfoEntity {
        /**
         * orderId : 523812a29e034fc39c17a724c0d465b4
         * memberId : 0d3e5898d0c04bde8afec8adea40179d
         * headImage : http://flyimg.kangerys.com/G1/M00/00/6A/eEzAjVloh7mAFHPwAACVlV6m-v8366.jpg
         * nickName : 我是昵称
         * activityId : b44c1c23fc1f4f6c8384e7c22ed409e5
         * activityStatus : 2
         * activityStatusStr : 拼团成功
         * groupCode : 1571509764504472
         * groupLeaderReturn : 0
         * expiresDate : 2017-11-02 05:52:20
         * joinMemberNum : 2
         * createOrderNum : 2
         * payOrderNum : 2
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

    public static class JoinMemberEntity {
        /**
         * groupCode : 1571509764504472
         * memberId : 0d3e5898d0c04bde8afec8adea40179d
         * orderId : 523812a29e034fc39c17a724c0d465b4
         * headImage : http://flyimg.kangerys.com/G1/M00/00/6A/eEzAjVloh7mAFHPwAACVlV6m-v8366.jpg
         * nickName : 我是昵称
         * activityId : b44c1c23fc1f4f6c8384e7c22ed409e5
         * status : 1
         * payDate : 2017-11-01 19:52:15
         * role : 1
         */

        @SerializedName("groupCode")
        public String groupCode;
        @SerializedName("memberId")
        public String memberId;
        @SerializedName("orderId")
        public String orderId;
        @SerializedName("headImage")
        public String headImage;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("activityId")
        public String activityId;
        @SerializedName("status")
        public int status;
        @SerializedName("payDate")
        public String payDate;
        /**
         * 1：团长
         */
        @SerializedName("role")
        public int role;
        @SerializedName("orderCode")
        public String orderCode;
    }
}
