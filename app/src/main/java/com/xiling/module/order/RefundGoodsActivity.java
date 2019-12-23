package com.xiling.module.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Order;
import com.xiling.shared.bean.OrderProduct;
import com.xiling.shared.bean.RefundsOrder;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.common.ImageUploadAdapter;
import com.xiling.shared.component.DecimalEditText;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.ValidateUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order
 * @since 2017-07-17
 * 提交申请退货页面
 */
public class RefundGoodsActivity extends BaseActivity {

    @BindView(R.id.reasonSpinner)
    protected Spinner mReasonSpinner;
    @BindView(R.id.maxMoneyTv)
    protected TextView mMaxMoneyTv;
    @BindView(R.id.moneyEt)
    protected DecimalEditText mMoneyEt;
    @BindView(R.id.refundRemarkEt)
    protected EditText mRefundRemarkEt;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    private IOrderService mOrderService;
    private ImageUploadAdapter mImageUploadAdapter;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private String mOrderCode;
    private ArrayList<String> mIds;
    private String mRefundId;
    /**
     * 最大退款金额
     */
    private long mMaxRefundMoney;
    /**
     * 是否整单退款
     */
    private boolean isOrderRefund;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_goods);
        ButterKnife.bind(this);

        getIntentData();
        initView();
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        initData();
    }

    private void initView() {
        setTitle("申请退货");
        setLeftBlack();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mImageUploadAdapter = new

                ImageUploadAdapter(this, 4);
        mRecyclerView.setAdapter(mImageUploadAdapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mOrderCode = intent.getStringExtra(Config.INTENT_KEY_ID);
        mIds = intent.getStringArrayListExtra("ids");
        mRefundId = intent.getExtras().getString("refundId");
    }

    private void initData() {
        APIManager.startRequest(
                mOrderService.getOrderByCode(mOrderCode),
                new BaseRequestListener<Order>(this) {
                    @Override
                    public void onSuccess(Order order) {
                        mMaxRefundMoney = 0;
                        if (order.products.size() == mIds.size()) {//如果选择所有商品 整单退货
                            mMaxRefundMoney = order.orderMain.payMoney - order.orderMain.freight;
                            isOrderRefund = true;
                        } else {
                            isOrderRefund = false;
                            for (OrderProduct product : order.products) {
                                for (String id : mIds) {
                                    if (product.order1Id.equals(id)) {
                                        mMaxRefundMoney += product.realtotal;
                                    }
                                }
                            }
                        }
                        mMaxMoneyTv.setText(String.format("（最多 %s 元）", ConvertUtil.cent2yuanNoZero(mMaxRefundMoney)));
                        mMoneyEt.setText(ConvertUtil.cent2yuan(mMaxRefundMoney) + "");
                    }

                });
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
                mImageUploadAdapter.addItems(data.refundGoodsImage);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageUploadHandler(EventMessage message) {
        if (message.getEvent().equals(Event.selectImage2Upload)) {
            UploadManager.selectImage(this, REQUEST_CODE_CHOOSE_PHOTO, 1);
        }
    }

    private void uploadImage(final Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                mImageUploadAdapter.addItem(result.url);
            }
        });
    }

    @OnClick(R.id.submitBtn)
    protected void onSubmit() {
        String reason = mReasonSpinner.getSelectedItem().toString();
        if (reason.isEmpty()) {
            ToastUtil.error("请选择退货原因");
            return;
        }
        final String moneyStr = mMoneyEt.getText().toString();
        if (moneyStr.isEmpty()) {
            ToastUtil.error("请输入退货金额");
            return;
        }
        if (!ValidateUtil.isMoney(moneyStr)) {
            ToastUtil.error("金额格式不正确");
            return;
        }
        final long money = ConvertUtil.stringMoney2Long(moneyStr);
        if (money > mMaxRefundMoney) {
            ToastUtil.error(String.format("最多只能退货 %s 元", ConvertUtil.cent2yuanNoZero(mMaxRefundMoney)));
            return;
        }

        if (mImageUploadAdapter.getItemCount() == 0) {
            ToastUtil.error("请上传退货凭证");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("orderCode", mOrderCode);
        if (isOrderRefund) {
            params.put("refundType", 1);
        } else {
            params.put("refundType", 3);
        }
        params.put("refundReason", reason);
        params.put("remark", mRefundRemarkEt.getText().toString());
        params.put("refundMoney", money);
        params.put("images", mImageUploadAdapter.getItems());
        Observable<RequestResult<Object>> observable;
        if (StringUtils.isEmpty(mRefundId)) {
            params.put("order1Ids", mIds);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_PHOTO:
                    List<Uri> uris = Matisse.obtainResult(data);
                    LogUtils.e("拿到图片" + uris.get(0).getPath());
                    uploadImage(uris.get(0));
                    break;
            }
        }
    }
}
