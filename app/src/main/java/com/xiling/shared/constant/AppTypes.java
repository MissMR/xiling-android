package com.xiling.shared.constant;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/6/8.
 */
public class AppTypes {
    /**
     * 实名认证状态
     */
    public static final class AUTH_STATUS {
        public final static int NO_SUBMIT = 0;
        public final static int WAIT = 1;
        public final static int SUCESS = 2;
        public final static int FAIL = 3;
    }

    /**
     * 银行卡状态
     */
    public static final class CARD_STATUS {
        public final static int NO_SUBMIT = 3;
        public final static int WAIT = 0;
        public final static int SUCESS = 1;
        public final static int FAIL = 2;
    }

    public static final class MY_SUBMIT_PROJECT_STATUS {
        public final static int OFFER = 0;//新的方案
        public final static int ACCEPT = 1;//对方已接受你的方案
        public final static int REFUSE = 2;//对方已拒绝你的方案
        public final static int CANCEL = 3;//对方已取消
        public final static int FINiSH = 4;//已完成
        public final static int EXPIRE = 5;//已过期
    }

    public static final class PROJECT {
        public final static int MY_CREATE = 0;//从我的创建进来
        public final static int MY_SUBMIT = 1;//从我提交的方案进来
    }

    public static final class USER {
        public final static int BIND_PHONE = 1 << 0;
        public final static int EDIT_PHONE = 1 << 1;
    }

    public static final class TRANSFER {
        public final static int TRANSFER_MONEY_SUCESS = 1 << 0;
        public final static int DEAL_SUCESS = 1 << 1;
        /**
         * 转账
         */
        public final static int MONEY = 1 << 2;
        /**
         * 积分转赠
         */
        public final static int SCORE = 1 << 3;
        public final static int TRANSFER_SCORE_SUCESS = 1 << 4;

    }

    public static final class STATUS {
        public final static int SUBMIT_SUCESS = 1 << 0;
        public final static int WAIT = 1 << 1;
        public final static int FAIL = 1 << 2;

        public final static int SUBMIT_SUCESS_STORE = SUBMIT_SUCESS * 100;
        public final static int WAIT_STORE = WAIT * 100;
        public final static int FAIL_STORE = FAIL * 100;


    }

    public static final class QRCODE {
        /**
         * 关注我们
         */
        public final static int SUBSCRIBE = 1 << 0;
        /**
         * 提现
         */
        public final static int DEAL = 1 << 1;
        public final static int BIND_WECHAT = 1 << 2;


    }

    public static final class ORDER {
        public final static String SELLER_WAIT_SHIP = "SELLER_WAIT_SHIP";
        public final static String SELLER_HAS_SHIP = "SELLER_HAS_SHIP";
        public final static String SELLER_HAS_COMPLETE = "SELLER_HAS_COMPLETE";
        public final static String SELLER_HAS_CLOSE = "SELLER_HAS_CLOSE";
        public final static String SELLER_REFUND = "SELLER_REFUND";
        public final static String SELLER_ORDER_REFUND = "SELLER_oder_REFUND";

        public final static int STATUS_SELLER_WAIT_SHIP = 2;
        public final static int STATUS_SELLER_HAS_SHIP = 3;
        public final static int STATUS_SELLER_HAS_COMPLETE = 4;
        public final static int STATUS_SELLER_REFUND = 17;
        public final static int STATUS_SELLER_HAS_CLOSE = 0;

