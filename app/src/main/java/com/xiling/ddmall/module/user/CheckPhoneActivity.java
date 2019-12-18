package com.xiling.ddmall.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.WeChatLoginModel;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/8/24
 * 绑定手机号
 */
public class CheckPhoneActivity extends BaseActivity {

    @BindView(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @BindView(R.id.ib_next)
    Button mIbNext;
    @BindView(R.id.cb_agreement)
    CheckBox mCbAgreement;
    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;

    private IUserService mIUserService;
    private String loginType;

    // 根据前一页面传入的参数判断是什么操作 未传参数的话 是登录操作
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone);
        ButterKnife.bind(this);
        initData();
        initView();
//        mEtPhoneNumber.setText("13475323377");
//        mEtPhoneNumber.setText("15863222525");
        mEtPhoneNumber.setText("13412345678");
    }

    private void initData() {
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        loginType = getIntent().getStringExtra(Constants.Extras.LOGINTYPE);
    }

    private void initView() {
        showHeader("", true);
        setTitleNoLine();
        initAgreement();
        mIbNext.setEnabled(false);
        mEtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && charSequence.charAt(0) != '1') {
                    ToastUtil.error("请填写您的真实的手机号！");
                    return;
                }
                if (charSequence.length() == 11) {
                    // 限定11位 否则不可点击
                    mIbNext.setEnabled(true);
                    KeyboardUtils.hideSoftInput(CheckPhoneActivity.this);
                } else {
                    mIbNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initAgreement() {
//        mTvAgreement.setText(SpannableStringUtils.getBuilder("我已阅读并同意")
////                .append("《店多多用户协议》")
//                .setClickSpan(new ClickableSpan() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(CheckPhoneActivity.this, WebViewActivity.class);
//                        intent.putExtra("url", HtmlService.REGISTER_PROTOCOL);
//                        startActivity(intent);
//                    }
//
//                }).create());
//        mTvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess)) {
            finish();
        }
    }

    @OnClick(R.id.ib_next)
    public void onViewClicked() {
        if (!mCbAgreement.isChecked()) {
            ToastUtil.error("请勾选\"我已经认真阅读并同意《喜领服务协议》及《隐私协议》\"");
            return;
        }

        goNextStep();
//        ToastUtil.showLoading(this);
//        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + mEtPhoneNumber.getText().toString());
//        APIManager.startRequest(mIUserService.checkPhoneExist(mEtPhoneNumber.getText().toString(), token),
//                new BaseRequestListener<HashMap<String, Integer>>(this) {
//                    @Override
//                    public void onSuccess(HashMap<String, Integer> result, String msg) {
//                        super.onSuccess(result);
//                        if (result.containsKey("registerStatus") && result.get("registerStatus") == 1) {
//                            // 未注册
//                            if (isToLogin && null == mWeChatLoginModel) {
//                                // 登录流程 未授权 去走微信登录的流程
//                                sendWechatAuth();
//                            } else {
//                                goNextStep();
//                            }
//
//                        } else {
//                            // 注册过
//                            if (isToLogin) {
//                                goNextStep();
//                            } else {
//                                ToastUtil.error("手机号已存在");
//                            }
//                        }
//
//                    }
//                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatAuth(EventMessage message) {
        if (message.getEvent().equals(Event.wxLoginSuccess)) {
            getAccessToken((String) message.getData());
        } else if (message.getEvent().equals(Event.wxLoginCancel)) {
            ToastUtil.hideLoading();
            ToastUtil.error("登录取消");
        }
    }

    private void getAccessToken(String code) {
        APIManager.startRequest(mIUserService.getAccessToken(code), new BaseRequestListener<WeChatLoginModel>() {
            @Override
            public void onSuccess(WeChatLoginModel result) {
                super.onSuccess(result);
                ToastUtil.hideLoading();
                if (result.registerStatus == 1) {
                    // 注册过且已经完善信息了
                    ToastUtil.error("该微信已经注册过，请直接用微信登录！");
                } else {
                    // 未注册过
                    goNextStep();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
            }
        });
    }

    private void sendWechatAuth() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "" + System.currentTimeMillis();
        IWXAPI api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        api.sendReq(req);
    }

    private void goNextStep() {
        Intent intent = new Intent(CheckPhoneActivity.this, CaptchaActivity.class);
        intent.putExtra(Constants.Extras.LOGINTYPE,
                loginType);
        intent.putExtra(Constants.Extras.PHONE_NUMBER, mEtPhoneNumber.getText().toString());
        startActivity(intent);
    }

}
