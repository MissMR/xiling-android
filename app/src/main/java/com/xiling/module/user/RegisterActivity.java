package com.xiling.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Strings;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.ValidateUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    private String mInvitationCode;
    private IUserService mUserService;
    private ICaptchaService mCaptchaService;
    private User userInfo;

    @BindView(R.id.avatarIv)
    protected SimpleDraweeView mAvatarIv;
    @BindView(R.id.nicknameTv)
    protected TextView mNicknameTv;
    @BindView(R.id.phoneTv)
    protected TextView mPhoneTv;
    @BindView(R.id.agreeIv)
    protected ImageView mAgreeIv;
    @BindView(R.id.captchaBtn)
    protected CaptchaBtn mCaptchaBtn;
    @BindView(R.id.phoneEt)
    protected EditText mPhoneEt;
    @BindView(R.id.captchaEt)
    protected EditText mCaptchaEt;
    @BindView(R.id.passwordEt)
    protected EditText mPasswordEt;
    @BindView(R.id.confirmedEt)
    protected EditText mConfirmedEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        mAgreeIv.setSelected(true);

        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        getIntentData();
        
        setTitle("注册");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!Strings.isNullOrEmpty(mInvitationCode)) {
            getUserInfoByCode(mInvitationCode);
        }
        mAgreeIv.setSelected(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getIntentData() {
        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            setUserViews(user);
        }
    }

    @OnClick(R.id.agreeIv)
    protected void changeAgreeStatus() {
        mAgreeIv.setSelected(!mAgreeIv.isSelected());
    }


    private void getUserInfoByCode(String invitationCode) {
//        APIManager.startRequest(mUserService.getUserInfoByCode(invitationCode), new BaseRequestListener<User>(this) {
//            @Override
//            public void onSuccess(User user) {
//                if (!user.isShowStoreView()) {
//                    ToastUtils.showShortToast("推荐人非店主");
//                }
//                userInfo = user;
//                setUserViews(user);
//            }
//        });
    }

    private void setUserViews(User user) {
        mInvitationCode = user.invitationCode;
        mNicknameTv.setText("昵称：" + user.nickname);
        FrescoUtil.setImage(mAvatarIv, user.avatar);
        mPhoneTv.setText("手机号：" + ConvertUtil.maskPhone(user.phone));
    }


    @OnClick(R.id.userInfoLayout)
    protected void showChangeUserInfoDialog() {
        new ChangeUserDialog(this).show();
    }

    @OnClick(R.id.registerBtn)
    protected void onRegister() {
        if (!mAgreeIv.isSelected()) {
            ToastUtil.error("请先阅读并同意服务协议");
            return;
        }

        if (Strings.isNullOrEmpty(mInvitationCode)) {
            ToastUtil.error("请选择邀请人");
            return;
        }

        String phone = mPhoneEt.getText().toString();
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            mPhoneEt.requestFocus();
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            mPhoneEt.requestFocus();
            return;
        }
        String captcha = mCaptchaEt.getText().toString();
        if (Strings.isNullOrEmpty(captcha)) {
            ToastUtil.error("请输入验证码");
            mCaptchaEt.requestFocus();
            return;
        }
        String password = mPasswordEt.getText().toString();
        String confirmed = mConfirmedEt.getText().toString();
        if (Strings.isNullOrEmpty(password)) {
            ToastUtil.error("请输入密码");
            mPasswordEt.requestFocus();
            return;
        }
        if (Strings.isNullOrEmpty(confirmed)) {
            ToastUtil.error("请再次输入密码");
            mConfirmedEt.requestFocus();
            return;
        }
        if (!confirmed.equals(password)) {
            ToastUtil.error("两次输入的密码不一致");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", StringUtil.md5(password));
        params.put("checkNumber", captcha);
        params.put("nickName", "新用户");
        params.put("invitationCode", mInvitationCode);
        params.put("headImage", "");
        params.put("wechatOpenId", "");
        params.put("wechatUnionId", "");
//        params.put("status", "9");
        ToastUtil.showLoading(this);
        APIManager.startRequest(mUserService.register(params), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("注册成功");
                finish();
            }

//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                mCaptchaBtn.stop();
//            }
        });
    }

    @OnClick(R.id.captchaBtn)
    protected void getCaptcha() {
        String phone = mPhoneEt.getText().toString();
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            mPhoneEt.requestFocus();
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            mPhoneEt.requestFocus();
            return;
        }
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(mCaptchaService.getCaptchaForRegister(token, phone), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                mCaptchaBtn.start();
            }
        });
    }

    @OnClick(R.id.agreementNameTv)
    public void onViewClicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", "file:///android_asset/用户协议.html");
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userInfoFromDialog(EventMessage message) {
        if (message.getEvent().equals(Event.inviterUpdate)) {
            setUserViews((User) message.getData());
        }
    }
}
