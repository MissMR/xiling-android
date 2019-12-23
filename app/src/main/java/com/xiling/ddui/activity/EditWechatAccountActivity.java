package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ToastUtil;
import com.google.common.base.Strings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/12/8
 * 我的微信号
 */

public class EditWechatAccountActivity extends BaseActivity {

    @BindView(R.id.et_wechat)
    EditText mEtWechat;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    private String mCurrentWechat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wechat_account);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        showHeader("微信号");
        mCurrentWechat = getIntent().getExtras().getString(Constants.Extras.WECHAT);
        if (!TextUtils.isEmpty(mCurrentWechat)) {
            mEtWechat.setText(mCurrentWechat);
            mEtWechat.setSelection(mEtWechat.getText().length());
            mTvConfirm.setText("确认修改");
        }
        mEtWechat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvConfirm.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.tv_confirm)
    protected void onConfirm() {
        final String wechat = mEtWechat.getText().toString();
        if (Strings.isNullOrEmpty(wechat)) {
            ToastUtil.error("请输入新的昵称");
            return;
        }
        IUserService userService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(userService.addWechat(wechat), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                if (!TextUtils.isEmpty(mCurrentWechat)) {
                    ToastUtil.success("修改成功");
                }
                finish();
            }
        });
    }
}
