package com.xiling.ddmall.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.google.common.base.Strings;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chan on 2017/6/9.
 */

public class EditPasswordActivity extends BaseActivity {

    @BindView(R.id.oldPasswordEt)
    protected EditText mOldPasswordEt;

    @BindView(R.id.newPasswordEt)
    protected EditText mNewPasswordEt;

    @BindView(R.id.surePasswordEt)
    protected EditText mSurePasswordEt;
    private IUserService mUserService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("修改密码");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.editBtn)
    protected void onEdit() {
        String oldPass = mOldPasswordEt.getText().toString();
        String newPass = mNewPasswordEt.getText().toString();
        String surePass = mSurePasswordEt.getText().toString();
        if (Strings.isNullOrEmpty(oldPass)) {
            ToastUtil.error("请输入原始密码");
            mOldPasswordEt.requestFocus();
            return;
        }
        if (Strings.isNullOrEmpty(newPass)) {
            ToastUtil.error("请输入新密码");
            mNewPasswordEt.requestFocus();
            return;
        }
        if (Strings.isNullOrEmpty(surePass)) {
            ToastUtil.error("请确认新密码");
            mSurePasswordEt.requestFocus();
            return;
        }
        if(!newPass.equals(surePass)){
            ToastUtil.error("两次密码输入不一致");
            mSurePasswordEt.requestFocus();

            return;
        }

        APIManager.startRequest(mUserService.editPassword(StringUtil.md5(oldPass), StringUtil.md5(newPass)), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("修改成功");
                // 清空 Token 退出登录，并跳转到登录页
//                UserService.logout();
//                startActivity(new Intent(EditPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}
