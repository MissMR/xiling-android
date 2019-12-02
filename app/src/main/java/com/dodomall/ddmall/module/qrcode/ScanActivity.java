package com.dodomall.ddmall.module.qrcode;

import android.os.Bundle;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;


public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    @BindView(R.id.zbarview)
    ZBarView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        setTitle("扫描快递单号");
        setLeftBlack();
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        ToastUtil.success("扫描成功");
        MsgStatus msgStatus = new MsgStatus(MsgStatus.ACTION_SCAN_SUCCEED);
        msgStatus.setCode(result);
        EventBus.getDefault().post(msgStatus);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtil.error("打开相机出错");
    }
}
