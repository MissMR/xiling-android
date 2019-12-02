package com.dodomall.ddmall.module.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.model.CardDetailModel;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.FrescoUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/18 上午11:54.
 * <p>
 * 银行卡绑定成功
 */
public class AuthCardSuccssdActivity extends BaseActivity {
    @BindView(R.id.ivBg)
    SimpleDraweeView mIvBg;
    @BindView(R.id.ivLogo)
    SimpleDraweeView mIvLogo;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvType)
    TextView mTvType;
    @BindView(R.id.tvNumber)
    TextView mTvNumber;
    @BindView(R.id.tvStatus)
    TextView mTvStatus;
    @BindView(R.id.tvReAuth)
    TextView mTvReAuth;

    private CardDetailModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_card_succssd);
        ButterKnife.bind(this);
        initData();
        setTitle("绑定银行卡");
        setLeftBlack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("回来了,清除 event");
        EventBus.getDefault().removeStickyEvent(CardDetailModel.class);
    }

    private void initData() {
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(service.getCard(), new BaseRequestListener<CardDetailModel>(this) {
            @Override
            public void onSuccess(CardDetailModel model) {
                mModel = model;
                setData();
            }
        });
    }

    private void setData() {
        mTvName.setText(mModel.bankName);
        String id = mModel.bankAccount;
        id = id.substring(mModel.bankAccount.length() - 4);
        mTvNumber.setText(String.format("**** **** **** %s",id));
        FrescoUtil.setImage(mIvLogo, mModel.bankLogo);
    }

    @OnClick(R.id.tvReAuth)
    public void onViewClicked() {
        Intent intent = new Intent(this, AuthPhoneActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME,Config.USER.INTENT_KEY_TYPE_AUTH_CARD);
        intent.putExtra("isEdit",true);
        startActivity(intent);
        finish();
    }
}
