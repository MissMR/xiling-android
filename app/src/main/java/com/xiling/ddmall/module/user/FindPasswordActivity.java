package com.xiling.ddmall.module.user;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.component.CaptchaBtn;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICaptchaService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.ValidateUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPasswordActivity extends BaseActivity {


    @BindView(R.id.phoneEt)
    EditText mPhoneEt;
    @BindView(R.id.captchaEt)
    EditText mCaptchaEt;
    @BindView(R.id.captchaBtn)
    CaptchaBtn mCaptchaBtn;
    @BindView(R.id.passwordEt)
    EditText mPasswordEt;
    @BindView(R.id.confirmedEt)
    EditText mConfirmedEt;
    @BindView(R.id.registerBtn)
    TextView mRegisterBtn;

    private IUserService mUserService;
    private ICaptchaService mCaptchaService;
    private boolean mIsSetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_passwprd);
        ButterKnife.bind(this);
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mIsSetPassword = getIntent().getBooleanExtra("isSetPassword", false);
        setTitle(mIsSetPassword ? "设置密码" : "找回密码");
    }


    @OnClick(R.id.registerBtn)
    protected void onRegister() {
        String phone = mPhoneEt.getText().toString();
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            mPhoneEt.requestFocus();
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            mPhoneEt.requestFocus();
            return;
        }
        String captcha = mCaptchaEt.getText().toString();
        if (Strings.isNullOrEmpty(captcha)) {
            ToastUtil.error("请输入验证码");
            mCaptchaEt.requestFocus();
            return;
        }
        String password = mPasswordEt.getText().toString();
        String confirmed = mConfirmedEt.getText().toString();
        if (Strings.isNullOrEmpty(password)) {
            ToastUtil.error("请输入密码");
            mPasswordEt.requestFocus();
            return;
        }
        if (Strings.isNullOrEmpty(confirmed)) {
            ToastUtil.error("请再次输入密码");
            mConfirmedEt.requestFocus();
            return;
        }
        if (!confirmed.equals(password)) {
            ToastUtil.error("两次输入的密码不一致");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("newPass", StringUtil.md5(password));
        params.put("checkNumber", captcha);
        ToastUtil.showLoading(this);
        APIManager.startRequest(mUserService.putPassword(params), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("操作成功");
                finish();
            }
        });
    }

    @OnClick(R.id.captchaBtn)
    protected void getCaptcha() {
        String phone = mPhoneEt.getText().toString();
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            mPhoneEt.requestFocus();
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            mPhoneEt.requestFocus();
            return;
        }
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(mCaptchaService.getCaptchaForUpdate(token, phone), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                mCaptchaBtn.start();
            }

        });
    }
}
