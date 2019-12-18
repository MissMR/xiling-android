package com.xiling.ddmall.module.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.WeChatLoginModel;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.PushManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.UserService;
import com.xiling.ddmall.shared.service.contract.ICaptchaService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.CountDownRxUtils;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.UiUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author zjm
 * @date 2017/12/8
 */
public class OldLoginActivity extends BaseActivity {

    @BindView(R.id.ivLogo)
    ImageView mIvLogo;

    @BindView(R.id.etPhone)
    EditText mEtPhone;

    @BindView(R.id.etMsgCode)
    EditText mEtMsgCode;

    @BindView(R.id.tvGetMsg)
    TextView mTvGetMsg;

    @BindView(R.id.layoutMsgCode)
    RelativeLayout mLayoutMsgCode;

    @BindView(R.id.etPassword)
    EditText mEtPassword;

    @BindView(R.id.layoutPassword)
    RelativeLayout mLayoutPassword;

    @BindView(R.id.tvFindPassword)
    TextView mTvFindPassword;

    @BindView(R.id.tvLogin)
    TextView mTvLogin;

    @BindView(R.id.ivShowPassword)
    ImageView mIvShowPassword;

    @BindView(R.id.tvMsgCodeLogin)
    TextView mTvMsgCodeLogin;

    @BindView(R.id.tvRegister)
    TextView mTvRegister;

    public final static int TYPE_MSG_CODE = 1 << 0;
    public final static int TYPE_PASSWORD = 1 << 1;

    private IUserService mUserService;
    private int mType;
    private ICaptchaService mCaptchaService;
    private boolean isUnregister = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEtPhone.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mType == TYPE_PASSWORD) {
            isUnregister = false;
        }
    }

    private void initData() {
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
    }

    private void initView() {
        setTitleNoLine();
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setBarPadingHeight(QMUIStatusBarHelper.getStatusbarHeight(this));
        setBarPadingColor(Color.WHITE);

        switch (mType) {
            case TYPE_MSG_CODE:
                setTitle("验证码登录");
                setLeftBlack();
                mLayoutMsgCode.setVisibility(View.VISIBLE);
                mLayoutPassword.setVisibility(View.GONE);
                mTvMsgCodeLogin.setVisibility(View.GONE);
                break;
            case TYPE_PASSWORD:
                setTitle("密码登录");
                getHeaderLayout().setRightDrawable(R.drawable.ic_login_close);
                getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                mLayoutMsgCode.setVisibility(View.GONE);
                mLayoutPassword.setVisibility(View.VISIBLE);
                mTvMsgCodeLogin.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void getIntentData() {
        mType = getIntent().getIntExtra(Config.INTENT_KEY_TYPE_NAME, TYPE_PASSWORD);
    }


    private void loginByPassword(String phone, String password) {
        if (StringUtils.isEmpty(phone)) {
            phone = mEtPhone.getText().toString();
        }
        if (StringUtils.isEmpty(password)) {
            password = mEtPassword.getText().toString();
        }
        if (StringUtils.isEmpty(password)) {
            ToastUtil.error("请输入密码");
            return;
        }
        password = StringUtil.md5(password);
        APIManager.startRequest(mUserService.login(phone, password), new BaseRequestListener<User>(this) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                loginSucceed(user);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void loginSucceed(User user) {
        UserService.login(user);
        PushManager.setJPushInfo(this, user);
        EventBus.getDefault().post(new EventMessage(Event.loginSuccess));
    }

    private void loginByMsgCode() {
        String msgCode = mEtMsgCode.getText().toString();
        if (StringUtils.isEmpty(msgCode)) {
            ToastUtil.error("请输入验证码");
            return;
        }
        APIManager.startRequest(mUserService.login(mEtPhone.getText().toString(), msgCode), new BaseRequestListener<User>(this) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                loginSucceed(user);
            }
        });
    }

    @OnClick(R.id.ivShowPassword)
    public void onMIvShowPasswordClicked() {
        mIvShowPassword.setSelected(!mIvShowPassword.isSelected());
        if (mIvShowPassword.isSelected()) {
            mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        mEtPassword.setSelection(mEtPassword.length());
    }

    @OnClick(R.id.tvMsgCodeLogin)
    public void onMTvPasswordClicked() {
        isUnregister = true;
        Intent intent = new Intent(this, OldLoginActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, TYPE_MSG_CODE);
        startActivity(intent);
    }

    @OnClick(R.id.tvFindPassword)
    public void onMTvFindPasswordClicked() {
        startActivity(new Intent(this, FindPasswordActivity.class));
    }

    @OnClick(R.id.tvGetMsg)
    public void onViewClicked() {
        if (UiUtils.checkETPhone(mEtPhone)) {
            APIManager.startRequest(mCaptchaService.getCaptchaForRegister(mEtPhone.getText().toString()), new BaseRequestListener<Object>(this) {

                @Override
                public void onSuccess(Object result) {
                    CountDownRxUtils.textViewCountDown(mTvGetMsg, 60, "获取验证码");
                }
            });
        }
    }

    @OnClick(R.id.tvRegister)
    public void onGoRegister() {
        startActivity(new Intent(this, NewRegisterActivity.class));
    }

    @OnClick(R.id.tvLogin)
    public void onMTvLoginClicked() {
        if (UiUtils.checkETPhone(mEtPhone)) {
            switch (mType) {
                case TYPE_MSG_CODE:
                    loginByMsgCode();
                    break;
                case TYPE_PASSWORD:
                    loginByPassword(null, null);
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess)) {
            finish();
        } else if (message.getEvent().equals(Event.wxLoginSuccess) && !isUnregister) {
            getAccessToken((String) message.getData());
        } else if (message.getEvent().equals(Event.wxLoginCancel)) {
            ToastUtil.hideLoading();
        }
    }

    private void getAccessToken(String code) {
        APIManager.startRequest(mUserService.getAccessToken(code), new BaseRequestListener<WeChatLoginModel>() {
            @Override
            public void onSuccess(WeChatLoginModel result) {
                super.onSuccess(result);
                if (result.registerStatus == 1) {
                    // 直接登录
                    loginByPassword(result.unionid, result.unionid);
                } else {
                    // 跳到信息完善
                    goWxRegister(result);
                    finish();
                }
            }

            private void goWxRegister(WeChatLoginModel weChatLoginModel) {
                Intent intent = new Intent(OldLoginActivity.this, WxRegisterActivity.class);
                intent.putExtra("data", weChatLoginModel);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.tvWxLogin)
    public void onViewClickedLogin() {
//        ToastUtil.showLoading(this);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        IWXAPI api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        api.sendReq(req);
    }
}
