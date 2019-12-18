package com.xiling.ddmall.module.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.activity.PayResultActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Order;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IOrderService;
import com.xiling.ddmall.shared.util.AliPayUtils;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.WePayUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.pay
 * @since 2017-06-09
 * 选择支付方式 弹出框
 * 1 正常支付流程 只是选择作用 业务还是在PayActivity 付款页面 处理 返回 支付方式
 * 2 订单列表or详情 去支付
 */
public class PayDialogActivity extends Activity {

    // 正常支付流程
    public static final String PARAM_KEY_ONLY_SELECT = "";

    private IOrderService mOrderService;
    private String mOrderCode;
    private Order mOrder;

    // 是否只是选择 不处理支付业务
    private boolean mIsOnlySelectPayWay = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        ButterKnife.bind(this);
        getIntentData();
        if (!mIsOnlySelectPayWay) {
            initData();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void initData() {
        WePayUtils.initWePay(this);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(mOrderService.getOrderByCode(mOrderCode), new BaseRequestListener<Order>(this) {
            @Override
            public void onSuccess(Order order) {
                mOrder = order;
//                mPriceTv.setText(ConvertUtil.centToCurrency(PayDialogActivity.this, order.orderMain.totalMoney));
            }
        });
    }


    private void getIntentData() {
        Intent intent = getIntent();
        mIsOnlySelectPayWay = intent.hasExtra(PARAM_KEY_ONLY_SELECT);

        if (!mIsOnlySelectPayWay) {
            mOrderCode = intent.getStringExtra("orderCode");
            if (StringUtils.isEmpty(mOrderCode)) {
                ToastUtil.error("参数错误");
                finish();
                return;
            }
        }

    }

    protected void payOrder(int payWay) {
        if (mOrder == null) {
            return;
        }
        switch (payWay) {
            case AppTypes.PAY_TYPE.WECHAT:
                WePayUtils.wePay(this, mOrder.orderMain.totalMoney, mOrderCode);
                break;
            case AppTypes.PAY_TYPE.ALI:
                AliPayUtils.pay(this, ConvertUtil.cent2yuanNoZero(mOrder.orderMain.totalMoney), mOrderCode);
                break;
            default:
                ToastUtil.error("请选择支付方式");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayEvent(PayMsg msgStatus) {
        switch (msgStatus.getAction()) {
            case PayMsg.ACTION_ALIPAY_SUCCEED:
            case PayMsg.ACTION_WXPAY_SUCCEED:
                EventBus.getDefault().post(new EventMessage(Event.paySuccess));
                startPayResultActivity(true);
                ToastUtil.success("支付成功");
                finish();
                break;
            case PayMsg.ACTION_WXPAY_FAIL:
            case PayMsg.ACTION_ALIPAY_FAIL:
                startPayResultActivity(false);
                ToastUtils.showShortToast(msgStatus.message);
                finish();
                break;
            default:
        }
    }

    private void startPayResultActivity(boolean isPaySuccess) {
        PayResultActivity.start(this, mOrderCode, isPaySuccess);
    }

    @OnClick({R.id.iv_pay_ali, R.id.iv_pay_wechat})
    public void onViewClicked(View view) {
        int payWay = view.getId() == R.id.iv_pay_ali ? AppTypes.PAY_TYPE.ALI : AppTypes.PAY_TYPE.WECHAT;

        if (mIsOnlySelectPayWay) {
            setResult(RESULT_OK, new Intent().putExtra(Constants.Extras.PAY_TYPE, payWay));
            finish();
        } else {
            payOrder(payWay);
        }
    }

    @OnClick(R.id.tv_back)
    void onBackClicked() {
        finish();
    }
}
