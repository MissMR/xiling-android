package com.dodomall.ddmall.ddui.manager;

import android.content.Context;
import android.content.Intent;

import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.activity.IncomeRecordActivity;
import com.dodomall.ddmall.ddui.activity.TeamOrderActivity;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.module.auth.Config;
import com.dodomall.ddmall.module.order.OrderDetailActivity;
import com.dodomall.ddmall.module.order.RefundDetailActivity;
import com.dodomall.ddmall.module.page.WebViewActivity;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.bean.Message;
import com.dodomall.ddmall.shared.constant.Key;

/**
 * created by Jigsaw at 2019/1/9
 */
public class MessageManager {
    private Context mContext;
    private Message mMessage;

    private MessageManager(Context context, Message message) {
        mContext = context;
        mMessage = message;
    }

    public void setMessage(Message message) {
        this.mMessage = message;
    }

    public static MessageManager newInstance(Context context, Message message) {
        return new MessageManager(context, message);
    }

    public void openMessageDetail() {
        openMessageDetail(false);
    }

    public void openMessageDetail(boolean isActivityInNewTask) {
        Intent intent = getIntent();
        if (intent == null) {
            DLog.e("openMessageDetail 不能为空");
            return;
        }
        if (isActivityInNewTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }

    public Intent getIntent() {
        return getIntent(mContext, mMessage);
    }

    public static Intent getIntent(Context context, Message message) {
        if (context == null || message == null) {
            DLog.e("getIntent context == null || message == null");
            return null;
        }
        Intent intent = new Intent();
        switch (message.businessType) {
            case MessageType.ORDER:
                // 订单详情页
                intent.setClass(context, OrderDetailActivity.class);
                intent.putExtra("orderCode", message.businessCode);
                break;
            case MessageType.REFUND_ORDER:
                // 退款/退货详情页
                intent.setClass(context, RefundDetailActivity.class);
                intent.putExtra(Config.INTENT_KEY_ID, message.businessCode);
                break;
            case MessageType.TRAINING_REWARD_DIRECT_LEADER_ACTIVE:
            case MessageType.TRAINING_REWARD_DIRECT_LEADER_DIAMOND_VALID:
            case MessageType.TRAINING_REWARD_DIRECTLEADER_NO:
            case MessageType.TRAINING_REWARD_INDIRECT_LEADER_DIAMOND_NO:
            case MessageType.TRAINING_REWARD_SUPERIOR:
            case MessageType.SALE_REWARD_SURE:
            case MessageType.SALE_REWARD_FANS_SURE:
                // 收益记录
                intent.setClass(context, IncomeRecordActivity.class);
                break;
            case MessageType.SALE_REWARD_FANS_BUY:
            case MessageType.SALE_REWARD_SELF_BUY:
                // 客户订单列表
                intent.setClass(context, TeamOrderActivity.class);
                break;
            case MessageType.SYS_SELF_UP_VIP:
            case MessageType.SYS_VIP_UP_LEVEL:
            case MessageType.SYS_INVOTE_SUCCESS:
            case MessageType.SYS_TEAM_IP_VIP:
            case MessageType.SYS_TEAM_UP_ALERT:
                // 店主中心
                intent.setClass(context, MainActivity.class);
                intent.putExtra(Constants.Extras.TAB_INDEX, 1);
                break;
            case MessageType.PRODUCT_DETAIL:
                intent.setClass(context, DDProductDetailActivity.class);
                intent.putExtra(Key.SPU_ID, message.businessCode);
                break;
            case MessageType.WEB_URL:
                intent.setClass(context, WebViewActivity.class);
                intent.putExtra(Constants.Extras.WEB_URL, message.businessCode);
                break;
            default:
                return null;
        }
        return intent;
    }

    public static class MessageType {

        // 商品详情页
        public static final int PRODUCT_DETAIL = 1008;
        // webview
        public static final int WEB_URL = 2;

        // 订单
        public static final int ORDER = 101;

        // 退货单
        public static final int REFUND_ORDER = 201;

        // 培训津贴
        public static final int TRAINING_REWARD_SUPERIOR = 301;

        public static final int TRAINING_REWARD_DIRECTLEADER_NO = 302;

        public static final int TRAINING_REWARD_DIRECT_LEADER_DIAMOND_VALID = 303;

        public static final int TRAINING_REWARD_DIRECT_LEADER_ACTIVE = 304;

        public static final int TRAINING_REWARD_INDIRECT_LEADER_DIAMOND_NO = 305;

        // 销售佣金收入
        public static final int SALE_REWARD_SELF_BUY = 401;

        public static final int SALE_REWARD_SURE = 402;

        public static final int SALE_REWARD_FANS_BUY = 403;

        public static final int SALE_REWARD_FANS_SURE = 404;

        // 系统
        public static final int SYS_REG_SUCCESS = 1001;

        public static final int SYS_SELF_UP_VIP = 1002;

        public static final int SYS_VIP_UP_LEVEL = 1003;

        public static final int SYS_INVOTE_SUCCESS = 1004;

        public static final int SYS_TEAM_IP_VIP = 1005;

        public static final int SYS_TEAM_UP_ALERT = 1006;

        public static final int SYS_TEAM_ADD = 1007;

        /**
         * // 订单
         ORDER(101, "订单"),
         // 退货单
         REFUND_ORDER(201, "退货退款"),

         // 培训津贴
         TRAINING_REWARD_SUPERIOR(301, "直接推荐人培训津贴"),
         TRAINING_REWARD_DIRECTLEADER_NO(302, "直接团队长钻石/皇冠店主-无条件"),
         TRAINING_REWARD_DIRECT_LEADER_DIAMOND_VALID(303, "直接团队长钻石店主-有效"),
         TRAINING_REWARD_DIRECT_LEADER_ACTIVE(304, "直接团队长钻石/皇冠店主-活跃"),
         TRAINING_REWARD_INDIRECT_LEADER_DIAMOND_NO(305, "间接团队长钻石店主-无条件"),

         // 销售佣金收入
         SALE_REWARD_SELF_BUY(401, "自购提醒"),
         SALE_REWARD_SURE(402, "自购确认收货"),
         SALE_REWARD_FANS_BUY(403, "下级购买提醒"),
         SALE_REWARD_FANS_SURE(404, "下级确认收货"),

         // 系统
         SYS_REG_SUCCESS(1001, "注册成功"),
         SYS_SELF_UP_VIP(1002, "晋升店主"),
         SYS_VIP_UP_LEVEL(1003, "店主晋级"),
         SYS_INVOTE_SUCCESS(1004, "邀请成功"),
         SYS_TEAM_IP_VIP(1005, "用户晋升"),
         SYS_TEAM_UP_ALERT(1006, "团队晋级提醒"),
         SYS_TEAM_ADD(1007, "团队成员新增");
         */
    }
}
