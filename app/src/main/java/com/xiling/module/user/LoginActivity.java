package com.xiling.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.config.AppConfig;
import com.xiling.ddui.tools.TextTools;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseBean;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.LoginSwitchBean;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.UserService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.WechatUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.ll_btn_login_phone)
    LinearLayout mLlBtnLoginPhone;

    @BindView(R.id.ll_btn_login_wechat)
    LinearLayout mLlBtnLoginWechat;

    private INewUserService mNewUserService;

    // 是否有手机号登录
    private boolean isShowPhoneLogin = false;
    // 是否处理wechat回调  手机号登录 不处理回调
    private boolean canHandleWechatCallback = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guide);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        canHandleWechatCallback = true;
    }

    private void initData() {
        mNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
    }

    private void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        hideHeader();
        mLlBtnLoginPhone.setVisibility(View.VISIBLE);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatAuth(EventMessage message) {
        if (!canHandleWechatCallback) {
            return;
        }
        if (message.getEvent().equals(Event.wxLoginSuccess)) {
            wxLogin((String) message.getData());
        } else if (message.getEvent().equals(Event.wxLoginCancel)) {
            ToastUtil.hideLoading();
            ToastUtil.error("登录取消");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.LOGIN_SUCCESS)) {
            finish();
        }
    }


    /**
     * 微信登录
     *
     * @param code
     */
    private void wxLogin(String code) {
        APIManager.startRequest(mNewUserService.wxLogin(code,"ANDROID"), new BaseRequestListener<NewUserBean>() {
            @Override
            public void onSuccess(NewUserBean result) {
                super.onSuccess(result);
                ToastUtil.hideLoading();
               //登录成功
                UserManager.getInstance().loginSuccess(context,result);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                checkMessage(e.getMessage());
                finish();
            }
        });
    }

    /**
     * 处理异常状态
     *
     * @param msg
     */
    private void checkMessage(String msg) {
        switch (msg) {
            case "uncheck-login":
                //跳转到登录注册页面
                finish();
                break;
            case "unbind-phone":
                //跳转到绑定手机号页面
                goCheckPhone("0");
                break;
            case "unbind-wechat":
                //跳转到绑定微信页面
                if (WechatUtil.isWeChatAppInstalled(this)) {
                    sendWechatAuth();
                } else {
                    ToastUtil.error("请先安装微信客户端");
                }
                break;
            case "unbind-invite-code":
                //跳转到绑定邀请码页面
                Intent intent = new Intent(this, InviteCodeActivity.class);
                intent.putExtra(Constants.Extras.WECHAT_USER, getIntent().getSerializableExtra(Constants.Extras.WECHAT_USER));
                intent.putExtra(Constants.Extras.PHONE_NUMBER, "");
                intent.putExtra(Constants.Extras.REGISTER_CAPTCHA, "");
                startActivity(intent);
                break;
            default:
                ToastUtil.error(msg);
                break;
        }
    }

    /**
     * 登录方式
     * ”0“ 微信登录 ”1“ 手机号登录
     *
     * @param type
     */
    private void goCheckPhone(String type) {
        Intent intent = new Intent(this, CheckPhoneActivity.class);
        intent.putExtra(Constants.Extras.LOGINTYPE, type);
        startActivity(intent);
    }

    @OnClick({R.id.ll_btn_login_wechat, R.id.ll_btn_login_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_btn_login_wechat:
                // 微信登录
                ViewUtil.setViewClickedDelay(mLlBtnLoginWechat);
                if (WechatUtil.isWeChatAppInstalled(this)) {
                    sendWechatAuth();
                } else {
                    ToastUtil.error("请先安装微信客户端");
                }
                break;
            case R.id.ll_btn_login_phone:
                // 手机号登录
                goCheckPhone("1");
                canHandleWechatCallback = false;
                break;
        }
    }

    private void sendWechatAuth() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "" + System.currentTimeMillis();
        IWXAPI api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        api.sendReq(req);
    }

}
