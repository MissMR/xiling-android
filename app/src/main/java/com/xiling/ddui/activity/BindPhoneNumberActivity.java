package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.SessionUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
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
        showHeader("我的手机号", true);
        tvPhoneNumber.setText(PhoneNumberUtil.getSecretPhoneNumber(UserManager.getInstance().getUser().getPhone()));
    }

    @OnClick(R.id.tv_btn_change_phone)
    public void onViewClicked() {
        startActivity(new Intent(this, AccessCaptchaActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdata(EventMessage message) {
        switch (message.getEvent()) {
            case UPDATEE_PHONE:
                tvPhoneNumber.setText(PhoneNumberUtil.getSecretPhoneNumber((String) message.getData()));
                break;
        }
    }
}
