package com.dodomall.ddmall.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.component.CaptchaBtn;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.UserService;
import com.dodomall.ddmall.shared.service.contract.ICaptchaService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.PhoneNumberUtil;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.google.common.base.Strings;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/8/24
 * 输入验证码界面
 * 若不传 wechatuser 则为登录操作
 */
public class CaptchaActivity extends BaseActivity {

    @BindView(R.id.et_pin_captcha)
    PinEntryEditText mPeEditText;
    @BindView(R.id.ib_next)
    ImageButton mIbNext;
    @BindView(R.id.tv_captcha)
    CaptchaBtn mTvCaptcha;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.tv_voice_captcha)
    TextView mTvVoiceCaptcha;

    private ICaptchaService mCaptchaService;
    private IUserService mUserService;
    private String mPhoneNumber;

    private boolean toLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);
        ButterKnife.bind(this);
        initData();
        initView();
        getCaptcha(ICaptchaService.TYPE_MESSAGE);
    }

    private void initData() {
        mPhoneNumber = getIntent().getStringExtra(Constants.Extras.PHONE_NUMBER);
        toLogin = null == getIntent().getSerializableExtra(Constants.Extras.WECHAT_USER);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private void initView() {
        showHeader("", true);
        setTitleNoLine();
        mTvTip.setText(String.format(mTvTip.getText().toString(), PhoneNumberUtil.getLast4Number(mPhoneNumber)));
        mIbNext.setEnabled(false);
        mPeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    mIbNext.setEnabled(true);
                    KeyboardUtils.hideSoftInput(CaptchaActivity.this);
                } else {
                    mIbNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTvCaptcha.start();
        mTvCaptcha.setOnCountDownListener(new CaptchaBtn.OnCountDownListener() {
            @Override
            public void onCountDownFinish(CaptchaBtn view) {
                mTvCaptcha.setVisibility(View.VISIBLE);
                mTvVoiceCaptcha.setVisibility(View.VISIBLE);
            }
        });
    }


    @OnClick(R.id.tv_voice_captcha)
    void onClickVoiceCaptcha() {
        mTvVoiceCaptcha.setVisibility(View.INVISIBLE);
        getCaptcha(ICaptchaService.TYPE_VOICE);
    }

    @OnClick(R.id.tv_captcha)
    protected void onClickCaptcha() {
        mTvVoiceCaptcha.setVisibility(View.INVISIBLE);
        getCaptcha(ICaptchaService.TYPE_MESSAGE);
    }

    private void getCaptcha(int type) {
        if (toLogin) {
            getLoginCaptcha(type);
        } else {
            getRegisterCaptcha(type);
        }
    }

    protected void getRegisterCaptcha(int type) {
        if (Strings.isNullOrEmpty(mPhoneNumber)) {
            ToastUtil.error("手机号为空");
            return;
        }
        mTvCaptcha.start();
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + mPhoneNumber);
        APIManager.startRequest(mCaptchaService.getCaptchaForRegister(token, mPhoneNumber, type), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result, msg);
                mTvCaptcha.start();
                ToastUtil.success(msg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error("获取验证码失败");
            }
        });
    }

    protected void getLoginCaptcha(int type) {
        if (Strings.isNullOrEmpty(mPhoneNumber)) {
            ToastUtil.error("手机号为空");
            return;
        }
        mTvCaptcha.start();
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + mPhoneNumber);
        APIManager.startRequest(mCaptchaService.getLoginCode(token, type, mPhoneNumber), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result, msg);
                mTvCaptcha.start();
                ToastUtil.success(msg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error("获取验证码失败");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess)) {
            finish();
        }
    }

    @OnClick(R.id.ib_next)
    protected void doNext() {

        int codeType = toLogin ? 1 : 0;
        String token = StringUtil.md5(mPhoneNumber + BuildConfig.TOKEN_SALT + mPeEditText.getText().toString() + codeType);
        APIManager.startRequest(mCaptchaService.checkCaptcha(mPhoneNumber, codeType, mPeEditText.getText().toString(), token),
                new BaseRequestListener<HashMap<String, Integer>>(this) {
                    @Override
                    public void onSuccess(HashMap<String, Integer> result) {
                        super.onSuccess(result);
                        if (toLogin) {
                            login();
                        } else {
                            ToastUtil.success("验证成功");
                            Intent intent = new Intent(CaptchaActivity.this, InviteCodeActivity.class);
                            intent.putExtra(Constants.Extras.WECHAT_USER, getIntent().getSerializableExtra(Constants.Extras.WECHAT_USER));
                            intent.putExtra(Constants.Extras.PHONE_NUMBER, mPhoneNumber);
                            intent.putExtra(Constants.Extras.REGISTER_CAPTCHA, mPeEditText.getText().toString());
                            startActivity(intent);
                        }

                    }
                });

    }

    private void login() {
        APIManager.startRequest(mUserService.login(mPhoneNumber, mPeEditText.getText().toString()), new BaseRequestListener<User>(this) {
            @Override
            public void onSuccess(User result) {
                super.onSuccess(result);
                UserService.loginSuccess(CaptchaActivity.this, result);
            }
        });
    }
}
