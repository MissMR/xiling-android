package com.dodomall.ddmall.module.order;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.module.qrcode.ScanActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IOrderService;
import com.dodomall.ddmall.shared.util.PermissionsUtils;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;


public class ShipActivity extends BaseActivity {

    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.etCode)
    EditText mEtCode;
    @BindView(R.id.ivScan)
    ImageView mIvScan;
    @BindView(R.id.etRemark)
    EditText mEtRemark;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    private IOrderService mService;
    private String mOrderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);
        ButterKnife.bind(this);
        mOrderCode = getIntent().getStringExtra("orderCode");
        setTitle("扫描快递单号");
        setLeftBlack();
        EventBus.getDefault().register(this);
        mService = ServiceManager.getInstance().createService(IOrderService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.ivScan)
    public void onViewClicked() {
        new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(ShipActivity.this, ScanActivity.class));
                } else {
                    PermissionsUtils.goPermissionsSetting(ShipActivity.this);
                    ToastUtil.error("无法获得相机权限");
                }
            }
        });
    }

    @OnClick(R.id.tvSubmit)
    public void onSubmit() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderCode", mOrderCode);
        params.put("expressName", mEtName.getText().toString());
        params.put("expressCode", mEtCode.getText().toString());
        params.put("sellerRemark", mEtRemark.getText().toString());
        APIManager.startRequest(
                mService.shipOrder(APIManager.buildJsonBody(params)),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        ToastUtil.success("发货成功");
                        finish();
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_STORE_SHIT_SUCCEED));
                    }
                }
        );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MsgStatus status) {
        switch (status.getAction()) {
            case MsgStatus.ACTION_SCAN_SUCCEED:
                mEtCode.setText(status.getCode());
                break;
        }
    }


}
