package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.RegexUtils;
import com.xiling.R;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.dialog.DDMDialog;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/17
 * 身份认证
 * 提交实名  身份证号 信息
 */
public class UploadAuthInfoActivity extends BaseActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_id)
    EditText etUserId;
    @BindView(R.id.tv_btn_confirm)
    TextView tvBtnConfirm;
    private String[] mURLs;

    private IUserService mIUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_id_card);
        ButterKnife.bind(this);
        initData();
        initView();
    }


    private void initData() {
        mURLs = getIntent().getStringArrayExtra(Constants.Extras.ID_URLS);
        if (TextUtils.isEmpty(mURLs[0]) || TextUtils.isEmpty(mURLs[1])) {
            ToastUtil.error("身份证照片缺失");
            return;
        }
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    @OnClick(R.id.tv_btn_confirm)
    public void onViewClicked() {
        String id = etUserId.getText().toString();
        boolean enableId = false;
        if (id.length() == 15) {
            enableId = RegexUtils.isIDCard15(id);
        } else {
            enableId = RegexUtils.isIDCard18(id);
        }
        if (id.length() < 15 || (id.length() > 15 && id.length() < 18)) {
            ToastUtil.error("请核实身份证号位数");
            return;
        }
        if (!enableId) {
            ToastUtil.error("请录入真实的身份证号");
            return;
        }


        String content = "" + etUserName.getText().toString() + "\n" + id;

        new DDMDialog(this)
                .setTitle("请确认您的填写信息")
                .setContent(content)
                .setContextGravity(Gravity.CENTER)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateIdentifyInfo();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DLog.i("取消");

                    }
                })
                .enableClose(false)
                .show();
    }

    private void updateIdentifyInfo() {
        APIManager.startRequest(mIUserService.updateAuthInfo(mURLs[0], mURLs[1], etUserName.getText().toString(), etUserId.getText().toString()),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        ToastUtil.success("实名认证申请成功，请等待审核~");
                        goBack();
                    }
                });
    }

    private void goBack() {
        startActivity(new Intent(this, AuthActivity.class));
    }

    private void initView() {
        tvBtnConfirm.setEnabled(false);
        showHeader("实名认证", true);
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBtnConfirm.setEnabled(s.length() > 0 && etUserId.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBtnConfirm.setEnabled(s.length() > 0 && etUserName.getText().length() > 0);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
