package com.xiling.ddmall.shared.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.ddmall.module.pay.PayBalanceActivity;
import com.xiling.ddmall.module.pay.PayMsg;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.GetHiCardPay;
import com.xiling.ddmall.shared.bean.Order;
import com.xiling.ddmall.shared.component.dialog.WJDialog;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.UserService;
import com.xiling.ddmall.shared.service.contract.IOrderService;
import com.xiling.ddmall.shared.service.contract.IPayService;

import org.greenrobot.eventbus.EventBus;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.util
 * @since 2017-07-06
 */
public class PayUtil {

    public static void payByWebWepay(final Activity context, final Order order) {
        IPayService service = ServiceManager.getInstance().createService(IPayService.class);
        APIManager.startRequest(
                service.getHiCardPay(
                        order.orderMain.orderCode,
                        "013",
                        null,
                        StringUtil.md5("a70c34cc321f407d990c7a2aa7900729" + order.orderMain.orderCode)
                ),
                new BaseRequestListener<GetHiCardPay>(context) {
                    @Override
                    public void onSuccess(GetHiCardPay result) {
                        //货号
                        ToastUtil.success("创建成功");

                        String qrURL = result.mwebUrl;
                        if (webviewStartUrl(context, qrURL)) {
                            showWebPayDialog(context, order);
                        }
                    }
                });
    }

    public static void payByWebAlipay(final Activity context, final Order order) {
        IPayService service = ServiceManager.getInstance().createService(IPayService.class);
        APIManager.startRequest(
                service.getHiCardPay(
                        order.orderMain.orderCode,
                        "009",
                        null,
                        StringUtil.md5("a70c34cc321f407d990c7a2aa7900729" + order.orderMain.orderCode)
                ),
                new BaseRequestListener<GetHiCardPay>(context) {
                    @Override
                    public void onSuccess(GetHiCardPay result) {
                        String qrURL = result.qrURL;
                        if (webviewStartUrl(context, qrURL)) {
                            showWebPayDialog(context, order);
                        }
                    }
                });
    }

    public static boolean webviewStartUrl(Context context, String url) {
        if (StringUtils.isEmpty(url)) {
            ToastUtil.error("URL数据异常");
            return false;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
        return true;
    }

    public static void showWebPayDialog(final Activity context, final Order order) {
        WJDialog dialog = new WJDialog(context);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setContentText("支付后若订单状态没及时改变，可稍后再查看");
        dialog.setConfirmText("好的");
        dialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOrderPayStatusDialog(context, order);
            }
        });
    }

    public static void checkOrderPayStatusDialog(final Activity context, final Order order) {
        IOrderService service = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(service.getOrderByCode(order.orderMain.orderCode), new BaseRequestListener<Order>(context) {
            @Override
            public void onSuccess(Order result) {
                if (result.orderMain.isPay) {
                    EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_WEBPAY_SUCCEED,""));
                } else {
                    EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_WEBPAY_FAIL,""));
                }
            }
        });
    }

    /**
     * 余额支付
     * @param context
     * @param order
     */
    public static void payBalance(final Activity context, final Order order, final boolean isOrderPay) {
        UserService.checkHasPassword(context, new UserService.HasPasswordListener() {
            @Override
            public void onHasPassword() {
                Intent intent = new Intent(context, PayBalanceActivity.class);
                intent.putExtra("order", order);
                intent.putExtra("isOrderPay", isOrderPay);
                context.startActivity(intent);
            }
        });
    }
}
