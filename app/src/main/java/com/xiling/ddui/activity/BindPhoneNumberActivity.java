package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.util.SessionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 更换手机号 展示当前手机号的页面
 */
public class BindPhoneNumberActivity extends BaseActivity {

    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_number);
        ButterKnife.bind(this);
        showHeader("更换手机号", true);
        tvPhoneNumber.setText(SessionUtil.getInstance().getLoginUser().getSecretPhoneNumber());
    }

    @OnClick(R.id.tv_btn_change_phone)
    public void onViewClicked() {
        startActivity(new Intent(this, AccessCaptchaActivity.class));
    }
}
