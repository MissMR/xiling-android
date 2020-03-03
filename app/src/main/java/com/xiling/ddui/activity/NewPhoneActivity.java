package com.xiling.ddui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.constant.Event.UPDATEE_PHONE;

public class NewPhoneActivity extends BaseActivity implements CaptchaBtn.OnCountDownListener {
    INewUserService mUserService;
    @BindView(R.id.et_phone_number)
    TextView etPhoneNumber;
    @BindView(R.id.edit_mobile)
    EditText editMobile;
    @BindView(R.id.cb_captcha)
    CaptchaBtn cbCaptcha;
    @BindView(R.id.cb_btn_captcha_voice)
    TextView cbCaptchaVoice;
    @BindView(R.id.tv_btn_next)
    Button btnOk;

    private String phone, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_phone);
        ButterKnife.bind(this);
        setTitle("更换手机号");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initView();

    }

    private void initView() {
        cbCaptcha.setOnCountDownListener(this);
        cbCaptcha.setOnCountDownListener(this);
        btnOk.setEnabled(false);
        editMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editMobile.getText().toString().length() == 4) {
                    btnOk.setEnabled(true);
                } else {
                    btnOk.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.cb_captcha, R.id.cb_btn_captcha_voice, R.id.tv_btn_next})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.cb_captcha:
                phone = etPhoneNumber.getText().toString();
                getMemberInfoChangeMsg(phone, "0");
                break;
            case R.id.cb_btn_captcha_voice:
                cbCaptcha.start();
                cbCaptchaVoice.setVisibility(View.GONE);
                phone = etPhoneNumber.getText().toString();
                getMemberInfoChangeMsg(phone, "1");
                break;
            case R.id.tv_btn_next:
                phone = etPhoneNumber.getText().toString();
                code = editMobile.getText().toString();
                if (!PhoneNumberUtil.checkPhoneNumber(phone)) {
                    ToastUtil.error("请正确填写手机号");
                    return;
                }

                if (code.length() != 4) {
                    ToastUtil.error("请正确填写验证码");
                    return;
                }

                checkMember(code);
                break;
        }
    }


    @Override
    public void onCountDownFinish(CaptchaBtn view) {
        view.setText("重新获取");
        cbCaptchaVoice.setVisibility(View.VISIBLE);
    }


    /**
     * 获取验证码（更新手机号，设置交易密码，绑定银行卡使用）
     *
     * @param phone
     * @param sendType 0=短信 1=语音
     */
    private void getMemberInfoChangeMsg(String phone, String sendType) {
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(mUserService.getMemberMsgNewPhone(phone, token, sendType), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                cbCaptcha.start();
                ToastUtil.success(message);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.success(e.getMessage());
            }
        });
    }

    private void bindNewPhone(final String phone, String code) {
        APIManager.startRequest(mUserService.newPhoneBinding(phone, code), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                NewUserBean newUserBean = UserManager.getInstance().getUser();
                newUserBean.setPhone(phone);
                UserManager.getInstance().setUser(newUserBean);
                EventBus.getDefault().post(new EventMessage(UPDATEE_PHONE, phone));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    /**
     * 校验验证码
     */
    private void checkMember(final String code) {
        APIManager.startRequest(mUserService.checkMember(code), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                bindNewPhone(phone, code);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }
}
