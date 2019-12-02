package com.dodomall.ddmall.shared.util;

import android.content.Context;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.dodomall.ddmall.shared.bean.Order;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/15.
 */
public class UmengUtils {

    public static final String EVENT_REFUNDS_MONEY = "退款";
    public static final String EVENT_REFUNDS_GOODS = "退货";

    public static void addRefunds(Context context, Order order, String event, String refundMoney, long uploadMoney) {
        Map<String, String> map = new HashMap<>();
        map.put("手机号", order.orderMain.phone);
        map.put("订单号", order.orderMain.orderCode);
        if (event.equals(EVENT_REFUNDS_GOODS)) {
            map.put("订单可退款金额", order.canRefundMoney(1) + "");
        } else {
            map.put("订单可退款金额", order.canRefundMoney(2) + "");
        }
        map.put("订单支付金额", order.orderMain.payMoney + "");
        map.put("用户填写退款金额", refundMoney);
        map.put("上传到服务器退款金额", uploadMoney + "");
        map.put("退款时间", TimeUtils.date2String(new Date()));
        map.put("应用版本", AppUtils.getAppVersionCode(context) + "");
        MobclickAgent.onEvent(context, EVENT_REFUNDS_MONEY, map);
    }
}
