package com.xiling.ddmall.module.user;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.component.CaptchaBtn;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICaptchaService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.ValidateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.newPhoneEt)
    EditText mNewPhoneEt;
    @BindView(R.id.captchaEt)
    EditText mCaptchaEt;
    @BindView(R.id.captchaBtn)
    CaptchaBtn mCaptchaBtn;
    @BindView(R.id.passwordEt)
    EditText mPasswordEt;
    @BindView(R.id.rePasswordEt)
    EditText mRePasswordEt;
    @BindView(R.id.bindBtn)
    TextView mBindBtn;

    private IUserService mUserService;
    private ICaptchaService mCaptchaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        initTitle();
        initService();
    }

    private void initService() {
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private void initTitle() {
        showHeader("绑定手机号", true);
    }

    private Observable handingData() {
        final String phone = mNewPhoneEt.getText().toString();
        String captcha = mCaptchaEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        if (Strings.isNullOrEmpty(password)) {
            ToastUtil.error("请输入登录密码");
            mPasswordEt.requestFocus();
            return null;
        }
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            mNewPhoneEt.requestFocus();
            return null;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            mNewPhoneEt.requestFocus();
            return null;
        }
        if (Strings.isNullOrEmpty(captcha)) {
            ToastUtil.error("请输入验证码");
            mCaptchaEt.requestFocus();
            return null;
        }
        return mUserService.bindPhone(phone, captcha, StringUtil.md5(password));
    }

    @OnClick(R.id.captchaBtn)
    protected void getCaptcha() {
        String phone = mNewPhoneEt.getText().toString();
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            mNewPhoneEt.requestFocus();
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            mNewPhoneEt.requestFocus();
            return;
        }
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(mCaptchaService.getCaptchaForUpdate(token, phone), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                mCaptchaBtn.start();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error("获取验证码失败");
            }
        });
    }

    @OnClick(R.id.bindBtn)
    protected void onBind() {
        final String phone = mNewPhoneEt.getText().toString();
        APIManager.startRequest(handingData(), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("绑定成功");
                User loginUser = SessionUtil.getInstance().getLoginUser();
                loginUser.phone = phone;
                SessionUtil.getInstance().setLoginUser(loginUser);
                finish();
            }
            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
            }
        });
    }
}
