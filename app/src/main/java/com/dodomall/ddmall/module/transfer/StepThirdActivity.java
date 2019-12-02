package com.dodomall.ddmall.module.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.SubmitStatusActivity;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.component.CaptchaBtn;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICaptchaService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.transfer
 * @since 2017-08-03
 */
public class StepThirdActivity extends BaseActivity {

    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.captchaEt)
    EditText mCaptchaEt;
    @BindView(R.id.captchaBtn)
    CaptchaBtn mCaptchaBtn;
    @BindView(R.id.passwordEt)
    EditText mPasswordEt;
    @BindView(R.id.confirmBtn)
    TextView mConfirmBtn;
    private User mPayee;
    private double mMoney;
    private String mRemark;
    private User mUser;
    private ICaptchaService mCaptchaService;
    private IUserService mUserService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_transfer_step_third);
        ButterKnife.bind(this);
        showHeader();
        setTitle("转账");
        setLeftBlack();
        getIntentData();
        setData();
    }

    private void setData() {
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);

        mUser = SessionUtil.getInstance().getLoginUser();
        StringBuilder phoneSB = new StringBuilder(mUser.phone);
        String phone = phoneSB.replace(3, 7, "****").toString();
        mTvPhone.setText(phone);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mPayee = (User) intent.getExtras().getSerializable("payee");
            mMoney = intent.getExtras().getDouble("money");
            mRemark = intent.getExtras().getString("remark", "");
        }
        if (mPayee == null) {
            ToastUtil.error("参数错误");
            finish();
        }
    }

    private void doTransfer() {
        APIManager.startRequest(
                mUserService.doTransfer(
                        mPayee.phone,
                        Long.valueOf((long) ((mMoney + 0.005) * 100)),
                        mRemark,
                        StringUtil.md5(mPasswordEt.getText().toString()),
                        mCaptchaEt.getText().toString()
                ), new BaseRequestListener<Object>(this) {

                    @Override
                    public void onSuccess(Object result) {
                        finish();

                        Intent intent = new Intent(StepThirdActivity.this, SubmitStatusActivity.class);
                        startActivity(intent);
                        MsgStatus msgStatus = new MsgStatus(AppTypes.TRANSFER.TRANSFER_MONEY_SUCESS);
                        msgStatus.setTips(mPayee.nickname + "已收到你的转账");
                        msgStatus.setMoney(mMoney);
                        EventBus.getDefault().postSticky(msgStatus);

                        EventBus.getDefault().post(Event.transferSuccess);
                    }
                }
        );
    }

    @OnClick(R.id.captchaBtn)
    protected void getCaptcha() {
        String token = StringUtil.md5(Constants.API_SALT + mUser.phone);
        APIManager.startRequest(mCaptchaService.getCaptchaForCheck(token, mUser.phone), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                mCaptchaBtn.start();
            }
        });
    }

    @OnClick(R.id.confirmBtn)
    public void confirm() {
        String captcha = mCaptchaEt.getText().toString();
        if (Strings.isNullOrEmpty(captcha)) {
            ToastUtil.error("请输入验证码");
            mCaptchaEt.requestFocus();
            return;
        }
        String password = mPasswordEt.getText().toString();
        if (Strings.isNullOrEmpty(password)) {
            ToastUtil.error("请输入密码");
            mPasswordEt.requestFocus();
            return;
        }
        doTransfer();
    }
}
