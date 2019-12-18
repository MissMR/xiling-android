package com.xiling.ddmall.module.user;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.xiling.ddmall.MyApplication;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.qrcode.DDMScanActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseBean;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.WeChatLoginModel;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.component.dialog.DDMDialog;
import com.xiling.ddmall.shared.component.dialog.DDMHintDialog;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.UserService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @author Jigsaw
 * @date 2018/8/24
 * 邀请码界面  注册的最后流程
 */
public class InviteCodeActivity extends BaseActivity {

    public static final int REQUEST_INVITE_CODE = 1;

    @BindView(R.id.et_pin_captcha)
    PinEntryEditText mPeEditText;

    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    @BindView(R.id.tv_invitor_empty)
    TextView tvInvitorEmpty;

    @BindView(R.id.iv_avatar)
    SimpleDraweeView mSdvAvatar;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    @BindView(R.id.tv_identity)
    TextView tvIdentity;

    @BindView(R.id.rl_invitor)
    RelativeLayout rlInvitor;

    private User mInvitor;
    private IUserService mUserService;
    private DDMHintDialog mDDMHintDialog;

    private String mPhoneNumber;
    private String mCaptcha;
    private WeChatLoginModel mWeChatLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        ButterKnife.bind(this);
        mPhoneNumber = getIntent().getStringExtra(Constants.Extras.PHONE_NUMBER);
        mWeChatLoginModel = (WeChatLoginModel) getIntent().getSerializableExtra(Constants.Extras.WECHAT_USER);
        mCaptcha = getIntent().getStringExtra(Constants.Extras.REGISTER_CAPTCHA);
        initView();
        initService();

