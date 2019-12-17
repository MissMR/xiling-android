package com.dodomall.ddmall.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.AppUtils;
import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.config.AppConfig;
import com.dodomall.ddmall.ddui.tools.TextTools;
import com.dodomall.ddmall.ddui.tools.ViewUtil;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.LoginSwitchBean;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.bean.WeChatLoginModel;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.UserService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.dodomall.ddmall.shared.util.WechatUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/25
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @BindView(R.id.ll_btn_login_phone)
    LinearLayout mLlBtnLoginPhone;

    @BindView(R.id.ll_btn_login_wechat)
    LinearLayout mLlBtnLoginWechat;

    private IUserService mUserService;

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
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
//        getLoginSwitch();
    }

    private void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        hideHeader();
        mLlBtnLoginPhone.setVisibility(View.VISIBLE);
//        mTvVersion.setText("v " + AppUtils.getAppVersionName(this));
    }

    private void getLoginSwitch() {
        APIManager.startRequest(mUserService.getLoginFlag(BuildConfig.VERSION_CODE, TextTools.getChannelName(this),
                TextTools.getChannelId(this)), new BaseRequestListener<LoginSwitchBean>(this) {
            @Override
            public void onSuccess(LoginSwitchBean result) {
                super.onSuccess(result);
                if (null == result) {
                    return;
                }
                if (StringUtil.md5(String.valueOf(result.getTimestamp() + result.getFlag())).equals(result.getToken())) {
                    if (AppConfig.DEBUG) {
                        mLlBtnLoginPhone.setVisibility(View.VISIBLE);
                    } else {
                        mLlBtnLoginPhone.setVisibility(result.isOpen() ? View.VISIBLE : View.INVISIBLE);
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatAuth(EventMessage message) {
        if (!canHandleWechatCallback) {
            return;
        }
        if (message.getEvent().equals(Event.wxLoginSuccess)) {
            getAccessToken((String) message.getData());
        } else if (message.getEvent().equals(Event.wxLoginCancel)) {
            ToastUtil.hideLoading();
            ToastUtil.error("登录取消");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess)) {
            finish();
        }
    }

    private void loginByPassword(String phone, String password) {
        password = StringUtil.md5(password);
        APIManager.startRequest(mUserService.login(phone, password), new BaseRequestListener<User>(this) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                UserService.loginSuccess(LoginActivity.this, user);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    private void getAccessToken(String code) {
        APIManager.startRequest(mUserService.getAccessToken(code), new BaseRequestListener<WeChatLoginModel>() {
            @Override
            public void onSuccess(WeChatLoginModel result) {
                super.onSuccess(result);
                ToastUtil.hideLoading();
                if (result.registerStatus == 1) {
                    // 注册过且已经完善信息了的直接登录
                    loginByPassword(result.unionid, result.unionid);
                } else {
                    // 走绑定手机号流程
                    goCheckPhone(result);
                    finish();
                }
                // TODO:Jigsaw 2019/3/20 等小程序准备就绪 用户已注册没有绑定手机号业务处理
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
            }
        });
    }

    private void goCheckPhone(WeChatLoginModel wechat) {
        Intent intent = new Intent(this, CheckPhoneActivity.class);
        intent.putExtra(Constants.Extras.WECHAT_USER, wechat);
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
                goCheckPhone(null);
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
