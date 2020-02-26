package com.xiling.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.xiling.R;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
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

    private String loginType;

    // 根据前一页面传入的参数判断是什么操作 未传参数的话 是登录操作
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        loginType = getIntent().getStringExtra(Constants.Extras.LOGINTYPE);
    }

    private void initView() {
        showHeader("", true);
        setTitleNoLine();
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.LOGIN_SUCCESS)) {
            finish();
        }
    }

    private void goNextStep() {
        Intent intent = new Intent(CheckPhoneActivity.this, CaptchaActivity.class);
        intent.putExtra(Constants.Extras.LOGINTYPE,
                loginType);
        intent.putExtra(Constants.Extras.PHONE_NUMBER, mEtPhoneNumber.getText().toString());
        startActivity(intent);
    }

    @OnClick({R.id.tv_agreement, R.id.btn_ysfw, R.id.ib_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement:
                WebViewActivity.jumpUrl(context,"喜领服务协议", H5UrlConfig.SERVICE_AGREEMENT);
                break;
            case R.id.btn_ysfw:
                WebViewActivity.jumpUrl(context,"隐私协议", H5UrlConfig.PRIVACY_AGREEMENT);
                break;
            case R.id.ib_next:
                if (!mCbAgreement.isChecked()) {
                    ToastUtil.error("请勾选\"我已经认真阅读并同意《喜领服务协议》及《隐私协议》\"");
                    return;
                }

                goNextStep();
                break;
        }
    }
}
