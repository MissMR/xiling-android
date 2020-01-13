package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.SkuOrderAdapter;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.Constants.ORDER_WAIT_RECEIVED;
import static com.xiling.shared.Constants.ORDER_WAIT_SHIP;

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
    private String orderId;
    IOrderService mOrderService;

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


    private void setStatus(String status, XLOrderDetailsBean orderDetailsBean) {
        switch (status) {
            case ORDER_WAIT_PAY:
                relStatus.setBackgroundResource(R.drawable.bg_order_pay);
                tvStatusTitle.setText("等待买家付款");

                tvOrderId.setText("订单编号：" + orderDetailsBean.getOrderId());
                tvOrderCreateTime.setText("创建时间：" + orderDetailsBean.getCreateTime());

                btnSee.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                btnRemind.setVisibility(View.GONE);
                btnPayment.setVisibility(View.VISIBLE);
                btmCancel.setVisibility(View.VISIBLE);

                break;
            case ORDER_WAIT_SHIP:
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
                break;
            case ORDER_WAIT_RECEIVED:
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


                break;

            default:
                relStatus.setBackgroundResource(R.drawable.bg_order_complete);
                tvStatusTitle.setText("订单已完成");

                relStatus.setBackgroundResource(R.drawable.bg_order_ship);
                tvStatusTitle.setText("等待买家验收");

                tvOrderId.setText("订单编号：" + orderDetailsBean.getOrderId());
                tvOrderCreateTime.setText("创建时间：" + orderDetailsBean.getCreateTime());
                tvOrderPayType.setText("支付方式：" + orderDetailsBean.getPayType() + " >");
                tvOrderExpressId.setText("物流单号：" + orderDetailsBean.getExpressId());
                tvOrderCompleteTime.setText("完成时间："+orderDetailsBean.getDoneTime());

                btnSee.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                btnRemind.setVisibility(View.GONE);
                btnPayment.setVisibility(View.GONE);
                btmCancel.setVisibility(View.GONE);

                break;
        }
    }


    @OnClick({R.id.btn_see, R.id.btn_confirm, R.id.btn_remind, R.id.btm_cancel, R.id.btn_payment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_see:
                break;
            case R.id.btn_confirm:
                break;
            case R.id.btn_remind:
                break;
            case R.id.btm_cancel:
                break;
            case R.id.btn_payment:
                break;
        }
    }
}
