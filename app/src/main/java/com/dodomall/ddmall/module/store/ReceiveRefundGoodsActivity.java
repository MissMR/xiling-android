package com.dodomall.ddmall.module.store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.Config;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.UploadResponse;
import com.dodomall.ddmall.shared.bean.body.ReceiveRefundGoodsBody;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.common.ImageUploadAdapter;
import com.dodomall.ddmall.shared.component.DecimalEditText;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.manager.UploadManager;
import com.dodomall.ddmall.shared.service.contract.IOrderService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReceiveRefundGoodsActivity extends BaseActivity {

    @BindView(R.id.maxMoneyTv)
    TextView mMaxMoneyTv;
    @BindView(R.id.moneyEt)
    DecimalEditText mMoneyEt;
    @BindView(R.id.refundRemarkEt)
    EditText mRefundRemarkEt;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.submitBtn)
    TextView mSubmitBtn;
    private String mOrderCode;
    private ImageUploadAdapter mImageUploadAdapter;
    private long mMaxPrice;
    private IOrderService mOrderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_refund_goods);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        EventBus.getDefault().register(this);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        setTitle("确认退货");
        setLeftBlack();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mImageUploadAdapter = new ImageUploadAdapter(this, 4);
        mRecyclerView.setAdapter(mImageUploadAdapter);

        mMaxMoneyTv.setText(String.format("（最多 %s 元）", ConvertUtil.cent2yuanNoZero(mMaxPrice)));
        mMoneyEt.setMaxValue(mMaxPrice / 100 + 1);
    }

    private void getIntentData() {
        mOrderCode = getIntent().getStringExtra("orderCode");
        mMaxPrice = getIntent().getLongExtra("maxPrice", 0);
        if (StringUtils.isEmpty(mOrderCode)) {
            ToastUtil.error("参数异常");
            finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT:
                    List<Uri> uris = Matisse.obtainResult(data);
                    LogUtils.e("拿到图片" + uris.get(0).getPath());
                    uploadImage(uris.get(0));
                    break;
            }
        }
    }

    @OnClick(R.id.submitBtn)
    public void onViewClicked() {
        String moneyStr = mMoneyEt.getText().toString();
        if (moneyStr.isEmpty()) {
            ToastUtil.error("请输入退货金额");
            return;
        }

        ReceiveRefundGoodsBody receiveRefundGoodsBody = new ReceiveRefundGoodsBody(
                mOrderCode,
                mRefundRemarkEt.getText().toString(),
                ConvertUtil.stringMoney2Long(mMoneyEt.getText().toString()),
                mImageUploadAdapter.getItems()
        );

        APIManager.startRequest(
                mOrderService.receiveRefundGoods(receiveRefundGoodsBody),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_REFUND_CHANGE));
                        finish();
                    }
                }
        );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageUploadHandler(EventMessage message) {
        if (message.getEvent().equals(Event.selectImage2Upload)) {
            UploadManager.selectImage(this, Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT, 1);
        }
    }
}
