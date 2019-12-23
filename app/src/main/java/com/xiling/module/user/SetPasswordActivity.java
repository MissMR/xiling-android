package com.xiling.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.User;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author zjm
 * @date 2017/12/14
 */
public class SetPasswordActivity extends BaseActivity {

    @BindView(R.id.newPasswordEt)
    protected EditText mNewPasswordEt;

    @BindView(R.id.surePasswordEt)
    protected EditText mSurePasswordEt;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.captchaEt)
    EditText mCaptchaEt;
    @BindView(R.id.captchaBtn)
    CaptchaBtn mCaptchaBtn;
    @BindView(R.id.editBtn)
    TextView mEditBtn;
    private IUserService mUserService;
    private User mLoginUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        initView();
        initData();
    }

    private void initView() {
        setTitle("设置密码");
        setLeftBlack();
    }

    private void initData() {
        if (!SessionUtil.getInstance().isLogin()) {
            ToastUtil.error("用户未登录");
            finish();
        }
        mLoginUser = SessionUtil.getInstance().getLoginUser();
        mTvPhone.setText(ConvertUtil.maskPhone(mLoginUser.phone));
    }

    @OnClick(R.id.captchaBtn)
    public void onViewClicked() {
        ICaptchaService service = ServiceManager.getInstance().createService(ICaptchaService.class);
        String phone = mLoginUser.phone;
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(service.getCaptchaForUpdate(token, phone), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                mCaptchaBtn.start();
            }

        });
    }

    @OnClick(R.id.editBtn)
    protected void onEdit() {
        String newPass = mNewPasswordEt.getText().toString();
        String surePass = mSurePasswordEt.getText().toString();

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
        if (!newPass.equals(surePass)) {
            ToastUtil.error("两次密码输入不一致");
            mSurePasswordEt.requestFocus();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", mLoginUser.phone);
        params.put("newPass", StringUtil.md5(newPass));
        params.put("checkNumber", mCaptchaEt.getText().toString());
        ToastUtil.showLoading(this);
        APIManager.startRequest(mUserService.putPassword(params), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("操作成功");
                finish();
            }
        });

    }


}
