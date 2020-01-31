package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易密码手机验证
 */
public class PasswordMobilePhoneActivity extends BaseActivity {

    @BindView(R.id.et_phone_number)
    TextView etPhoneNumber;
    @BindView(R.id.edit_mobile)
    EditText editMobile;
    INewUserService mUserService;

    private String phone, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_mobile_phone);
        ButterKnife.bind(this);
        setTitle("交易密码手机验证");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        phone = UserManager.getInstance().getUser().getPhone();
        etPhoneNumber.setText(PhoneNumberUtil.getSecretPhoneNumber(phone));
    }


    @OnClick({R.id.cb_captcha, R.id.cb_btn_captcha_voice, R.id.tv_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_captcha:
                if (!PhoneNumberUtil.checkPhoneNumber(phone)) {
                    ToastUtil.error("请填写您的真实手机号码");
                } else {
                    getMemberInfoChangeMsg(phone, "0");
                }
                break;
            case R.id.cb_btn_captcha_voice:
                if (!PhoneNumberUtil.checkPhoneNumber(phone)) {
                    ToastUtil.error("请填写您的真实手机号码");
                } else {
                    getMemberInfoChangeMsg(phone, "1");
                }
                break;
            case R.id.tv_btn_next:
                code = editMobile.getText().toString();
                if (!PhoneNumberUtil.checkPhoneNumber(phone)) {
                    ToastUtil.error("请填写您的真实手机号码");
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    ToastUtil.error("请填写验证码");
                    return;
                }
                checkMember(code);

                break;
        }
    }

    /**
     * 获取验证码（更新手机号，设置交易密码，绑定银行卡使用）
     *
     * @param phone
     * @param sendType 0=短信 1=语音
     */
    private void getMemberInfoChangeMsg(String phone, String sendType) {
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(mUserService.getMemberMsg(phone, token, sendType), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                ToastUtil.success(message);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.success(e.getMessage());
            }
        });
    }

    /**
     * 校验验证码
     */
    private void checkMember(String code) {
        APIManager.startRequest(mUserService.checkMember(code), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                startActivity(new Intent(context, PassWordInputActivity.class));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.success(e.getMessage());
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.UPDE_PASSWORD)) {
            finish();
        }
    }

}
