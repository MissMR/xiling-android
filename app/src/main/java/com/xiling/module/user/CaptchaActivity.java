package com.xiling.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseBean;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.UserService;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.WechatUtil;
import com.google.common.base.Strings;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 输入验证码界面
 * 若不传 wechatuser 则为登录操作
 */
public class CaptchaActivity extends BaseActivity {

    @BindView(R.id.et_pin_captcha)
    PinEntryEditText mPeEditText;

    @BindView(R.id.ib_next)
    Button mIbNext;

    @BindView(R.id.tv_captcha)
    CaptchaBtn mTvCaptcha;

    @BindView(R.id.tv_tip)
    TextView mTvTip;

    @BindView(R.id.cb_agreement)
    CheckBox mCbAgreement;

    private INewUserService iNewUserService;
    // private IUserService mUserService;
    private String mPhoneNumber;
    private String loginType;
    // 是否处理wechat回调  手机号登录 不处理回调
    private boolean canHandleWechatCallback = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);
        ButterKnife.bind(this);
        initData();
        initView();
        getphoneCode(0);
    }

    private void initData() {
        mPhoneNumber = getIntent().getStringExtra(Constants.Extras.PHONE_NUMBER);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        loginType = getIntent().getStringExtra(Constants.Extras.LOGINTYPE);
    }

    private void initView() {
        showHeader("", true);
        setTitleNoLine();
        showHeaderRightText("没有收到验证码？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCaptcha(ICaptchaService.TYPE_VOICE);
            }
        });
        mTvTip.setText(String.format(mTvTip.getText().toString(), PhoneNumberUtil.getSecretPhoneNumber(mPhoneNumber)));
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


        mTvCaptcha.setOnCountDownListener(new CaptchaBtn.OnCountDownListener() {
            @Override
            public void onCountDownFinish(CaptchaBtn view) {
                mTvCaptcha.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getCaptcha(int type) {
        getphoneCode(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        canHandleWechatCallback = true;
    }

    /**
     * 获取验证码
     * 0短信  1语音
     *
     * @param type
     */
    protected void getphoneCode(int type) {
        if (Strings.isNullOrEmpty(mPhoneNumber)) {
            ToastUtil.error("手机号为空");
            return;
        }
        APIManager.startRequest(iNewUserService.getLoginCode(type, mPhoneNumber), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result, msg);
                mTvCaptcha.start();
                ToastUtil.success(msg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.LOGIN_SUCCESS)) {
            finish();
        }
    }

    @OnClick(R.id.ib_next)
    protected void doNext() {
        if (!mCbAgreement.isChecked()) {
            ToastUtil.error("请勾选\"我已经认真阅读并同意《喜领服务协议》及《隐私协议》\"");
            return;
        }
        //根据登录方式  0  微信登录 1 手机号登录
        switch (loginType) {
            case "0":
                //微信登录 绑定手机号
                bindPhone();
                break;
            case "1":
                //手机要登录 验证验证码
                phoheLogin();
                break;
        }

    }

    @OnClick(R.id.tv_captcha)
    protected void onClickCaptcha() {
        getCaptcha(ICaptchaService.TYPE_MESSAGE);
    }

    @OnClick({R.id.tv_agreement, R.id.btn_ysfw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement:
                WebViewActivity.jumpUrl(context, "喜领服务协议", H5UrlConfig.SERVICE_AGREEMENT);
                break;
            case R.id.btn_ysfw:
                WebViewActivity.jumpUrl(context, "隐私协议", H5UrlConfig.PRIVACY_AGREEMENT);
                break;
        }
    }

    /**
     * 验证手机验证码
     */
    private void phoheLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", mPhoneNumber);
            jsonObject.put("code", mPeEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIManager.startRequest(iNewUserService.checkCaptcha(APIManager.getRequestBody(jsonObject.toString())),
                new BaseRequestListener<NewUserBean>(this) {
                    @Override
                    public void onSuccess(NewUserBean result) {
                        super.onSuccess(result);
                        //登录成功
                        UserManager.getInstance().loginSuccess(context, result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //登录失败
                        checkMessage(e.getMessage());
                    }
                });
    }

    /**
     * 微信绑定手机号
     */
    private void bindPhone() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", mPhoneNumber);
            jsonObject.put("code", mPeEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIManager.startRequest(iNewUserService.bindPhone(APIManager.getRequestBody(jsonObject.toString())),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        //微信绑定手机号成功，跳转绑定邀请码
                        if (result != null){
                            Intent intent = new Intent(CaptchaActivity.this, InviteCodeActivity.class);
                            intent.putExtra(Constants.Extras.WECHAT_USER, getIntent().getSerializableExtra(Constants.Extras.WECHAT_USER));
                            intent.putExtra(Constants.Extras.PHONE_NUMBER, mPhoneNumber);
                            intent.putExtra(Constants.Extras.REGISTER_CAPTCHA, mPeEditText.getText().toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //登录失败
                        checkMessage(e.getMessage());
                    }
                });
    }

    /**
     * 处理异常状态
     *
     * @param msg
     */
    private void checkMessage(String msg) {
        switch (msg) {
            case "uncheck-login":
                //跳转到登录注册页面
                finish();
                break;
            case "unbind-phone":
                //跳转到绑定手机号页面
                finish();
                break;
            case "unbind-wechat":
                //跳转到绑定微信页面
                if (WechatUtil.isWeChatAppInstalled(this)) {
                    sendWechatAuth();
                } else {
                    ToastUtil.error("请先安装微信客户端");
                }
                break;
            case "unbind-invite-code":
                //跳转到绑定邀请码页面
                Intent intent = new Intent(CaptchaActivity.this, InviteCodeActivity.class);
                startActivity(intent);
                break;
            default:
                ToastUtil.error(msg);
                break;
        }
    }

    /**
     * 微信登录
     */
    private void sendWechatAuth() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "" + System.currentTimeMillis();
        IWXAPI api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        api.sendReq(req);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatAuth(EventMessage message) {
        if (!canHandleWechatCallback) {
            return;
        }
        if (message.getEvent().equals(Event.wxLoginSuccess)) {
            //绑定微信成功
            String wxCode = (String) message.getData();
            bindWXCode(wxCode);
        } else if (message.getEvent().equals(Event.wxLoginCancel)) {
            ToastUtil.hideLoading();
            ToastUtil.error("登录取消");
        }
    }

    /**
     * 绑定微信code
     *
     * @param code
     */
    private void bindWXCode(String code) {
        APIManager.startRequest(iNewUserService.bindWXCode(code, "ANDROID"), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                ToastUtil.hideLoading();
                //手机号绑定微信，跳转绑定邀请码
                //  if (result) {
                Intent intent = new Intent(CaptchaActivity.this, InviteCodeActivity.class);
                startActivity(intent);
                // }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                checkMessage(e.getMessage());
            }
        });
    }
}
