package com.xiling.ddui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.bean.UIEvent;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.manager.CSManager;
import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UserAuthHelper;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.address.AddressListActivity;
import com.xiling.module.auth.Config;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.user.AboutUsActivity;
import com.xiling.module.user.EditNicknameActivity;
import com.xiling.module.user.UserInfoActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.basic.BaseSubscriber;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PushManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.service.UserService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.CSUtils;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

public class UserSettingsActivity extends BaseActivity {

    private static final int PICTURE_PICK = 3;

    @BindView(R.id.avatarIv)
    SimpleDraweeView avatarIv;

    @BindView(R.id.nicknameValue)
    TextView nicknameValue;

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    // 账户与安全相关
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
    @BindView(R.id.tv_wechat_state)
    TextView mTvWechatState;
    @BindView(R.id.tv_wechat_qr_state)
    TextView mTvWechatQrCodeState;

    private User mUser;
    private IUserService mUserService;
    private UserAuthBean mUserAuthBean;

    private static final String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/data/"
            + BuildConfig.APPLICATION_ID + "/avatar.jpg";
    private Uri mPickAvatar = Uri.parse(IMAGE_FILE_LOCATION);

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        ButterKnife.bind(this);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        showHeader("账户设置", true);
        mTvVersion.setText("v " + BuildConfig.VERSION_NAME);
        getUserInfo();
        FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + "/data/com.dodomall.ddmall");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAuth();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 刷新
        mUser = SessionUtil.getInstance().getLoginUser();
        tvBindPhoneState.setText(mUser.getSecretPhoneNumber());
        getUserAuth();
    }

    private void getUserAuth() {
        APIManager.startRequest(mUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    void getUserInfo() {
        execute(mUserService.getUserInfo(), new BaseSubscriber<User>() {
            @Override
            public void onNext(@NonNull User user) {
                super.onNext(user);
                mUser = user;
                setUserData(user);
                SessionUtil.getInstance().setLoginUser(user);
            }
        });
    }

    /**
     * 设置用户数据
     *
     * @param user 用户数据实体类
     */
    void setUserData(User user) {
        mUser = user;
        setAvatar(user.avatar);
        setNickname(user.getNickNameLimit());
        tvBindPhoneState.setText(mUser.getSecretPhoneNumber());

        setWechatState(mUser.wechat);
        setWechatQrCodeState(!TextUtils.isEmpty(mUser.wechatCode));
    }

    /**
     * 设置头像
     *
     * @param url 头像地址
     */
    void setAvatar(String url) {
        FrescoUtil.setImageSmall(avatarIv, url);
    }

    /**
     * 设置昵称
     *
     * @param text 昵称文本
     */
    void setNickname(String text) {
        nicknameValue.setText(text);
    }

    /**
     * 设置我的微信号
     *
     * @param text 微信号
     */
    private void setWechatState(String text) {
        mTvWechatState.setText(TextUtils.isEmpty(text) ? "未填写" : text);
        setTextViewActive(mTvWechatState, TextUtils.isEmpty(text));
    }

    /**
     * 设置我的微信二维码
     *
     * @param isUpload 是否已经上传二维码
     */
    private void setWechatQrCodeState(boolean isUpload) {
        mTvWechatQrCodeState.setText(isUpload ? "已上传" : "未上传");
        setTextViewActive(mTvWechatQrCodeState, !isUpload);
    }

    private void setTextViewActive(TextView textView, boolean isActive) {
        textView.setTextColor(isActive ? getResources().getColor(R.color.ddm_red) : getResources().getColor(R.color.ddm_gray_dark));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            DLog.e("获取ImagePath:" + uris.get(0).getPath());
//            uploadImage(uris.get(0));
            startPhotoZoom(uris.get(0));
        } else if (requestCode == PICTURE_PICK && resultCode == RESULT_OK) {
            uploadImage(mPickAvatar);
        } else {
            getUserInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateNickname(EventMessage message) {
        if (message.getEvent() == Event.updateNickname) {
            String nickName = "" + message.getData();
            nicknameValue.setText(nickName);
            mUser.nickname = nickName;
        }
    }

    @OnClick({R.id.iconPanel, R.id.nicknamePanel, R.id.addressPanel, R.id.invitePanel,
            R.id.servicePanel, R.id.exitPanel, R.id.authSecurityPanel, R.id.rl_wechat, R.id.rl_wechat_qr_code, R.id.protocolPanel})
    void onRowPressed(View view) {
        switch (view.getId()) {
            case R.id.iconPanel:
                UploadManager.selectImageWithCapture(this);
                break;
            case R.id.nicknamePanel:
                editNickname();
                break;
            case R.id.addressPanel:
                jumpToAddressList();
                break;
            case R.id.invitePanel:
                jumpToInvite();
                break;
            case R.id.servicePanel:
                jumpToService();
                break;
            case R.id.aboutPanel:
                jumpToAboutUs();
                break;
            case R.id.exitPanel:
                logout();
                break;
            case R.id.authSecurityPanel:
                jumpToAuthSecurity();
                break;
            case R.id.rl_wechat:
                startActivityForResult(new Intent(this, EditWechatAccountActivity.class)
                        .putExtra(Constants.Extras.WECHAT, mUser != null ? mUser.wechat : ""), 1111);
                break;
            case R.id.rl_wechat_qr_code:
                startActivityForResult(new Intent(this, EditWechatQrCodeActivity.class)
                        .putExtra(Constants.Extras.WECHAT_QR_CODE, mUser != null ? mUser.wechatCode : ""), 1111);
                break;
            case R.id.protocolPanel:
                startActivity(new Intent(context, WebViewActivity.class)
                        .putExtra(Constants.Extras.WEB_URL, HtmlService.PRIVACY_PROTOCOL));
                break;
            default:
        }
    }

    @OnClick({R.id.rl_bind_phone, R.id.rl_auth, R.id.tv_bank_card, R.id.rl_trade_password, R.id.rl_security})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.rl_security) {
            //进入安全问题之前检查是否实名认证
            checkAuth(new UserSettingsActivity.CheckAuthListener() {
                @Override
                public void onPass(UserAuthBean result) {
                    SecurityQuestionActivity.jumpQuestion(context, result);
                }
            });
        } else if (view.getId() == R.id.rl_trade_password) {
            //进入交易密码之前检查是否实名认证
            checkAuth(new UserSettingsActivity.CheckAuthListener() {
                @Override
                public void onPass(UserAuthBean result) {
                    SecurityQuestionActivity.jumpPassword(context, result);
                }
            });
        } else if (view.getId() == R.id.tv_bank_card) {
            //进入绑定银行卡之前检查是否实名认证
            checkAuth(new UserSettingsActivity.CheckAuthListener() {
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
    public void checkAuth(final UserSettingsActivity.CheckAuthListener listener) {
        APIManager.startRequest(mUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>() {
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

    private void jumpToAuthSecurity() {
        if (null == mUser) {
            ToastUtil.error("等待加载数据");
            return;
        }
        Intent intent = new Intent(this, AuthSecurityActivity.class);
        intent.putExtra(Constants.Extras.USER, mUser);
        startActivity(intent);
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 该参数可以不设定用来规定裁剪区的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 该参数设定为你的imageView的大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        // 是否返回bitmap对象
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPickAvatar);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片的格式
        intent.putExtra("noFaceDetection", true); // 头像识别
        // 表示对目标应用临时授权该Uri所代表的文件，否则会报无法加载此图片的错误。
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PICTURE_PICK);
    }

    void uploadImage(Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                updateAvatar(result.url);
            }
        });
    }

    void updateAvatar(final String imageUrl) {
        APIManager.startRequest(mUserService.updateAvatar(imageUrl), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                EventBus.getDefault().post(new EventMessage(Event.updateAvatar, imageUrl));
                FrescoUtil.setImageSmall(avatarIv, imageUrl);
                User loginUser = SessionUtil.getInstance().getLoginUser();
                loginUser.avatar = imageUrl;
                SessionUtil.getInstance().setLoginUser(loginUser);
            }
        });
    }

    void editNickname() {
        //fix bugly id 2204
        if (mUser == null) {
            mUser = SessionUtil.getInstance().getLoginUser();
        }
        Intent intent = new Intent(this, EditNicknameActivity.class);
        intent.putExtra("nickname", mUser.nickname);
        startActivity(intent);
    }

    void jumpToAddressList() {
        startActivity(new Intent(this, AddressListActivity.class));
    }

    void jumpToInvite() {
        startActivity(new Intent(this, UserInfoActivity.class));
    }

    public void jumpToService() {
        CSUtils.start(this);
    }

    void jumpToAboutUs() {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    void logout() {
        APIManager.startRequest(mUserService.logout(), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                UserService.logout();
                UserManager.getInstance().loginOut(context);
                onBackPressed();
            }

        });
    }

    public static void goBack(Context context) {
        context.startActivity(new Intent(context, UserSettingsActivity.class));
    }

}