        String inviteCode = MyApplication.getInstance().getInviteCode();
        if (!TextUtils.isEmpty(inviteCode)) {
            ToastUtil.success("已自动填充扫码邀请人的邀请码");
            mPeEditText.setText("" + inviteCode);
        }
    }

    private void initView() {
        showHeader("", true);
        setTitleNoLine();
        showHeaderRightText("邀请码是什么？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInviteCodeDialog();
            }
        });
        mTvConfirm.setEnabled(false);
        mPeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 8) {
                    KeyboardUtils.hideSoftInput(InviteCodeActivity.this);
                    // 校验邀请码有效性  设置 按钮是否可点击
                    rlInvitor.setVisibility(View.GONE);
                    getUserInfo(mPeEditText.getText().toString());
                } else {
                    mTvConfirm.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void initService() {
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        if (TextUtils.isEmpty(MyApplication.getInstance().getInviteCode())) {
//            getUserInviteCode();
        }
    }

    private void getUserInfo(String code) {
        ToastUtil.showLoading(this);
        APIManager.startRequest(mUserService.getUserInfoByCode(code), new BaseRequestListener<BaseBean<User>>(this) {

            @Override
            public void onSuccess(BaseBean<User> result) {
                super.onSuccess(result);
                if (result.getCode() == 0) {
                    mInvitor = result.getData();
                    mTvConfirm.setEnabled(true);
                    showInvitorInfo(result.getData());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showInvitorInfo(null);
                mTvConfirm.setEnabled(false);
            }
        });
    }

    private void getUserInviteCode() {
        APIManager.startRequest(mUserService.getMemberInviteCode(mPhoneNumber), new BaseRequestListener<HashMap<String, String>>(this) {
            @Override
            public void onSuccess(HashMap<String, String> result) {
                super.onSuccess(result);
                String key = "inviteCode";
                if (result.containsKey(key) && !TextUtils.isEmpty(key)) {
                    String inviteCode = result.get(key);
                    mPeEditText.setText(inviteCode);
                    getUserInfo(inviteCode);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void showInvitorInfo(User user) {
        rlInvitor.setVisibility(View.VISIBLE);
        boolean isShowVisitor = null != user;

        tvName.setVisibility(isShowVisitor ? View.VISIBLE : View.GONE);
        tvPhoneNumber.setVisibility(isShowVisitor ? View.VISIBLE : View.GONE);
        mSdvAvatar.setVisibility(isShowVisitor ? View.VISIBLE : View.GONE);
        tvInvitorEmpty.setVisibility(isShowVisitor ? View.GONE : View.VISIBLE);

        if (isShowVisitor) {
            tvName.setText(user.getNickNameLimit());
            tvPhoneNumber.setText(user.getSecretPhoneNumber());
            tvIdentity.setText("店主");
            FrescoUtil.setImage(mSdvAvatar, user.avatar);
        }
    }

    @OnClick(R.id.iv_scan)
    void scan() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    startActivityForResult(new Intent(InviteCodeActivity.this, DDMScanActivity.class), REQUEST_INVITE_CODE);
                } else {
                    Toast.makeText(InviteCodeActivity.this, "权限拒绝，无法使用，请打开权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE_CODE && resultCode == RESULT_OK) {
            if (null != data) {
                String result = data.getStringExtra(Constants.Extras.RESULT);
                DLog.i("scan result :" + result);
                if (null != result && result.contains("http")
                        && result.contains("inviteCode")
                        && checkFromDodomall(result)) {
                    Uri uri = Uri.parse(result);
                    String inviteCode = uri.getQueryParameter("inviteCode");
                    mPeEditText.setText(inviteCode);
                    getUserInfo(inviteCode);
                } else {
                    ToastUtil.error("未获取到邀请人信息");
                    showInvitorInfo(null);
                }
            }
        }
    }

    private boolean checkFromDodomall(String url) {
        return (url.contains("192.168.")
                || url.contains("61.162.59.74")
                || url.toLowerCase().contains("ddmall")
                || url.toLowerCase().contains("dodomall")
                || url.toLowerCase().contains("dianduoduo"));
    }


    void showInviteCodeDialog() {
        if (null == mDDMHintDialog) {
            mDDMHintDialog = new DDMHintDialog(this);
        }
        mDDMHintDialog.show();
    }


    @OnClick(R.id.tv_confirm)
    public void onClickConfirm() {
        new DDMDialog(this)
                .enableClose(false)
                .setTitle("再次确认邀请人信息")
                .setCustomContentView(getDialogContentView())
                .setNegativeButton("修改", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postFormData();
                    }
                }).show();

    }

    private View getDialogContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_invitor, null);
        TextView tvName = contentView.findViewById(R.id.tv_name);
        TextView tvPhoneNumber = contentView.findViewById(R.id.tv_phone_number);
        SimpleDraweeView sdvAvatar = contentView.findViewById(R.id.iv_avatar);

        tvName.setText(mInvitor.nickname);
        tvPhoneNumber.setText(mInvitor.getSecretPhoneNumber());
        FrescoUtil.setImageSmall(sdvAvatar, mInvitor.avatar);
        return contentView;
    }

    private void postFormData() {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("phone", mPhoneNumber);
//        params.put("checkNumber", mCaptcha);
//
//        // 微信登录，获取微信昵称时，截取前20个字作为用户昵称，修改昵称，最多输入20个字
//        String nickName = mWeChatLoginModel.nickName.length() > 20 ?
//                mWeChatLoginModel.nickName.substring(0, 20) : mWeChatLoginModel.nickName;
//        params.put("nickName", nickName);
//        params.put("invitationCode", mPeEditText.getText().toString());
//        params.put("headImage", mWeChatLoginModel.headImage);
//        params.put("wechatOpenId", mWeChatLoginModel.openid);
//        params.put("wechatUnionId", mWeChatLoginModel.unionid);
//        params.put("status", "9");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("inviteCode", mPeEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIManager.startRequest(mUserService.bindInviteCode(APIManager.getRequestBody(jsonObject.toString())),
                new BaseRequestListener<BaseBean<User>>(this) {
                    @Override
                    public void onSuccess(BaseBean<User> result) {
                        super.onSuccess(result);
                        if (result.getCode() == 0) {
                            UserService.loginSuccess(InviteCodeActivity.this, result.getData());
                        } else {
                            ToastUtil.error(result.getMessage());
                        }
                    }
                });
//        Observable<RequestResult<User>> registerAndLoginObservable = mUserService.registerNoPassword(params)
//                .flatMap(new Function<RequestResult<CheckNumber>, Observable<RequestResult<User>>>() {
//                    @Override
//                    public Observable<RequestResult<User>> apply(final RequestResult<CheckNumber> result) throws Exception {
//                        if (result.code != 0) {
//                            RequestResult<User> userRequestResult = new RequestResult<>();
//                            userRequestResult.code = result.code;
//                            userRequestResult.message = result.message;
//                            userRequestResult.data = new User();
//                            return Observable.just(userRequestResult);
//                        }
//                        return mUserService.login(mPhoneNumber, result.data.checkNumber);
//                    }
//                });
//
//
//        mUserService.registerNoPassword(params)
//                .map(new Function<RequestResult<CheckNumber>, Observable<RequestResult<User>>>() {
//                    @Override
//                    public Observable<RequestResult<User>> apply(RequestResult<CheckNumber> result) throws Exception {
//                        if (result.code != 0) {
//                            RequestResult<User> userRequestResult = new RequestResult<>();
//                            userRequestResult.code = result.code;
//                            userRequestResult.message = result.message;
//                            userRequestResult.data = new User();
//                            return Observable.just(userRequestResult);
//                        }
//                        return mUserService.login(mPhoneNumber, result.data.checkNumber);
//                    }
//                });
//
//
//        BaseSubscriber baseSubscriber = new BaseSubscriber<User>() {
//            @Override
//            public void onNext(User user) {
//                super.onNext(user);
//                UserService.loginSuccess(InviteCodeActivity.this, user);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                ToastUtil.error(e.getMessage());
//            }
//        };
//        baseSubscriber.setProgressDialog(getProgressDialog());
//        execute(registerAndLoginObservable, baseSubscriber);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess)) {
            //登录成功后清除扫码邀请码
            MyApplication.getInstance().setInviteCode("");
            finish();
        }
    }

}
