package com.xiling.ddui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.UIEvent;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.tools.UserAuthHelper;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.User;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/10
 * 账户与安全
 */
@Deprecated
public class AuthSecurityActivity extends BaseActivity {

    public static final int REQUEST_CODE_AUTH = 1;
    @BindView(R.id.tv_bind_phone_state)
    TextView tvBindPhoneState;
    @BindView(R.id.tv_auth_state)
    TextView tvAuthState;
    @BindView(R.id.tv_bank_card_state)
    TextView tvBankCardState;
    @BindView(R.id.tv_trade_password_state)
    TextView tvTradePasswordState;
    @BindView(R.id.tv_security_state)
    TextView tvSecurityState;

    private User mUser;
    private IUserService mIUserService;
    private UserAuthBean mUserAuthBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_security);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAuth();
    }

    private void initData() {
        mUser = (User) getIntent().getSerializableExtra(Constants.Extras.USER);
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private void getUserAuth() {
        APIManager.startRequest(mIUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>() {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                mUserAuthBean = result;
                setBaseStateView(result);
            }
        });
    }

    private void setBaseStateView(UserAuthBean result) {
        UserAuthHelper helper = new UserAuthHelper(result);
        helper.setAuthState(tvAuthState);
        helper.setBankState(tvBankCardState);
        helper.setTradePasswordState(tvTradePasswordState);
        helper.setQuestionState(tvSecurityState);
    }

    private void setRedText(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.ddm_red));
    }

    private void initView() {
        showHeader("账户与安全", true);
        tvBindPhoneState.setText(mUser.getSecretPhoneNumber());
    }

    public static void goBack(Context context) {
        context.startActivity(new Intent(context, AuthSecurityActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 刷新
        mUser = SessionUtil.getInstance().getLoginUser();
        tvBindPhoneState.setText(mUser.getSecretPhoneNumber());
        getUserAuth();
    }

    @OnClick({R.id.rl_bind_phone, R.id.rl_auth, R.id.tv_bank_card, R.id.rl_trade_password, R.id.rl_security})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.rl_security) {
            //进入安全问题之前检查是否实名认证
            checkAuth(new CheckAuthListener() {
                @Override
                public void onPass(UserAuthBean result) {
                    SecurityQuestionActivity.jumpQuestion(context, result);
                }
            });
        } else if (view.getId() == R.id.rl_trade_password) {
            //进入交易密码之前检查是否实名认证
            checkAuth(new CheckAuthListener() {
                @Override
                public void onPass(UserAuthBean result) {
                    SecurityQuestionActivity.jumpPassword(context, result);
                }
            });
        } else if (view.getId() == R.id.tv_bank_card) {
            //进入绑定银行卡之前检查是否实名认证
            checkAuth(new CheckAuthListener() {
                @Override
                public void onPass(UserAuthBean result) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(context, BankCardActivity.class));
                    intent.putExtra(Constants.Extras.HAS_SET_QUESTION, mUserAuthBean.isSetSecurityQuestion());
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.rl_bind_phone:
                    intent.setComponent(new ComponentName(this, BindPhoneNumberActivity.class));
                    break;
                case R.id.rl_auth:
                    intent.setComponent(new ComponentName(this, AuthActivity.class));
                    break;
            }
            startActivity(intent);
        }

    }

    public interface CheckAuthListener {
        void onPass(UserAuthBean result);
    }

    /**
     * 检查是否已通过实名认证
     */
    public void checkAuth(final CheckAuthListener listener) {
        APIManager.startRequest(mIUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>() {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                // 0:未认证;1:认证申请;2:认证通过;3:驳回重申;4:认证拒绝
                int status = result.getAuthStatus();
                if (status == 2) {
                    listener.onPass(result);
                } else {
                    D3ialogTools.showAuthDialog(context, status);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UIEvent event) {
        UIEvent.Type type = event.getType();
        //需要重新刷新数据
        if (type == UIEvent.Type.CloseQuestionActivity ||
                type == UIEvent.Type.ClosePasswordActivity ||
                type == UIEvent.Type.CloseBindCardActivity) {
            getUserAuth();
        }
    }

}
