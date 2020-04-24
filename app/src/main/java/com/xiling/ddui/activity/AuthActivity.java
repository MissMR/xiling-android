package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/10
 */
public class AuthActivity extends BaseActivity {

    private static final int AUTH_UNDO = 0;
    private static final int AUTH_WAITTING = 1;
    private static final int AUTH_DONE = 2;
    private static final int AUTH_FAIL = 3;

    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_btn_auth)
    TextView tvBtnAuth;
    @BindView(R.id.ll_none_auth_container)
    LinearLayout llNoneAuthContainer;
    @BindView(R.id.tv_auth_name)
    TextView tvAuthName;
    @BindView(R.id.tv_auth_id)
    TextView tvAuthId;
    @BindView(R.id.ll_user_auth_container)
    LinearLayout llUserAuthContainer;


    private IUserService mIUserService;

    private String[] mURLs = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        ToastUtil.showLoading(this);
        getUserAuth();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getUserAuth();
    }

    private void initView() {
        showHeader("账户认证", true);
    }

    @OnClick(R.id.tv_btn_auth)
    public void onViewClicked() {
        startActivity(new Intent(this, UploadIdCardActivity.class)
                .putExtra(Constants.Extras.ID_URLS, mURLs));
    }


    private void getUserAuth() {
        APIManager.startRequest(mIUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>(this) {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                showAuthInfo(result);
                int flag = 0;
                if (!TextUtils.isEmpty(result.getIdcardFrontImg())) {
                    mURLs[0] = result.getIdcardFrontImg();
                    flag++;
                }
                if (!TextUtils.isEmpty(result.getIdcardBackImg())) {
                    mURLs[1] = result.getIdcardBackImg();
                    flag++;
                }

                //未提交状态且有身份证数据直接进入下一层
                if (result.getAuthStatus() == AUTH_UNDO) {
                    if (flag > 0) {
                        onViewClicked();
//                        finish();
                    }
                }
            }
        });
    }

    private void showAuthInfo(UserAuthBean authBean) {
        boolean isAuthDone = authBean.getAuthStatus() == AUTH_DONE;
        llUserAuthContainer.setVisibility(isAuthDone ? View.VISIBLE : View.GONE);
        llNoneAuthContainer.setVisibility(isAuthDone ? View.GONE : View.VISIBLE);
        if (isAuthDone) {
            tvAuthName.setText(authBean.getSecretUserName());
            tvAuthId.setText(authBean.getSecretIdentityCardNumber());
        } else {
            showNoneAuthInfo(authBean.getAuthStatus());
        }

        tvTip.setText("" + authBean.getCheckRemark());
    }

    private void showNoneAuthInfo(int state) {
        String hint;
        int imgRes;
        switch (state) {
            case AUTH_UNDO:
                tvBtnAuth.setVisibility(View.VISIBLE);
                tvBtnAuth.setText("添加账户认证");
                tvTip.setVisibility(View.GONE);
                hint = "您还没有添加账户认证信息哦~";
                imgRes = R.mipmap.auth_undo;
                tvTip.setVisibility(View.GONE);
                break;
            case AUTH_WAITTING:
                tvBtnAuth.setVisibility(View.GONE);
                tvTip.setVisibility(View.GONE);
                hint = "周一至周五9:00-17:00（工作时间）提交将在2小时内审核完毕，非工作时间将顺延至工作时间处理，小主请耐心等待吆~";
                imgRes = R.mipmap.authing;
                tvTip.setVisibility(View.GONE);
                break;
            default:
                tvBtnAuth.setVisibility(View.VISIBLE);
                tvBtnAuth.setText("重新提交账户认证");
                tvTip.setVisibility(View.VISIBLE);
                hint = "您的账户认证信息审核失败";
                tvTip.setVisibility(View.VISIBLE);
                imgRes = R.mipmap.auth_fail;
        }
        tvHint.setText(hint);
        ivInfo.setImageResource(imgRes);
    }
}
