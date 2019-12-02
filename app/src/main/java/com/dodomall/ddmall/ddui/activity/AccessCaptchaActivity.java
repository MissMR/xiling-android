package com.dodomall.ddmall.ddui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;

import com.dodomall.ddmall.ddui.bean.UserAuthBean;

import com.dodomall.ddmall.ddui.bean.UIEvent;

import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.component.CaptchaBtn;
import com.dodomall.ddmall.shared.component.dialog.DDMDialog;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICaptchaService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 修改手机号  获取当前手机号的验证码页面
 */
public class AccessCaptchaActivity extends BaseActivity implements CaptchaBtn.OnCountDownListener {

    private static SparseArray<Class> mRouteArray;
    // 换手机号 验证
    public static final int ROUTE_UPDATE_PHONE_NUMBER = 0;
    // 安全问题 验证
    public static final int ROUTE_SECURITY_QUESTION = 1;
    // 交易密码 验证
    public static final int ROUTE_TRADE_PASSWORD = 2;

    static {
        mRouteArray = new SparseArray<>();
        mRouteArray.put(ROUTE_UPDATE_PHONE_NUMBER, UpdatePhoneNumberActivity.class);
        mRouteArray.put(ROUTE_SECURITY_QUESTION, EditSecurityQuestionActivity.class);
        mRouteArray.put(ROUTE_TRADE_PASSWORD, TradePasswordActivity.class);
    }

    @BindView(R.id.tv_btn_forget_phone_number)
    TextView mTvForgetPhoneNumber;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    // 获取手机验证码  重新获取  59s
    @BindView(R.id.cb_captcha)
    CaptchaBtn cbCaptcha;
    @BindView(R.id.cb_btn_captcha_voice)
    CaptchaBtn cbCaptchaVoice;
    @BindView(R.id.tv_btn_next)
    TextView tvBtnNext;

    private int mRoute = 0;
    private ICaptchaService mCaptchaService;
    private IUserService mIUserService;
    private String mPhoneNumber;

    private int mParamSendType = ICaptchaService.SEND_TYPE_SMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_captcha);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mPhoneNumber = SessionUtil.getInstance().getLoginUser().phone;
        mRoute = getIntent().getIntExtra(Constants.Extras.ROUTE, 0);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private void initView() {

        mTvForgetPhoneNumber.setVisibility(mRoute == ROUTE_UPDATE_PHONE_NUMBER ? View.VISIBLE : View.INVISIBLE);

        showHeader("手机验证", true);
        tvPhoneNumber.setText(SessionUtil.getInstance().getLoginUser().getSecretPhoneNumber());
        cbCaptcha.setOnCountDownListener(this);
        cbCaptchaVoice.setOnCountDownListener(this);

        tvBtnNext.setEnabled(false);
        etCaptcha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBtnNext.setEnabled(s.length() == 4);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCaptcha.requestFocus();
        //出键盘
        showSoftInputFromWindow(etCaptcha);
    }

    @OnClick({R.id.cb_captcha, R.id.tv_btn_forget_phone_number, R.id.cb_btn_captcha_voice, R.id.tv_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_forget_phone_number:
                getAuthInfo();
                break;
            case R.id.cb_captcha:
                mParamSendType = ICaptchaService.SEND_TYPE_SMS;
                cbCaptcha.start();
                cbCaptchaVoice.setVisibility(View.GONE);
                getCaptcha();
                break;
            case R.id.cb_btn_captcha_voice:
                mParamSendType = ICaptchaService.SEND_TYPE_VOICE;
                cbCaptchaVoice.start();
                cbCaptcha.setVisibility(View.GONE);
                getCaptcha();
                break;
            case R.id.tv_btn_next:
                checkCaptcha();
                break;
        }
    }


    @Override
    public void onCountDownFinish(CaptchaBtn view) {
        switch (view.getId()) {
            case R.id.cb_captcha:
                cbCaptchaVoice.setVisibility(View.VISIBLE);
                break;
            case R.id.cb_btn_captcha_voice:
                cbCaptcha.setVisibility(View.VISIBLE);
                break;
        }
        view.setText("重新获取");
    }


    private void getAuthInfo() {
        APIManager.startRequest(mIUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>(this) {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                if (result.getAuthStatus() == 2) {
                    // 实名认证通过  去验证身份
                    startActivity(new Intent(AccessCaptchaActivity.this, CheckIdentityActivity.class));
                } else {
                    showAlertDialog(result.getAuthStatus());
                }
            }
        });
    }

    //    status 审核状态,0:未提交;1:等待审核;2:审核通过;3:审核失败,int,默认为0
    private void showAlertDialog(final int authStatus) {
        String content = "";
        String positiveText = "确定";
        switch (authStatus) {
            case 0:
                content = "您还未进行实名认证哦~";
                positiveText = "去认证";
                break;
            case 1:
                content = "您的实名认证还在审核哦~";
                break;
            case 3:
                content = "您的实名认证审核失败~";
                positiveText = "去认证";
                break;
        }
        final Dialog dialog = new DDMDialog(this).setTitle("提示")
                .setContent(content).setNegativeButton("取消", null).setPositiveButton(positiveText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (authStatus == 0 || authStatus >= 3) {
                            startActivity(new Intent(AccessCaptchaActivity.this, AuthActivity.class));
                        }
                    }
                });
        dialog.show();
    }

    private void getCaptcha() {
        APIManager.startRequest(getApiObervable(), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result, msg);
                ToastUtil.success(msg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error("获取验证码失败");
            }
        });
    }

    private void checkCaptcha() {
        String token = StringUtil.md5(mPhoneNumber + BuildConfig.TOKEN_SALT + etCaptcha.getText().toString() + ICaptchaService.CODE_TYPE_DEFAULT);
        APIManager.startRequest(mCaptchaService.checkCaptcha(mPhoneNumber, ICaptchaService.CODE_TYPE_DEFAULT, etCaptcha.getText().toString(), token),
                new BaseRequestListener<HashMap<String, Integer>>(this) {
                    @Override
                    public void onSuccess(HashMap<String, Integer> result) {
                        super.onSuccess(result);
                        ToastUtil.success("验证成功");
                        startActivity(new Intent(AccessCaptchaActivity.this, mRouteArray.get(mRoute)));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.error("验证失败");
                    }
                });
    }

    private Observable<RequestResult<Object>> getApiObervable() {
        Observable<RequestResult<Object>> resultObservable = null;
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + mPhoneNumber);
        switch (mRoute) {
            case ROUTE_UPDATE_PHONE_NUMBER:
            case ROUTE_TRADE_PASSWORD:
            case ROUTE_SECURITY_QUESTION:
                resultObservable = mCaptchaService.getUserCaptcha(token, mPhoneNumber, mParamSendType);
                break;
        }
        return resultObservable;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UIEvent event) {
        //需要重新刷新数据
        if (event.getType() == UIEvent.Type.CloseQuestionActivity || event.getType() == UIEvent.Type.ClosePasswordActivity) {
            finish();
        }
    }

}
