package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.tools.TextTools;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 修改手机号 -> 忘记手机号 -> 身份验证页面
 */
public class CheckIdentityActivity extends BaseActivity {

    @BindView(R.id.et_identity)
    EditText etIdentity;

    @BindView(R.id.tv_btn_next)
    TextView mTvBtnNext;

    private IUserService mIUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_identity);
        ButterKnife.bind(this);
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        showHeader("身份验证", true);
        mTvBtnNext.setEnabled(false);
        etIdentity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTvBtnNext.setEnabled(charSequence.length() >= 15);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @OnClick(R.id.tv_btn_next)
    public void onViewClicked() {

        String pId = etIdentity.getText().toString();

        if (TextUtils.isEmpty(pId)) {
            ToastUtil.error("请先输入身份证号");
            return;
        }

        if (pId.length() < 15 || (pId.length() > 15 && pId.length() < 18)) {
            ToastUtil.error("请核实身份证号位数");
            return;
        }

        //满足身份证长度的情况下
        if (pId.length() == 15 || pId.length() == 18) {
            //删除末尾
            pId = pId.substring(0, pId.length() - 1);
            //包含字符和特殊符号则提示
            if (TextTools.isHasABC(pId) || TextTools.isHasSpecialChar(pId)) {
                ToastUtil.error("请录入真实的身份证号");
                return;
            }
        } else {
            ToastUtil.error("请录入真实的身份证号");
            return;
        }

        String id = etIdentity.getText().toString();
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + id);
        APIManager.startRequest(mIUserService.checkUserAuth(id, token), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                startActivity(new Intent(CheckIdentityActivity.this, UpdatePhoneNumberActivity.class));
            }
        });
    }
}
