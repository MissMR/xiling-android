package com.xiling.module.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.R;
import com.xiling.module.auth.SubmitStatusActivity;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.User;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

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
