package com.xiling.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.ValidateUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chan on 2017/6/9.
 */

public class EditPhoneActivity extends BaseActivity {

    private ICaptchaService mCaptchaService;

    @BindView(R.id.oldPhoneTv)
    protected TextView mOldPhoneTv;

    @BindView(R.id.passwordEt)
    protected EditText mPasswordEt;

    @BindView(R.id.newPhoneEt)
    protected EditText mNewPhoneEt;

    @BindView(R.id.captchaEt)
    protected EditText mCaptchaEt;

    @BindView(R.id.captchaBtn)
    protected CaptchaBtn mCaptchaBtn;
    private IUserService mUserService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        ButterKnife.bind(this);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        String phone = getIntent().getExtras().getString("phone");
        if (phone == null) {
            ToastUtil.error("参数错误");
            finish();
        } else {
            mOldPhoneTv.setText(StringUtil.maskPhone(phone));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("修改绑定手机");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        APIManager.startRequest(mCaptchaService.getCaptchaForUpdatePhone(token, phone), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                mCaptchaBtn.start();
            }
        });
    }

    @OnClick(R.id.editBtn)
    protected void onEdit() {
        final String phone = mNewPhoneEt.getText().toString();
        String captcha = mCaptchaEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        if (Strings.isNullOrEmpty(password)) {
            ToastUtil.error("请输入登录密码");
            mPasswordEt.requestFocus();
            return;
        }
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
        if (Strings.isNullOrEmpty(captcha)) {
            ToastUtil.error("请输入验证码");
            mCaptchaEt.requestFocus();
            return;
        }
        APIManager.startRequest(mUserService.editPhone(phone, captcha, StringUtil.md5(password)), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("修改成功");
                SessionUtil.getInstance().logout();
                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_EDIT_PHONE));
                startActivity(new Intent(EditPhoneActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
            }
        });
    }
}
