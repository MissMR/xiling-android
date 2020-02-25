package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.SkuOrderAdapter;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.custom.popupwindow.CancelOrderDialog;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.module.community.DateUtils;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.ddui.config.H5UrlConfig.WEB_URL_EXPRESS;
import static com.xiling.shared.Constants.ORDER_CLOSED;
import static com.xiling.shared.Constants.ORDER_WAIT_AUDIT;
import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;
import static com.xiling.shared.constant.Event.CANCEL_ORDER;
import static com.xiling.shared.constant.Event.ORDER_OVERTIME;
import static com.xiling.shared.constant.Event.ORDER_RECEIVED_GOODS;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_ORDER;

/**
 * pt
 * 订单详情
 */
public class XLOrderDetailsActivity extends BaseActivity {
    public static final String ORDER_ID = "order_id";
    @BindView(R.id.tv_status_title)
    TextView tvStatusTitle;
    @BindView(R.id.tv_status_time)
    TextView tvStatusTime;
    @BindView(R.id.rel_status)
    RelativeLayout relStatus;
    @BindView(R.id.tv_contact_name)
    TextView tvContactName;
    @BindView(R.id.tv_contact_address)
    TextView tvContactAddress;
    @BindView(R.id.tv_contact_address_details)
    TextView tvContactAddressDetails;
    @BindView(R.id.recycler_sku)
    RecyclerView recyclerSku;
    @BindView(R.id.tv_price_total)
    TextView tvPriceTotal;
    @BindView(R.id.tv_price_freight)
    TextView tvPriceFreight;
    @BindView(R.id.tv_price_discount)
    TextView tvPriceDiscount;
    @BindView(R.id.tv_price_coupon)
    TextView tvPriceCoupon;
    @BindView(R.id.tv_price_actual)
    TextView tvPriceActual;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_order_create_time)
    TextView tvOrderCreateTime;
    @BindView(R.id.tv_order_pay_type)
    TextView tvOrderPayType;
    @BindView(R.id.tv_order_express_id)
    TextView tvOrderExpressId;
    @BindView(R.id.tv_order_complete_time)
    TextView tvOrderCompleteTime;
    @BindView(R.id.btn_see)
    TextView btnSee;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;
    @BindView(R.id.btn_remind)
    TextView btnRemind;
    @BindView(R.id.btm_cancel)
    TextView btmCancel;
    @BindView(R.id.btn_payment)
    TextView btnPayment;
    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.ll_count_down)
    LinearLayout llCountDown;
    @BindView(R.id.tv_warehouse_name)
    TextView tvWarehouseName;
    @BindView(R.id.btn_examine)
    TextView btnExamine;
    private String orderId;
    IOrderService mOrderService;
    XLOrderDetailsBean orderDetailsBean;

    public static void jumpOrderDetailsActivity(Context context, String orderId) {
        Intent intent = new Intent(context, XLOrderDetailsActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlorder_details);
        ButterKnife.bind(this);
        setTitle("订单详情");
        setLeftBlack();
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        initData();
    }

    private void initData() {
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra(ORDER_ID);
        }

        if (TextUtils.isEmpty(orderId)) {
            ToastUtil.error("订单编号为空");
            return;
        }
        getOrderDetails();
    }

    private void getOrderDetails() {
        APIManager.startRequest(mOrderService.getOrderDetails(orderId), new BaseRequestListener<XLOrderDetailsBean>(this) {
            @Override
            public void onSuccess(XLOrderDetailsBean result) {
                super.onSuccess(result);
                orderDetailsBean = result;
                tvWarehouseName.setText(orderDetailsBean.getStoreName());
                setStatus(result.getOrderStatusUs(), result);
                tvContactName.setText(result.getContactUsername());
                tvContactAddress.setText(result.getAddress());
                tvContactAddressDetails.setText(result.getContactDetail());
                recyclerSku.setLayoutManager(new LinearLayoutManager(context));
                SkuOrderAdapter skuAdapter = new SkuOrderAdapter();
                recyclerSku.setAdapter(skuAdapter);
                skuAdapter.setNewData(result.getDetails());

                tvPriceTotal.setText("¥ " + NumberHandler.reservedDecimalFor2(result.getGoodsTotalRetailPrice()));
                tvPriceFreight.setText("¥ " + NumberHandler.reservedDecimalFor2(result.getFreight()));
                tvPriceDiscount.setText("¥ " + NumberHandler.reservedDecimalFor2(result.getDiscountPrice()));
                tvPriceCoupon.setText("¥ " + NumberHandler.reservedDecimalFor2(result.getDiscountCoupon()));
                tvPriceActual.setText("¥ " + NumberHandler.reservedDecimalFor2(result.getPayMoney()));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 订单超时
     */
    private void orderOverTime() {
        finish();
        EventBus.getDefault().post(new EventMessage(ORDER_OVERTIME));
    }

    private void setStatus(String status, XLOrderDetailsBean orderDetailsBean) {
        switch (status) {
            case ORDER_WAIT_PAY:
                //待支付
                tvStatusTime.setVisibility(View.GONE);
                llCountDown.setVisibility(View.VISIBLE);
                startCountDown(orderDetailsBean.getWaitPayTimeMilli(), 1000, new OnCountDownListener() {
                    @Override
                    public void onTick(long l) {
                        DateUtils.setCountDownTimeStrng(l, tvHour, tvMinute, tvSecond);
                    }
                });

                relStatus.setBackgroundResource(R.drawable.bg_order_pay);
                tvStatusTitle.setText("等待买家付款");

                tvOrderId.setText("订单编号：" + orderDetailsBean.getOrderId());
                tvOrderCreateTime.setText("创建时间：" + orderDetailsBean.getCreateTime());

                btnSee.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                btnRemind.setVisibility(View.GONE);
                btnExamine.setVisibility(View.GONE);
                btnPayment.setVisibility(View.VISIBLE);
                btmCancel.setVisibility(View.VISIBLE);

                break;
            case ORDER_WAIT_SHIP:
                //待发货
                tvStatusTime.setText("该商品预计于" + 5 + "个工作日内发货");
                tvStatusTime.setVisibility(View.VISIBLE);
                llCountDown.setVisibility(View.GONE);

                relStatus.setBackgroundResource(R.drawable.bg_order_ship);
                tvStatusTitle.setText("等待平台发货");

                tvOrderId.setText("订单编号：" + orderDetailsBean.getOrderId());
                tvOrderCreateTime.setText("创建时间：" + orderDetailsBean.getCreateTime());
                tvOrderPayType.setText("支付方式：" + orderDetailsBean.getPayType() + " >");

                btnSee.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                btnRemind.setVisibility(View.VISIBLE);
                btnPayment.setVisibility(View.GONE);
                btmCancel.setVisibility(View.GONE);
                btnExamine.setVisibility(View.GONE);
                break;
            case ORDER_WAIT_AUDIT:
                //待审核
                tvStatusTime.setText("喜领客服尽快处理订单审核");
                tvStatusTime.setVisibility(View.VISIBLE);
                llCountDown.setVisibility(View.GONE);
                relStatus.setBackgroundResource(R.drawable.bg_order_audit);
                tvStatusTitle.setText("等待订单审核");

                tvOrderId.setText("订单编号：" + orderDetailsBean.getOrderId());
                tvOrderCreateTime.setText("创建时间：" + orderDetailsBean.getCreateTime());
                tvOrderPayType.setText("支付方式：" + orderDetailsBean.getPayType() + " >");

                btnSee.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                btnRemind.setVisibility(View.GONE);
                btnPayment.setVisibility(View.GONE);
                btmCancel.setVisibility(View.GONE);
                btnExamine.setVisibility(View.VISIBLE);
                break;
            case ORDER_WAIT_RECEIVED:
                //待收货
                String shipDate = orderDetailsBean.getShipDate();
                if (!TextUtils.isEmpty(shipDate)) {
                    long countDownTimne = DateUtils.getOrderFinishTime(shipDate);
                    String statusTime = DateUtils.getOrderFinishTimeString(countDownTimne);
                    tvStatusTime.setText(statusTime);
                    startCountDown(countDownTimne, 1000 * 60 * 60, new OnCountDownListener() {
                        @Override
                        public void onTick(long l) {
                            String statusTime = DateUtils.getOrderFinishTimeString(l);
                            tvStatusTime.setText(statusTime);
                        }
                    });
                }
                tvStatusTime.setVisibility(View.VISIBLE);
                llCountDown.setVisibility(View.GONE);

                relStatus.setBackgroundResource(R.drawable.bg_order_ship);
                tvStatusTitle.setText("等待买家验收");
                tvOrderId.setText("订单编号：" + orderDetailsBean.getOrderId());
                tvOrderCreateTime.setText("创建时间：" + orderDetailsBean.getCreateTime());
                tvOrderPayType.setText("支付方式：" + orderDetailsBean.getPayType() + " >");
                tvOrderExpressId.setText("物流单号：" + orderDetailsBean.getExpressId());

                btnSee.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnRemind.setVisibility(View.GONE);
                btnPayment.setVisibility(View.GONE);
                btmCancel.setVisibility(View.GONE);
                btnExamine.setVisibility(View.GONE);
                break;
            default:
                //订单已关闭
                tvStatusTime.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(orderDetailsBean.getBuyerRemark())) {
                    tvStatusTime.setText(orderDetailsBean.getBuyerRemark());
                } else {
                    tvStatusTime.setText("感谢您的支持");
                }
                llCountDown.setVisibility(View.GONE);
                relStatus.setBackgroundResource(R.drawable.bg_order_complete);
                tvStatusTitle.setText("订单已完成");

                tvOrderId.setText("订单编号：" + orderDetailsBean.getOrderId());
                tvOrderCreateTime.setText("创建时间：" + orderDetailsBean.getCreateTime());
                tvOrderPayType.setText("支付方式：" + orderDetailsBean.getPayType() + " >");

                if (orderDetailsBean.getExpressId() != 0) {
                    tvOrderExpressId.setText("物流单号：" + orderDetailsBean.getExpressId());
                }
                if (!TextUtils.isEmpty(orderDetailsBean.getDoneTime())) {
                    tvOrderCompleteTime.setText("完成时间：" + orderDetailsBean.getDoneTime());
                }


                btnSee.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                btnRemind.setVisibility(View.GONE);
                btnPayment.setVisibility(View.GONE);
                btmCancel.setVisibility(View.GONE);
                btnExamine.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 启动倒计时
     */
    CountDownTimer countDownTimer;

    private void startCountDown(long millisInFuture, long countDownInterval, final OnCountDownListener onCountDownListener) {
        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long l) {
                if (onCountDownListener != null) {
                    onCountDownListener.onTick(l);
                }
            }

            @Override
            public void onFinish() {
                orderOverTime();
            }
        };
        countDownTimer.start();
    }

    interface OnCountDownListener {
        void onTick(long l);
    }

    @OnClick({R.id.btn_see, R.id.btn_confirm, R.id.btn_remind, R.id.btm_cancel, R.id.btn_payment, R.id.btn_examine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_see:
                //查看物流
                String url = WEB_URL_EXPRESS.replace("@expressCode", orderDetailsBean.getExpressCode());
                url = url.replace("@expressId", orderDetailsBean.getExpressId() + "");
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra(Constants.Extras.WEB_URL, url)
                );
                break;
            case R.id.btn_confirm:
                confirmReceived(orderId);
                break;
            case R.id.btn_remind:
                remindDelivery(orderId);
                break;
            case R.id.btm_cancel:
                cancelOrder(orderId);
                break;
            case R.id.btn_payment:
                XLCashierActivity.jumpCashierActivity(context, PAY_TYPE_ORDER, orderDetailsBean.getPayMoney(), orderDetailsBean.getWaitPayTimeMilli(), orderDetailsBean.getOrderId());
                break;
            case R.id.btn_examine:
                //提醒审核
                remindAudit(orderDetailsBean.getOrderCode());
                break;
        }
    }

    /**
     * 提醒审核
     */
    private void remindAudit(String orderCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderCode", orderCode);
        APIManager.startRequest(mOrderService.remindAudit(APIManager.buildJsonBody(params)), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                ToastUtil.success("提醒成功");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    /**
     * 订单-确认收货
     */
    private void confirmReceived(String orderCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderCode", orderCode);
        APIManager.startRequest(mOrderService.confirmReceived(APIManager.buildJsonBody(params)), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                EventBus.getDefault().post(new EventMessage(ORDER_RECEIVED_GOODS));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    /**
     * 提醒发货
     */
    private void remindDelivery(String orderCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderCode", orderCode);
        APIManager.startRequest(mOrderService.remindDelivery(APIManager.buildJsonBody(params)), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                ToastUtil.success("已提醒");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    /**
     * 取消订单
     */
    CancelOrderDialog cancelOrderDialog;

    private void cancelOrder(final String orderCode) {
        if (cancelOrderDialog == null) {
            cancelOrderDialog = new CancelOrderDialog(context);
            cancelOrderDialog.setOnReasonSelectListener(new CancelOrderDialog.OnReasonSelectListener() {
                @Override
                public void onReasonSelected(String reason) {
                    ToastUtil.success(reason);
                    requestCancelOrder(orderCode, reason);
                }
            });
        }
        cancelOrderDialog.show();

    }

    /**
     * 取消订单请求
     *
     * @param orderCode
     * @param reason
     */
    private void requestCancelOrder(String orderCode, String reason) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderCode", orderCode);
        params.put("reason", reason);
        APIManager.startRequest(mOrderService.cancelOrder(APIManager.buildJsonBody(params)), new BaseRequestListener<Object>(context) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                EventBus.getDefault().post(new EventMessage(CANCEL_ORDER));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(EventMessage message) {
        switch (message.getEvent()) {
            case FINISH_ORDER:
                //付款完成，更新数据
                getOrderDetails();
                break;
        }
    }

}
