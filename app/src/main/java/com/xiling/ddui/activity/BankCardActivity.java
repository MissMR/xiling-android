package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ActivityController;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 银行卡信息
 */
public class BankCardActivity extends BaseActivity {

    @BindView(R.id.ll_none_bank_card)
    LinearLayout llNoneBankCard;
    @BindView(R.id.sdv_bank)
    SimpleDraweeView ivBank;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_bank_card_number)
    TextView tvBankCardNumber;
    @BindView(R.id.tv_bank_card_address)
    TextView tvBankCardAddress;
    @BindView(R.id.tv_bank)
    TextView tvBank;

    @BindView(R.id.rl_user_bank_card)
    RelativeLayout rlUserBankCard;
    @BindView(R.id.tv_btn_security)
    TextView tvBtnSecurity;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private IUserService mIUserService;
    private UserAuthBean mUserAuthBean;
    private boolean isChangeBankCard = false;
    private boolean hasSetQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        hasSetQuestion = getIntent().getBooleanExtra(Constants.Extras.HAS_SET_QUESTION, false);
        getBankCardInfo();
    }

    private void getBankCardInfo() {
        ToastUtil.showLoading(this);
        APIManager.startRequest(mIUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>(this) {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                mUserAuthBean = result;
                if (result.getBankAccountFlag() == 1) {
                    llNoneBankCard.setVisibility(View.GONE);
                    rlUserBankCard.setVisibility(View.VISIBLE);
                    setBankCardInfo(result);
                } else {
                    llNoneBankCard.setVisibility(View.VISIBLE);
                    rlUserBankCard.setVisibility(View.GONE);
                }
            }
        });

        // 安全问题 都显示
        hasSetQuestion = false;
        if (hasSetQuestion) {
            tvBtnSecurity.setVisibility(View.GONE);
            tvTip.setVisibility(View.INVISIBLE);
        }
    }

    private void setBankCardInfo(UserAuthBean result) {
        ivBank.setImageURI(result.getBankLogo());
        tvBank.setText(result.getBankName());
        tvBankCardNumber.setText(result.getSecretBankCardNumber());
        tvBankCardAddress.setText(result.getBankcardAddress());

        tvUserName.setText(result.getSecretUserName());
    }

    private void initView() {
        showHeader("银行卡", true);
    }

    @OnClick({R.id.tv_btn_add_bank_card, R.id.tv_btn_change_bank_card, R.id.tv_btn_security})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_add_bank_card:
            case R.id.tv_btn_change_bank_card:
                if (null == mUserAuthBean) {
                    ToastUtil.error("正在等待数据返回");
                    return;
                }
                startActivityForResult(new Intent(this, AddBankCardActivity.class)
                        .putExtra(Constants.Extras.USER_AUTH, mUserAuthBean), 1);
                break;
            case R.id.tv_btn_security:
                if (null == mUserAuthBean) {
                    ToastUtil.error("正在等待数据返回");
                    return;
                }
                SecurityQuestionActivity.jump(this, SecurityQuestionActivity.vModeQuestion, mUserAuthBean);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getBankCardInfo();
            isChangeBankCard = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ActivityController.getInstance().isActivityAlive(UserSettingsActivity.class)) {
            UserSettingsActivity.goBack(this);
        }
    }
}
