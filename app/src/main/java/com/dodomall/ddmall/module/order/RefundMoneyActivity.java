package com.dodomall.ddmall.module.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Order;
import com.dodomall.ddmall.shared.bean.OrderProduct;
import com.dodomall.ddmall.shared.bean.RefundsOrder;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.component.DecimalEditText;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IOrderService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.dodomall.ddmall.shared.util.ValidateUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order
 * @since 2017-07-17
 * 提交申请退款界面
 */
public class RefundMoneyActivity extends BaseActivity {

    @BindView(R.id.reasonSpinner)
    protected Spinner mReasonSpinner;
    @BindView(R.id.maxMoneyTv)
    protected TextView mMaxMoneyTv;
    @BindView(R.id.moneyEt)
    protected DecimalEditText mMoneyEt;
    @BindView(R.id.refundRemarkEt)
    protected EditText mRefundRemarkEt;
    private Order mOrder;
    private String mOrderCode;
    private String mRefundId;
    private IOrderService mOrderService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_money);
        ButterKnife.bind(this);

        getIntentData();
        showHeader();
        setTitle("申请退款");
        setLeftBlack();
        initData();
    }

    private void initData() {
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(mOrderService.getOrderByCode(mOrderCode), new BaseRequestListener<Order>(this) {
            @Override
            public void onSuccess(Order result) {
                mOrder = result;
                String money = ConvertUtil.cent2yuanNoZero(mOrder.orderMain.payMoney);
                mMoneyEt.setText("" + money);
                mMaxMoneyTv.setText(String.format("（最多 %s 元）", money));
            }
        });
        String[] stringArray = getResources().getStringArray(R.array.refundReasons);
        LogUtils.e(stringArray[0]);
        if (StringUtils.isEmpty(mRefundId)) {
            return;
        }
        APIManager.startRequest(mOrderService.getRefundDetail(mRefundId), new BaseRequestListener<RefundsOrder>() {
            @Override
            public void onSuccess(RefundsOrder result) {
                super.onSuccess(result);
                RefundsOrder.ApiRefundOrderBeanEntity data = result.apiRefundOrderBean;
                mMoneyEt.setText(ConvertUtil.cent2yuan(data.applyRefundMoney) + "");
                mRefundRemarkEt.setText(data.refundRemark);

                String[] stringArray = getResources().getStringArray(R.array.refundReasons);
                mReasonSpinner.setSelection(2);
                for (int i = 0; i < stringArray.length; i++) {
                    if (stringArray[i].equals(data.refundReason)) {
                        mReasonSpinner.setSelection(i);
                    }
                }
            }
        });
    }

    private void getIntentData() {
        mOrderCode = getIntent().getStringExtra("orderCode");
        mRefundId = getIntent().getStringExtra("refundId");
    }

    @OnClick(R.id.submitBtn)
    protected void onSubmit() {
        String reason = mReasonSpinner.getSelectedItem().toString();
        if (reason.isEmpty()) {
            ToastUtil.error("请选择退款原因");
            return;
        }
        final String moneyStr = mMoneyEt.getText().toString();
        if (moneyStr.isEmpty()) {
            ToastUtil.error("请输入退款金额");
            return;
        }
        if (!ValidateUtil.isMoney(moneyStr)) {
            ToastUtil.error("金额格式不正确");
            return;
        }
        final long money = ConvertUtil.stringMoney2Long(moneyStr);
        if (money > mOrder.orderMain.payMoney) {
            ToastUtil.error(String.format("最多只能退款 %s 元", ConvertUtil.cent2yuanNoZero(mOrder.orderMain.payMoney)));
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("orderCode", mOrderCode);
        params.put("refundType", 2);
        params.put("refundReason", reason);
        params.put("remark", mRefundRemarkEt.getText().toString());
        params.put("refundMoney", money);
        Observable<RequestResult<Object>> observable;
        if (StringUtils.isEmpty(mRefundId)) {
            ArrayList<String> ids = new ArrayList<>();
            for (OrderProduct product : mOrder.products) {
                ids.add(product.order1Id);
            }
            params.put("order1Ids", ids);
            observable = mOrderService.refundOrderExt(APIManager.buildJsonBody(params));
        } else {
            params.put("refundId", mRefundId);
            observable = mOrderService.refundOrderExtEdit(APIManager.buildJsonBody(params));
        }
        APIManager.startRequest(observable, new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                // 发送通知
                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));

                ToastUtil.success("申请成功");
                finish();
            }
        });
    }
}