        /**
         * 已关闭
         */
        public final static int STATUS_BUYER_HAS_CLOSE = 0;
        /**
         * 代付款
         */
        public final static int STATUS_BUYER_WAIT_PAY = 1;
        /**
         * 待发货
         */
        public final static int STATUS_BUYER_WAIT_SHIP = 2;
        /**
         * 待收货
         */
        public final static int STATUS_BUYER_HAS_SHIP = 3;
        /**
         * 已收货
         */
        public final static int STATUS_BUYER_HAS_RECEIVED = 4;
        /**
         * 退款中
         */
        public final static int STATUS_BUYER_RETURN_MONEYING = 5;
        /**
         * 退货中
         */
        public final static int STATUS_BUYER_RETURN_GOODING = 6;
        /**
         * 退款完成呢
         */
        public final static int STATUS_BUYER_RETURN_MONECLOSE = 7;
        /**
         * 退货完成
         */
        public final static int STATUS_BUYER_RETURN_GOODSCLOSE = 8;
        /**
         * 订单异常
         */
        public final static int STATUS_BUYER_RETURN_ERROR_ORDER = 9;
    }

    public static final class REFUNDS {
        /**
         * 申请中
         */
        public final static int BUYER_APPLY = 0;
        /**
         * 商家确认
         */
        public final static int SELLER_AGREE = 1;
        /**
         * 上传凭证
         */
        public final static int BUYER_UPLOAD = 2;
        /**
         * 申请中
         */
        public final static int SELLER_RECEIVING = 3;//
        /**
         * 确认退款
         */
        public final static int SELLER_REFUND_MONEY = 4;//
        public final static int MONEY = 2;//退款
        public final static int GOODS = 1;//退货
        public final static int NULL = 0;//不退

    }

    public static final class CART {
        public final static int FROM_HOME = 1 << 0;
        public final static int FROM_ACTIVITY = 1 << 1;
    }

    public static final class FAMILY {
        public final static int LARGE = 1;
        public final static int MIDDLE = 2;
        public final static int SMALL = 3;

        /**
         * 尊姓会员
         */
        public final static int MEMBER_ZUNXIANG = 1;
        /**
         * 金卡会员(废弃)
         */
        public final static int MEMBER_JINKA = 2;
        /**
         * 体验店主
         */
        public final static int MEMBER_TIYAN = 3;
        /**
         * 专营店主
         */
        public final static int MEMBER_ZHUANYING = 4;
        /**
         * 美集店铺
         */
        public final static int MEMBER_DIANPU = 5;
        /**
         * 普通会员
         */
        public final static int MEMBER_NORMAL = 0;

        /**
         * 所有
         */
        public final static int MEMBER_ALL = 99;
    }

    public static final class PAY_TYPE {
        public final static int WECHAT = 1;
        public final static int ALI = 2;
        public final static int BALANCE = 3;
        public final static int WECHAT_WEB = 4;
        public final static int ALI_WEB = 5;

    }

    public static final class STORE {
        public final static int WAIT = 0;
        public final static int COMMON = 1;
        public final static int FAIL = 2;
        public final static int CLOSE = 3;
        public final static int NO_SUBMIT = 4;
    }

    public static final class SKU_SELECTOR_DIALOG {
        public static final int ACTION_SINGLE = -1;
        public final static int ACTION_CART = 1 << 0;
        public final static int ACTION_BUY = 1 << 1;
        public static final int ACTION_DEFAULT = 1 << 4;
        public static final int ACTION_ACTIVITY_FREE = 1 << 5;
        /**
         * 开团
         */
        public final static int ACTION_CREATE_GROUP = 1 << 2;
        /**
         * 加入团购
         */
        public final static int ACTION_JOIN_GROUP = 1 << 3;
    }

    public static final class GROUP_BUY {
        /**
         * 等待支付
         */
        public final static int STATUS_WAIT_PAY = 0;
        /**
         * 拼团中
         */
        public final static int STATUS_WAIT_COMPLETE = 1;
        /**
         * 拼团成功
         */
        public final static int STATUS_SUCCEED = 2;
        /**
         * 失败
         */
        public final static int STATUS_FAIL = 3;
        /**
         * 未知
         */
        public final static int STATUS_UN_KNOW = 99;

        public final static String FORM_CREATE_GROUP = "groupBuy";
        public final static String FORM_JOIN_GROUP = "joinGroup";
    }

    public static class PUSH {
        public final static int PRODUCT_TYPE = 99;
    }
}
