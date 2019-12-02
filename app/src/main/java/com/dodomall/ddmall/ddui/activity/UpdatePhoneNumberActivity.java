package com.dodomall.ddmall.ddui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.component.CaptchaBtn;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICaptchaService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.ActivityController;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 修改手机号
 * 校验完老手机号  或  校验完当前身份了
 * 跳到当前页面 修改手机号
 */
public class UpdatePhoneNumberActivity extends BaseActivity implements CaptchaBtn.OnCountDownListener {

    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.cb_captcha)
    CaptchaBtn cbCaptcha;
    @BindView(R.id.cb_captcha_voice)
    CaptchaBtn cbCaptchaVoice;
    @BindView(R.id.tv_btn_next)
    TextView tvBtnNext;

    private ICaptchaService mCaptchaService;
    private IUserService mIUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_number);
        ButterKnife.bind(this);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        initView();
    }

    private void initView() {
        showHeader("修改绑定手机号", true);

        tvBtnNext.setEnabled(false);

        cbCaptchaVoice.setOnCountDownListener(this);
        cbCaptcha.setOnCountDownListener(this);

        setEditTextListener();

    }


    @OnClick({R.id.cb_captcha, R.id.cb_captcha_voice, R.id.tv_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_captcha:
                getUpdatePhoneCaptcha(ICaptchaService.TYPE_MESSAGE);
                break;
            case R.id.cb_captcha_voice:
                getUpdatePhoneCaptcha(ICaptchaService.TYPE_VOICE);
                break;
            case R.id.tv_btn_next:
                checkPhoneExist();
                break;
        }
    }

    private void getUpdatePhoneCaptcha(final int type) {
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + etPhoneNumber.getText().toString());
        APIManager.startRequest(mCaptchaService.getCaptchaForUpdatePhone(token, etPhoneNumber.getText().toString(), type),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        if (type == ICaptchaService.TYPE_MESSAGE) {
                            cbCaptchaVoice.setVisibility(View.GONE);
                            cbCaptcha.start();
                        } else {
                            cbCaptcha.setVisibility(View.GONE);
                            cbCaptchaVoice.start();
                        }
                        ToastUtil.success("验证码发送成功");
                    }
                });
    }

    private void checkPhoneExist() {
        String phoneNumber = etPhoneNumber.getText().toString();
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phoneNumber);
        APIManager.startRequest(mIUserService.checkPhoneExist(phoneNumber, token),
                new BaseRequestListener<HashMap<String, Integer>>(this) {
                    @Override
                    public void onSuccess(HashMap<String, Integer> result, String msg) {
                        super.onSuccess(result);
                        if (result.containsKey("registerStatus") && result.get("registerStatus") == 1) {
                            // 手机号未绑定
                            updatePhoneNumber();
                        } else {
                            ToastUtil.error("手机号已存在");
                        }

                    }
                });

    }

    private void updatePhoneNumber() {
        APIManager.startRequest(mIUserService.updatePhoneNumber(etPhoneNumber.getText().toString(), etCaptcha.getText().toString()),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        ToastUtil.success("更换手机号成功~");
                        updateLocalPhoneNumber();
                        goBack();
                    }
                });
    }

    private void goBack() {
        if (ActivityController.getInstance().isActivityAlive(UserSettingsActivity.class)) {
            UserSettingsActivity.goBack(this);
        } else {
            MainActivity.goBack(this);
        }
    }

    private void updateLocalPhoneNumber() {
        User user = SessionUtil.getInstance().getLoginUser();
        user.phone = etPhoneNumber.getText().toString();
        SessionUtil.getInstance().setLoginUser(user);
    }

    @Override
    public void onCountDownFinish(CaptchaBtn view) {
        switch (view.getId()) {
            case R.id.cb_captcha:
                cbCaptchaVoice.setVisibility(View.VISIBLE);
                break;
            case R.id.cb_captcha_voice:
                cbCaptcha.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setEditTextListener() {
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBtnNext.setEnabled(s.length() == 11 && etCaptcha.getText().length() == 4);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && etPhoneNumber.getText().length() != 11) {
                    ToastUtil.error("请填写您的真实手机号");
                }
            }
        });
        etCaptcha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBtnNext.setEnabled(s.length() == 4 && etPhoneNumber.getText().length() == 11);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
