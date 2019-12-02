package com.dodomall.ddmall.module.order;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.module.qrcode.ScanActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.RefundsOrder;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IOrderService;
import com.dodomall.ddmall.shared.util.PermissionsUtils;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order
 * @since 2017-07-19
 * 退货 填写物流信息 页面
 */
public class RefundExpressActivity extends BaseActivity {

    @BindView(R.id.expressNameEt)
    protected EditText mExpressNameEt;
    @BindView(R.id.expressCodeEt)
    protected EditText mExpressCodeEt;

    private String mRefundId;
    private IOrderService mOrderService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_express);
        ButterKnife.bind(this);
        showHeader();
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mRefundId = intent.getExtras().getString("refundId");
        }
        if (mRefundId == null) {
            ToastUtil.error("参数错误");
            finish();
            return;
        }
        setTitle("填写物流信息");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initData();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(mOrderService.getRefundDetail(mRefundId), new BaseRequestListener<RefundsOrder>(this) {

            @Override
            public void onSuccess(RefundsOrder result) {
                super.onSuccess(result);
                if (result.apiRefundOrderBean.refundStatus == 2) {
                    String expressName = result.apiRefundOrderBean.refundGoodsExpressName;
                    final String expressCode = result.apiRefundOrderBean.refundGoodsExpressCode;
                    mExpressCodeEt.setText(expressCode);
                    mExpressNameEt.setText(expressName);
                }
            }
        });
    }

    @OnClick(R.id.ivScan)
    public void onViewClicked() {
        new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(RefundExpressActivity.this, ScanActivity.class));
                } else {
                    PermissionsUtils.goPermissionsSetting(RefundExpressActivity.this);
                    ToastUtil.error("无法获得相机权限");
                }
            }
        });
    }

    @OnClick(R.id.submitBtn)
    protected void onSubmit() {
        String expressName = mExpressNameEt.getText().toString();
        if (expressName.isEmpty()) {
            ToastUtil.error("请输入物流公司");
            return;
        }
        String expressCode = mExpressCodeEt.getText().toString();
        if (expressCode.isEmpty()) {
            ToastUtil.error("请输入快递单号");
            return;
        }

        APIManager.startRequest(mOrderService.refundExpress(mRefundId, expressName, expressCode), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                ToastUtil.success("提交成功");
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MsgStatus status) {
        switch (status.getAction()) {
            case MsgStatus.ACTION_SCAN_SUCCEED:
                mExpressCodeEt.setText(status.getCode());
                break;
            default:
        }
    }
}
