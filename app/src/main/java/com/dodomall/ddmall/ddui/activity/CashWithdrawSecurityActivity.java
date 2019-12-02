package com.dodomall.ddmall.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.UIEvent;
import com.dodomall.ddmall.ddui.bean.WithdrawBean;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/12
 * 提现 验证交易密码  安全问题
 */
public class CashWithdrawSecurityActivity extends BaseActivity {

    @BindView(R.id.tv_btn_next)
    TextView tvBtnNext;
    @BindView(R.id.et_trade_password)
    EditText etTradePassword;
    @BindView(R.id.et_secret)
    EditText etSecret;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.cb_password_switch)
    CheckBox cbPasswordSwitch;

    // 以分为单位
    private Long mMoney;
    private String mQuestion;
    private IUserService mIUserService;
    private WithdrawBean mWithdrawBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_withdraw_security);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        showHeader("提现");
        tvBtnNext.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBtnNext.setEnabled(!TextUtils.isEmpty(etSecret.getText()) && !TextUtils.isEmpty(etTradePassword.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etSecret.addTextChangedListener(textWatcher);
        etTradePassword.addTextChangedListener(textWatcher);

        cbPasswordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPasswordEditTextState(isChecked);
            }
        });

    }

    private void setPasswordEditTextState(boolean isShowPassword) {
        if (isShowPassword) {
            // 可见
            etTradePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); //字符
        } else {
            // 不可见
            etTradePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //字符
        }
        etTradePassword.setSelection(etTradePassword.getText().length());
    }

    private void initData() {
        mMoney = getIntent().getLongExtra(Constants.Extras.WITHDRAW, 0);
        mQuestion = getIntent().getStringExtra(Constants.Extras.QUESTION);
        if (mMoney == 0 || TextUtils.isEmpty(mQuestion)) {
            ToastUtil.error("缺少页面参数");
            return;
        }
        setSecurityQuestion(mQuestion);
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private void setSecurityQuestion(String question) {
        tvQuestion.setText("安全问题 " + question);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWithdrawInfo();
    }

    private void getWithdrawInfo() {
        APIManager.startRequest(mIUserService.getWithdrawInfo(), new BaseRequestListener<WithdrawBean>(this) {
            @Override
            public void onSuccess(WithdrawBean result) {
                super.onSuccess(result);
                setSecurityQuestion(result.getSafeProblem());
            }
        });
    }

    @OnClick(R.id.tv_btn_next)
    public void onViewClicked() {
        submitWithdraw();
    }

    private void submitWithdraw() {
        String password = StringUtil.md5(etTradePassword.getText().toString());
        APIManager.startRequest(mIUserService.addWithdraw(mMoney, password,
                etSecret.getText().toString()), new BaseRequestListener<String>(this) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                UIEvent uiEvent = new UIEvent();
                uiEvent.setType(UIEvent.Type.RefreshWithdraw);
                EventBus.getDefault().post(uiEvent);
                startActivity(new Intent(CashWithdrawSecurityActivity.this, CashWithdrawResultActivity.class)
                        .putExtra(Constants.Extras.DATA, result));

            }
        });
    }

    @OnClick({R.id.tv_btn_forget_trade_password, R.id.tv_btn_forget_secret})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_forget_trade_password:
                // 忘记交易密码
                startActivity(new Intent(this, AccessCaptchaActivity.class)
                        .putExtra(Constants.Extras.ROUTE, AccessCaptchaActivity.ROUTE_TRADE_PASSWORD));
                break;
            case R.id.tv_btn_forget_secret:
                // 忘记安全问题
                startActivity(new Intent(this, AccessCaptchaActivity.class)
                        .putExtra(Constants.Extras.ROUTE, AccessCaptchaActivity.ROUTE_SECURITY_QUESTION));
                break;
        }
    }
}
