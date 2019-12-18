package com.xiling.ddmall.module.user;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.utils.ConvertUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Strings;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.MyApplication;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.NearStore.NearStoreListActivity;
import com.xiling.ddmall.module.NearStore.model.NearStoreModel;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.basic.BaseSubscriber;
import com.xiling.ddmall.shared.bean.CheckNumber;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.UserService;
import com.xiling.ddmall.shared.service.contract.ICaptchaService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.CountDownRxUtils;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rx.functions.Action1;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author zjm
 * @date 2017/12/7
 */
public class NewRegisterActivity extends BaseActivity {

    @BindView(R.id.ivLogo)
    ImageView mIvLogo;
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.tvGetMsg)
    TextView mTvGetMsg;
    @BindView(R.id.tvAgreement)
    TextView mTvAgreement;
    @BindView(R.id.tvRegister)
    TextView mTvRegister;
    @BindView(R.id.tvLogin)
    TextView mTvLogin;
    @BindView(R.id.ivStoreAvatar)
    SimpleDraweeView mIvStoreAvatar;
    @BindView(R.id.tvStoreName)
    TextView mTvStoreName;
    @BindView(R.id.tvNearStore)
    TextView mTvNearStore;
    @BindView(R.id.tvInvitationCode)
    TextView mTvInvitationCode;
    @BindView(R.id.layoutLocalizeSucceed)
    LinearLayout mLayoutLocalizeSucceed;
    @BindView(R.id.tvInvitationCode2)
    TextView mTvInvitationCode2;
    @BindView(R.id.layoutLocalizeFail)
    LinearLayout mLayoutLocalizeFail;
    @BindView(R.id.etMsgCode)
    EditText mEtMsgCode;
    @BindView(R.id.tvStoreTips)
    TextView mTvStoreTips;

    private User mUser;
    private QMUIPopup mNormalPopup;

    private final String TEXT_STORE_TIPE = "新注册的用户若没有邀请人，系统将自动分配一位附近的店主（也可手动输入邀请码），在购买商品的过程店主可为您提供全方位的服务。";
    private IUserService mUserService;
    private ICaptchaService mCaptchaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.overridePendingTransition(R.anim.activity_top_bottom_open, 0);
        setContentView(R.layout.activity_new_register);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        setStoreData();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.isShowNoLogin = false;
        EventBus.getDefault().unregister(this);
    }

//    @Override
//    public void finish() {
//        super.finish();
//        this.overridePendingTransition(0, R.anim.activity_top_bottom_close);
//    }

    private void getIntentData() {
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    private void initView() {
//        QMUIStatusBarHelper.translucent(this);
//        setBarPadingHeight(QMUIStatusBarHelper.getStatusbarHeight(this));
//        setBarPadingColor(Color.WHITE);

        setTitle("注册");
        setTitleNoLine();
        setLeftBlack();
    }


    private void setStoreData() {
        if (mUser == null) {
            return;
        }
        FrescoUtil.setImageSmall(mIvStoreAvatar, mUser.avatar);
        mTvStoreName.setText(mUser.nickname);
        setLocationLayout(true);
    }

    private void initData() {
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);

        Disposable subscribe = Observable.timer(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        QMUIStatusBarHelper.setStatusBarLightMode(NewRegisterActivity.this);
                        initLocation();
                    }
                });
        addSubscribe(subscribe);
    }

    private void initLocation() {
        if (mUser != null) {
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    startLocation();
                } else {
                    ToastUtil.error("获取定位权限失败");
//                    setLocationLayout(false);
                    getDefaultUser();
                }
            }
        });

    }

    private void startLocation() {
        AMapLocationClient locationClient = new AMapLocationClient(getApplicationContext());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                getUserinfo(aMapLocation);
            }
        });
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        option.setNeedAddress(true);
        option.setMockEnable(true);
        option.setLocationCacheEnable(false);
        locationClient.setLocationOption(option);
        locationClient.startLocation();
    }

    private void setLocationLayout(boolean isSucceed) {
        if (isSucceed) {
            mLayoutLocalizeSucceed.setVisibility(View.VISIBLE);
            mLayoutLocalizeFail.setVisibility(View.GONE);
        } else {
            mLayoutLocalizeSucceed.setVisibility(View.GONE);
            mLayoutLocalizeFail.setVisibility(View.VISIBLE);
        }
    }

    private void getUserinfo(AMapLocation location) {
        if (location.getErrorCode() != 0) {
            getDefaultUser();
        } else {
            APIManager.startRequest(
                    mUserService.getNearStoreModelList(
                            location.getLongitude(),
                            location.getLatitude(),
                            location.getProvince(),
                            location.getCity(),
                            1,
                            1
                    ),
                    new BaseRequestListener<NearStoreModel>() {
                        @Override
                        public void onSuccess(NearStoreModel model) {
                            if (model.datas.size() == 0) {
                                getDefaultUser();
                                return;
                            }
                            User user = new User();
                            NearStoreModel.DatasEntity datasEntity = model.datas.get(0);
                            user.nickname = datasEntity.nickName;
                            user.avatar = datasEntity.headImage;
                            user.invitationCode = datasEntity.inviteCode;
                            mUser = user;
                            setStoreData();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            getDefaultUser();
                        }
                    });
        }
    }

    private void getDefaultUser() {
        APIManager.startRequest(
                mUserService.getDefaultUpMember(),
                new BaseRequestListener<User>() {
                    @Override
                    public void onSuccess(User model) {
                        mUser = model;
                        setStoreData();
                    }
                });
    }

    private void getUserInfo(final QMUIDialog dialog, String code) {
//        APIManager.startRequest(mUserService.getUserInfoByCode(code), new BaseRequestListener<User>(this) {
//            @Override
//            public void onSuccess(User result) {
//                dialog.dismiss();
//                mUser = result;
//                setStoreData();
//            }
//        });
    }


