package com.xiling.module.user;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Strings;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.basic.BaseSubscriber;
import com.xiling.shared.bean.CheckNumber;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.WeChatLoginModel;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.CaptchaBtn;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.UserService;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.CountDownRxUtils;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 微信登录。未注册过的用户将跳到此页面，注册信息
 * <p>
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author zjm
 * @date 2018/7/31
 */
public class WxRegisterActivity extends BaseActivity {

    @BindView(R.id.ivAvatar)
    SimpleDraweeView mIvAvatar;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.newPhoneEt)
    EditText mNewPhoneEt;
    @BindView(R.id.captchaEt)
    EditText mCaptchaEt;
    @BindView(R.id.captchaBtn)
    CaptchaBtn mCaptchaBtn;
    @BindView(R.id.etInvitationCode)
    EditText mEtInvitationCode;
    private WeChatLoginModel mModel;

    private IUserService mUserService;
    private ICaptchaService mCaptchaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_register);
        ButterKnife.bind(this);
        getIntentData();
        initData();
    }

    private void initData() {
        setTitle("补全信息");
        setLeftBlack();

        FrescoUtil.setImageSmall(mIvAvatar, mModel.headImage);
        mTvName.setText(mModel.nickName);

        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
    }

    private void getIntentData() {
        mModel = (WeChatLoginModel) getIntent().getSerializableExtra("data");
    }

    @OnClick(R.id.tvSubmit)
    public void onViewClicked() {
        String code = mEtInvitationCode.getText().toString();
        if (com.blankj.utilcode.utils.StringUtils.isEmpty(code)) {
            ToastUtil.error("请输入您的邀请码");
            mEtInvitationCode.requestFocus();
            return;
        }
        if (!UiUtils.checkETPhone(mNewPhoneEt)) {
            return;
        }
        String captcha = mCaptchaEt.getText().toString();
        if (Strings.isNullOrEmpty(captcha)) {
            ToastUtil.error("请输入验证码");
            mCaptchaEt.requestFocus();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", mNewPhoneEt.getText().toString());
        params.put("checkNumber", captcha);
        params.put("nickName", mModel.nickName);
        params.put("invitationCode", code);
        params.put("headImage", mModel.headImage);
        params.put("wechatOpenId", mModel.openid);
        params.put("wechatUnionId", mModel.unionid);
        Observable<RequestResult<User>> registerAndLoginObservable = mUserService.registerNoPassword(params)
                .flatMap(new Function<RequestResult<CheckNumber>, Observable<RequestResult<User>>>() {
                    @Override
                    public Observable<RequestResult<User>> apply(final RequestResult<CheckNumber> result) throws Exception {
                        if (result.code != 0) {
                            RequestResult<User> userRequestResult = new RequestResult<>();
                            userRequestResult.code = result.code;
                            userRequestResult.message = result.message;
                            userRequestResult.data = new User();
                            return Observable.just(userRequestResult);
                        }
                        return mUserService.login(mNewPhoneEt.getText().toString(), result.data.checkNumber);
                    }
                });
        BaseSubscriber baseSubscriber = new BaseSubscriber<User>() {
            @Override
            public void onNext(User user) {
                super.onNext(user);
                UserService.login(user);
                EventBus.getDefault().post(new EventMessage(Event.loginSuccess));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        };
        baseSubscriber.setProgressDialog(getProgressDialog());
        execute(registerAndLoginObservable, baseSubscriber);
    }

    @OnClick(R.id.captchaBtn)
    public void onMTvGetMsgClicked() {
        if (UiUtils.checkETPhone(mNewPhoneEt)) {
            String phone = mNewPhoneEt.getText().toString();
            String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
            APIManager.startRequest(mCaptchaService.getCaptchaForRegister(token, phone), new BaseRequestListener<Object>(this) {

                @Override
                public void onSuccess(Object result) {
                    CountDownRxUtils.textViewCountDown(mCaptchaBtn, 60, "获取验证码");
                }
            });
        }
    }
}
