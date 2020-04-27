package com.xiling.shared.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.module.auth.Config;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.groupBuy.JoinGroupActivity;
import com.xiling.module.order.OrderDetailActivity;
import com.xiling.module.order.RefundGoodsActivity;
import com.xiling.module.order.RefundMoneyActivity;
import com.xiling.module.order.ShipActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.pay.PayDialogActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Order;
import com.xiling.shared.bean.OrderProduct;
import com.xiling.shared.bean.RefundBody;
import com.xiling.shared.bean.RefundsOrder;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.TextListDialog;
import com.xiling.shared.component.dialog.TitleContentEditDialog;
import com.xiling.shared.component.dialog.WJDialog;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.CSUtils;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.service
 * @since 2017-07-15
 */
public class OrderService {

    /**
     * 跳到支付订单的页面
     *
     * @param context
     * @param orderCode
     */
    public static void viewPayActivity(final Context context, final String orderCode) {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(orderService.checkOrderToPay(orderCode), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                Intent intent = new Intent(context, PayDialogActivity.class);
                intent.putExtra("orderCode", orderCode);
                context.startActivity(intent);

                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_TO_PAY));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderCode", orderCode);
                context.startActivity(intent);

                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_TO_PAY));
            }
        });
    }

    public static void cancelOrder(final Activity context, final Order order) {
        final WJDialog dialog = new WJDialog(context);
        dialog.show();
        dialog.setTitle("取消订单");
        dialog.setContentText("取消订单后，该交易将被关闭");
        dialog.setCancelText("否");
        dialog.setConfirmText("是");
        dialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
                APIManager.startRequest(orderService.cancelOrder(order.orderMain.orderCode), new BaseRequestListener<Object>(context) {

                    @Override
                    public void onSuccess(Object result) {
                        ToastUtil.success("取消订单成功");
                        EventBus.getDefault().post(new EventMessage(Event.cancelOrder, order));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        super.onComplete();
                    }
                });
            }
        });
    }

    public static void viewApplyRefundMoneyActivity(Context context, Order order) {
        editRefundMoneyActivity(context, order.orderMain.orderCode, null);
    }

    public static void editRefundMoneyActivity(Context context, String orderCode, String refundId) {
        Intent intent = new Intent(context, RefundMoneyActivity.class);
        intent.putExtra("orderCode", orderCode);
        intent.putExtra("refundId", refundId);
        context.startActivity(intent);
    }


    public static void showRefundMoneyDialog(final Activity context, final RefundsOrder refundsOrder) {

        final WJDialog dialog = new WJDialog(context);
        dialog.show();
        dialog.setTitle("同意退款");
        dialog.setContentText("同意后，待平台给买家退款");
        dialog.setCancelText("取消");
        dialog.setConfirmText("确定");
        dialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refundMoney(
                        context,
                        refundsOrder.apiRefundOrderBean.applyRefundMoney,
                        refundsOrder.apiRefundOrderBean.orderCode,
                        refundsOrder.apiRefundOrderBean.refundCode,
                        ""
                );
            }
        });
    }

    private static void refundMoney(Activity context, int applyRefundMoney, String orderCode, String refundCode, String remark) {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                orderService.refundMoney(
                        new RefundBody(refundCode, orderCode, remark, applyRefundMoney)),
                new BaseRequestListener<Object>(context) {
                    @Override
                    public void onSuccess(Object result) {
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                    }
                }
        );
    }

    public static void showRefundGoodsDialog(final Activity context, final RefundsOrder refundsOrder) {

        final WJDialog dialog = new WJDialog(context);
        dialog.show();
        dialog.setTitle("同意退货");
        dialog.setContentText("同意后，需买家寄回商品并且上传快递单号");
        dialog.setCancelText("取消");
        dialog.setConfirmText("确定");
        dialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refundGoods(
                        context,
                        refundsOrder.apiRefundOrderBean.applyRefundMoney,
                        refundsOrder.apiRefundOrderBean.refundCode,
                        refundsOrder.apiRefundOrderBean.orderCode,
                        ""
                );
            }
        });
    }

    private static void refundGoods(Activity context, int applyRefundMoney, String refundCode, String orderCode, String remark) {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                orderService.refundGoods(
                        new RefundBody(refundCode, orderCode, remark, applyRefundMoney)),
                new BaseRequestListener<Object>(context) {
                    @Override
                    public void onSuccess(Object result) {
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                    }
                }
        );
    }

    public static void showRefuseRefundGoodsDialog(final Activity context, final RefundsOrder refundsOrder) {
        TitleContentEditDialog dialog = new TitleContentEditDialog(
                context,
                "拒绝退货",
                "", "请输入拒绝原因，最多200字...",
                new TitleContentEditDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String etStr) {
                        refuseRefundGoods(
                                context,
                                refundsOrder.apiRefundOrderBean.applyRefundMoney,
                                refundsOrder.apiRefundOrderBean.refundCode,
                                refundsOrder.apiRefundOrderBean.orderCode,
                                etStr
                        );
                    }
                });
        dialog.show();
    }

    private static void refuseRefundGoods(Activity context, int applyRefundMoney, String refundCode, String orderCode, String remark) {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                orderService.refuseRefundGoods(
                        new RefundBody(refundCode, orderCode, remark, applyRefundMoney)),
                new BaseRequestListener<Object>(context) {
                    @Override
                    public void onSuccess(Object result) {
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                    }
                }
        );
    }

    public static void showRefuseRefundMoneyDialog(final Activity context, final RefundsOrder refundsOrder) {
        TitleContentEditDialog dialog = new TitleContentEditDialog(
                context,
                "拒绝退款",
                "", "请输入拒绝原因，最多200字...",
                new TitleContentEditDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String etStr) {
                        refuseRefundMoney(
                                context,
                                refundsOrder.apiRefundOrderBean.applyRefundMoney,
                                refundsOrder.apiRefundOrderBean.refundCode,
                                refundsOrder.apiRefundOrderBean.orderCode,
                                etStr
                        );
                    }
                });
        dialog.show();
    }

    private static void refuseRefundMoney(Activity context, int applyRefundMoney, String refundCode, String orderCode, String remark) {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                orderService.refuseRefundMoney(
                        new RefundBody(refundCode, orderCode, remark, applyRefundMoney)),
                new BaseRequestListener<Object>(context) {
                    @Override
                    public void onSuccess(Object result) {
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                    }
                }
        );
    }

    /**
     * 查看物流
     *
     * @param context
     * @param order
     */
    public static void viewExpress(Context context, Order order) {
        String expressCode;
        if (order.refundOrder != null && !StringUtils.isEmpty(order.refundOrder.refundGoodsExpressCode)) {
            expressCode = order.refundOrder.refundGoodsExpressCode;
        } else {
            expressCode = order.orderMain.expressCode;
        }
        if (expressCode.indexOf(",") == -1) {
            checkExpress(context, expressCode, order.orderMain.expressType);
        } else {
            showExpressDialog(context, expressCode, order.orderMain.expressType);
        }
    }

    /**
     * 显示多个快递号的弹窗
     *
     * @param expressCode
     */
    public static void showExpressDialog(Context context, String expressCode, String companyCode) {
        String[] split = expressCode.split(",");
        TextListDialog textListDialog = new TextListDialog(context);
        textListDialog.show();
        textListDialog.setData(split);
        textListDialog.setCompanyCode(companyCode);
    }

    /**
     * 查询快递
     *
     * @param context
     * @param expressCode
     */
    public static void checkExpress(Context context, String expressCode, String companyCode) {
//        ExpressManager.checkExpress(context,expressCode,"zhongtong");
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", String.format(Constants.EXPRESS_URL, companyCode, expressCode));
        context.startActivity(intent);
    }

    /**
     * 联系客服
     *
     * @param context
     * @param order
     */
    public static void contactCs(Context context, Order order) {
        CSUtils.start(context, "从订单点进来的，订单号:" + order.orderMain.orderCode);
    }

    public static void ship(Context context, Order order) {
        Intent intent = new Intent(context, ShipActivity.class);
        intent.putExtra("orderCode", order.orderMain.orderCode);
        context.startActivity(intent);
    }


    public static void cancelRefund(Activity context, Order order) {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                orderService.refundCancel(
                        order.orderMain.orderCode,
                        "取消退款"
                ),
                new BaseRequestListener<Object>(context) {
                    @Override
                    public void onSuccess(Object result) {
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_CANCEL_REFUNDS));
                    }
                }
        );
    }

    /**
     * 取消部分退款退货申请
     *
     * @param context
     * @param refundId
     */
    public static void cancelRefundExt(Activity context, String refundId) {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                orderService.refundCancelExt(
                        refundId,
                        "取消退款退货"
                ),
                new BaseRequestListener<Object>(context) {
                    @Override
                    public void onSuccess(Object result) {
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                    }
                }
        );
    }

    /**
     * 申请或者编辑 退货单
     *
     * @param context
     * @param orderCode
     * @param orderProducts 需要退款的商品列表
     * @param refundsId     如果是编辑退款单则传入退款单号，传入 null 则新增退款单
     */
    public static void addOrEditRefundOrder(Context context, String orderCode, List<OrderProduct> orderProducts, String refundsId) {
        Intent intent = new Intent(context, RefundGoodsActivity.class);
        intent.putExtra(Config.INTENT_KEY_ID, orderCode);
        ArrayList<String> ids = new ArrayList<>();
        long maxPrice = 0;
        for (OrderProduct data : orderProducts) {
            ids.add(data.order1Id);
            maxPrice += data.realtotal;
        }
        intent.putStringArrayListExtra("ids", ids);
        intent.putExtra("maxPrice", maxPrice);
        intent.putExtra("refundId", refundsId);
        context.startActivity(intent);
    }

    /**
     * 卖家退款的确认收货
     *
     * @param context
     * @param order
     */
    public static void finishOrder(final Context context, final RefundsOrder order) {
        final WJDialog dialog = new WJDialog(context);
        dialog.show();
        dialog.setTitle("确认收货");
        dialog.setContentText("确认收货后，待平台给买家退款");
        dialog.setCancelText("取消");
        dialog.setConfirmText("确定");
        dialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IOrderService service = ServiceManager.getInstance().createService(IOrderService.class);
                APIManager.startRequest(service.receiveOrder(new RefundBody(order.apiRefundOrderBean.refundCode, order.apiRefundOrderBean.orderCode, "", 0))
                        , new BaseRequestListener<Object>() {
                            @Override
                            public void onSuccess(Object result) {
                                dialog.dismiss();
                                ToastUtil.success("操作成功");
                                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                            }
                        });
            }
        });
    }

    /**
     * 买家正常的确认收货
     *
     * @param context
     * @param order
     */
    public static void finishOrder(final Context context, final Order order) {
        final WJDialog dialog = new WJDialog(context);
        dialog.show();
        dialog.setContentText("确认收货?");
        dialog.setCancelText("取消");
        dialog.setConfirmText("确定");
        dialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IOrderService service = ServiceManager.getInstance().createService(IOrderService.class);
                APIManager.startRequest(service.receiveOrder(order.orderMain.orderCode)
                        , new BaseRequestListener<Object>() {
                            @Override
                            public void onSuccess(Object result) {
                                dialog.dismiss();
                                ToastUtil.success("操作成功");
                                EventBus.getDefault().post(new EventMessage(Event.finishOrder));
                            }
                        });
            }
        });
    }

    public static void showCancelRefund(final Activity context, final Order order) {
        final WJDialog wjDialog = new WJDialog(context);
        wjDialog.show();
        wjDialog.setContentText("确定取消？");
        wjDialog.setCancelText("否");
        wjDialog.setConfirmText("是");
        wjDialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wjDialog.dismiss();
                cancelRefund(context, order);
            }
        });
    }

    public static void goGroupBuy(Context context, Order order) {
        Intent intent = new Intent(context, JoinGroupActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, JoinGroupActivity.TYPE_HOST);
        intent.putExtra(Config.INTENT_KEY_ID, order.groupInfo.groupCode);
        context.startActivity(intent);
    }


}
