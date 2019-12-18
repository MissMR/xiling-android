package com.xiling.ddmall.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.UIEvent;
import com.xiling.ddmall.ddui.bean.UserAuthBean;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 安全问题
 */
public class SecurityQuestionActivity extends BaseActivity {

    /**
     * 跳转安全问题状态
     */

    public static void jumpQuestion(Context context) {
        jump(context, vModeQuestion, null);
    }

    public static void jumpPassword(Context context) {
        jump(context, vModePassword, null);
    }

    public static void jumpQuestion(Context context, UserAuthBean userAuth) {
        jump(context, vModeQuestion, userAuth);
    }

    /**
     * 跳转交易密码状态
     */
    public static void jumpPassword(Context context, UserAuthBean userAuth) {
        jump(context, vModePassword, userAuth);
    }

    /**
     * 跳转到指定类型
     */
    public static void jump(Context context, int mode, UserAuthBean userAuth) {
        Intent intent = new Intent(context, SecurityQuestionActivity.class);
        intent.putExtra(kMode, mode);
        intent.putExtra(kData, userAuth);
        context.startActivity(intent);
    }

    public static final String kData = "SecurityQuestionActivity.Data";
    public static final String kMode = "SecurityQuestionActivity.Mode";

    public static final int vModeQuestion = 0x00;
    public static final int vModePassword = 0x01;

    @BindView(R.id.iv_info)
    ImageView ivInfo;

    @BindView(R.id.tv_tip_security)
    TextView tvTipSecurity;

    @BindView(R.id.tv_hint)
    TextView tvHint;

    @BindView(R.id.tv_btn_add_question)
    TextView tvBtnAddQuestion;

    @BindView(R.id.tv_btn_update_question)
    TextView tvBtnUpdateQuestion;
    @BindView(R.id.fl_root)
    View mFlRoot;
    private boolean isAddedQuestion = false;

    UserAuthBean authBean = null;
    int mode = vModeQuestion;
    private IUserService mIUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra(kMode, vModeQuestion);
        setContentView(R.layout.activity_security_question);
        ButterKnife.bind(this);
        showHeader("");
        mFlRoot.setVisibility(View.GONE);
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        ToastUtil.showLoading(this);
        getUserAuth();
    }

    private void getUserAuth() {
        APIManager.startRequest(mIUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>(this) {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                mFlRoot.setVisibility(View.VISIBLE);
                authBean = result;
                initView();
            }
        });
    }

    private void initView() {

        if (mode == vModePassword) {
            showHeader("交易密码", true);

            //0 未设置   1 为已经设置
            isAddedQuestion = authBean.isSetTradePassword();
            if (isAddedQuestion) {
                tvTipSecurity.setVisibility(View.VISIBLE);
                showHeader("重置交易密码");
                tvBtnUpdateQuestion.setText("重置交易密码");
                tvBtnUpdateQuestion.setVisibility(View.VISIBLE);

                tvBtnAddQuestion.setVisibility(View.GONE);

                tvHint.setText("店多多为您提现保驾护航");
                tvTipSecurity.setText("您已经设置交易密码了哦~");

                ivInfo.setImageResource(R.mipmap.security_bg_red);
            } else {
                tvTipSecurity.setVisibility(View.GONE);
                tvBtnUpdateQuestion.setVisibility(View.GONE);

                tvBtnAddQuestion.setText("设置交易密码");
                tvBtnAddQuestion.setVisibility(View.VISIBLE);

                tvHint.setText("您还没有设置交易密码哦~");

                ivInfo.setImageResource(R.mipmap.security_bg_gray);
            }
        } else {
            showHeader("安全问题", true);

            //0 未设置   1 为已经设置
            isAddedQuestion = authBean.getSafeKeyFlag() == 1;
            if (isAddedQuestion) {
                showHeader("重置安全问题");
                tvTipSecurity.setVisibility(View.VISIBLE);
                tvBtnUpdateQuestion.setVisibility(View.VISIBLE);
                tvBtnUpdateQuestion.setText("重置安全问题");
                tvBtnAddQuestion.setVisibility(View.GONE);
                tvHint.setText("店多多为您提现保驾护航");
                tvTipSecurity.setText("您已设置安全问题了哦~ ");
                ivInfo.setImageResource(R.mipmap.security_bg_red);
            } else {
                tvTipSecurity.setVisibility(View.GONE);
                tvBtnUpdateQuestion.setVisibility(View.GONE);
                tvBtnAddQuestion.setVisibility(View.VISIBLE);
                tvHint.setText("您还没有添加安全问题哦~");
                ivInfo.setImageResource(R.mipmap.security_bg_gray);
            }
        }

    }

    @OnClick({R.id.tv_btn_add_question, R.id.tv_btn_update_question})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, AccessCaptchaActivity.class);
        int route = mode == vModeQuestion ? AccessCaptchaActivity.ROUTE_SECURITY_QUESTION : AccessCaptchaActivity.ROUTE_TRADE_PASSWORD;
        intent.putExtra(Constants.Extras.ROUTE, route);
        startActivity(intent);
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
        if (event.getType() == UIEvent.Type.CloseQuestionActivity && mode == vModeQuestion) {
            finish();
        } else if (event.getType() == UIEvent.Type.ClosePasswordActivity && mode == vModePassword) {
            finish();
        }
    }
}
