package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更换手机号-身份验证
 */

public class UpdatePhoneIdentityActivity extends BaseActivity {
    @BindView(R.id.nicknameEt)
    protected EditText mNicknameEt;
    @BindView(R.id.confirmBtn)
    TextView confirmBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_identity);
        ButterKnife.bind(this);
        setTitle("修改昵称");
        setLeftBlack();
        mNicknameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mNicknameEt.getText().toString().length() > 0) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }

            }
        });
    }


    @OnClick(R.id.confirmBtn)
    protected void onConfirm() {

        final String card = mNicknameEt.getText().toString();
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + card);

        INewUserService userService = ServiceManager.getInstance().createService(INewUserService.class);
        APIManager.startRequest(userService.getAuthentication(card, token), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                startActivity(new Intent(context, NewPhoneActivity.class));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdata(EventMessage message) {
        switch (message.getEvent()) {
            case UPDATEE_PHONE:
                finish();
                break;
        }
    }

}