//    private void getUserInfo(final QMUIDialog dialog, String phone) {
//        APIManager.startRequest(mUserService.getUserInfoByPhone(phone), new BaseRequestListener<User>(this) {
//            @Override
//            public void onSuccess(User result) {
//                dialog.dismiss();
//                mUser = result;
//                setStoreData();
//            }
//        });
//    }

    private void initNormalPopupIfNeed() {
        if (mNormalPopup == null) {
            mNormalPopup = new QMUIPopup(this, QMUIPopup.DIRECTION_NONE);
            TextView textView = new TextView(this);
            textView.setLayoutParams(mNormalPopup.generateLayoutParam(
                    ConvertUtils.dp2px(250f),
                    WRAP_CONTENT
            ));
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(this, 4), 1.0f);
            int padding = QMUIDisplayHelper.dp2px(this, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(TEXT_STORE_TIPE);
            textView.setTextColor(ContextCompat.getColor(this, R.color.text_black));
            mNormalPopup.setContentView(textView);
        }
    }

    @OnClick(R.id.tvGetMsg)
    public void onMTvGetMsgClicked() {
        if (UiUtils.checkETPhone(mEtPhone)) {
            String phone = mEtPhone.getText().toString();
            String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
            APIManager.startRequest(mCaptchaService.getCaptchaForRegister(token, phone), new BaseRequestListener<Object>(this) {

                @Override
                public void onSuccess(Object result) {
                    CountDownRxUtils.textViewCountDown(mTvGetMsg, 60, "获取验证码");
                }
            });
        }
    }

    @OnClick(R.id.tvAgreement)
    public void onMTvAgreementClicked() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", "file:///android_asset/用户协议.html");
        startActivity(intent);
    }

    @OnClick(R.id.tvRegister)
    public void onMTvRegisterClicked() {
        if (mUser == null) {
            ToastUtil.error("请选择邀请人");
            onMTvInvitationCodeClicked();
            return;
        }
        if (!UiUtils.checkETPhone(mEtPhone)) {
            return;
        }
        String captcha = mEtMsgCode.getText().toString();
        if (Strings.isNullOrEmpty(captcha)) {
            ToastUtil.error("请输入验证码");
            mEtMsgCode.requestFocus();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", mEtPhone.getText().toString());
        params.put("checkNumber", captcha);
        params.put("nickName", "新用户");
        params.put("invitationCode", mUser.invitationCode);
        params.put("headImage", "");
        params.put("wechatOpenId", "");
        params.put("wechatUnionId", "");
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
                        return mUserService.login(mEtPhone.getText().toString(), result.data.checkNumber);
                    }
                });
        BaseSubscriber baseSubscriber = new BaseSubscriber<User>() {
            @Override
            public void onNext(User user) {
                super.onNext(user);
                UserService.login(user);
                EventBus.getDefault().post(new EventMessage(Event.loginSuccess));
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

    @OnClick(R.id.tvLogin)
    public void onMTvLoginClicked() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.tvNearStore)
    public void onMTvNearStoreClicked() {
        startActivity(new Intent(this, NearStoreListActivity.class));
    }

    @OnClick({R.id.tvInvitationCode, R.id.tvInvitationCode2})
    public void onMTvInvitationCodeClicked() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle("邀请码")
                .setPlaceholder("请输入邀请人的邀请码")
                .setInputType(InputType.TYPE_CLASS_PHONE)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        getUserInfo(dialog, text.toString());
                    }
                });
        EditText editText = builder.getEditText();
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        builder.show();
    }

    @OnClick(R.id.tvStoreTips)
    public void onStoreTipsClicked() {
        initNormalPopupIfNeed();
        mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
        mNormalPopup.show(mTvStoreTips);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess)) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(MsgStatus msgStatus) {
        if (msgStatus.getAction() == MsgStatus.ACTION_SELECT_STORE) {
            finish();
        }
    }
}
