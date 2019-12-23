package com.xiling.module.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.module.address.AddressListActivity;
import com.xiling.module.auth.Config;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.user.model.UpMemberModel;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.basic.BaseSubscriber;
import com.xiling.shared.bean.HasPasswordModel;
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
import com.xiling.shared.util.StringUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.invitationTv)
    TextView mInvitationTv;
    @BindView(R.id.avatarLayout)
    LinearLayout mAvatarLayout;
    @BindView(R.id.nicknameLayout)
    LinearLayout mNicknameLayout;
    @BindView(R.id.phoneLayout)
    LinearLayout mPhoneLayout;
    @BindView(R.id.tvPasswordTips)
    TextView mTvPasswordTips;
    @BindView(R.id.passwordLayout)
    LinearLayout mPasswordLayout;
    @BindView(R.id.invitationLayout)
    LinearLayout mInvitationLayout;
    @BindView(R.id.addressLayout)
    LinearLayout mAddressLayout;
    @BindView(R.id.layoutCS)
    LinearLayout mLayoutCS;
    @BindView(R.id.aboutUsLayout)
    LinearLayout mAboutUsLayout;
    @BindView(R.id.logoutBtn)
    TextView mLogoutBtn;
    @BindView(R.id.tvPassword)
    TextView mTvPassword;
    private IUserService mUserService;

    @BindView(R.id.avatarIv)
    protected SimpleDraweeView mAvatarIv;

    @BindView(R.id.nicknameTv)
    protected TextView mNicknameTv;

    @BindView(R.id.phoneTv)
    protected TextView mPhoneTv;

    private User mUser;
    private boolean isNoUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);

        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        mUserService = ServiceManager.getInstance().createService(IUserService.class);

        execute(mUserService.getUserInfo(), new BaseSubscriber<User>() {
            @Override
            public void onNext(@NonNull User user) {
                super.onNext(user);
                mUser = user;
                setUserData(user);
                SessionUtil.getInstance().setLoginUser(user);
            }
        });
        BaseSubscriber<UpMemberModel> baseSubscriber = new BaseSubscriber<UpMemberModel>() {
            @Override
            public void onNext(UpMemberModel result) {
                super.onNext(result);
                mInvitationTv.setText(result.nickName);
            }
        };
        baseSubscriber.setProgressDialog(getProgressDialog());
        execute(mUserService.getUpMember(), baseSubscriber);


        APIManager.startRequest(mUserService.hasPassowrd(), new BaseRequestListener<HasPasswordModel>(this) {
            @Override
            public void onSuccess(HasPasswordModel model) {
                super.onSuccess(model);
                if (model.status) {
                    mTvPasswordTips.setText("");
                    mTvPassword.setText("修改密码");
                } else {
                    mTvPassword.setText("设置密码");
                    mTvPasswordTips.setText("未设置");
                }
            }
        });
    }

    private void setUserData(User user) {
        FrescoUtil.setImageSmall(mAvatarIv, user.avatar);
        mNicknameTv.setText(user.nickname);
        mPhoneTv.setText(StringUtil.maskPhone(user.phone));
        mUser = user;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        showHeader();
        setTitle("我的资料");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick(R.id.logoutBtn)
    protected void logout() {
        APIManager.startRequest(mUserService.logout(), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);

                UserService.logout();
                EventBus.getDefault().post(new EventMessage(Event.logout));
                EventBus.getDefault().post(new EventMessage(Event.viewHome));
                PushManager.setJPushInfo(ProfileActivity.this,null);
                ProfileActivity.this.finish();
            }

        });
    }

    @OnClick(R.id.avatarLayout)
    protected void changeAvatar() {
        UploadManager.selectImage(this);
    }

    private void uploadImage(Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                updateAvatar(result.url);
            }
        });
    }

    private void updateAvatar(final String imageUrl) {
        APIManager.startRequest(mUserService.updateAvatar(imageUrl), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                EventBus.getDefault().post(new EventMessage(Event.updateAvatar, imageUrl));
                FrescoUtil.setImageSmall(mAvatarIv, imageUrl);
                User loginUser = SessionUtil.getInstance().getLoginUser();
                loginUser.avatar = imageUrl;
                SessionUtil.getInstance().setLoginUser(loginUser);
            }
        });
    }

    @OnClick(R.id.nicknameLayout)
    protected void editNickname() {
        Intent intent = new Intent(this, EditNicknameActivity.class);
        intent.putExtra("nickname", mUser.nickname);
        startActivity(intent);
    }

    @OnClick(R.id.phoneLayout)
    protected void editPhone() {
        if (StringUtils.isEmpty(mUser.phone)) {
            startActivity(new Intent(this, BindPhoneActivity.class));
        } else {
            UserService.checkHasPassword(this, new UserService.HasPasswordListener() {
                @Override
                public void onHasPassword() {
                    Intent intent = new Intent(ProfileActivity.this, EditPhoneActivity.class);
                    intent.putExtra("phone", mUser.phone);
                    startActivity(intent);
                }
            });
        }
    }

    @OnClick(R.id.passwordLayout)
    protected void editPassword() {
        APIManager.startRequest(mUserService.hasPassowrd(), new BaseRequestListener<HasPasswordModel>(this) {
            @Override
            public void onSuccess(HasPasswordModel model) {
                super.onSuccess(model);
                if (model.status) {
                    startActivity(new Intent(ProfileActivity.this, EditPasswordActivity.class));
                } else {
                    startActivity(new Intent(ProfileActivity.this,SetPasswordActivity.class));
                }
            }
        });
    }

    @OnClick(R.id.invitationLayout)
    protected void onClickInvitation() {
        if (isNoUp) {
            ToastUtils.showShortToast("您没有邀请人");
            return;
        }
        startActivity(new Intent(this, UserInfoActivity.class));
    }

    @OnClick(R.id.aboutUsLayout)
    protected void onClickAboutUs() {
        startActivity(new Intent(this, AboutUsActivity.class));
    }


    @OnClick(R.id.addressLayout)
    protected void viewAddressList() {
        startActivity(new Intent(this, AddressListActivity.class));
    }

    @OnClick(R.id.layoutCS)
    public void goCS() {
        CSUtils.start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            LogUtils.e("拿到图片" + uris.get(0).getPath());
            uploadImage(uris.get(0));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateNickname(EventMessage message) {
        if (message.getEvent() == Event.updateNickname) {
            mNicknameTv.setText(String.valueOf(message.getData()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatusMsg(MsgStatus message) {
        switch (message.getAction()) {
            case MsgStatus.ACTION_EDIT_PHONE:
                finish();
                break;
            default:
                break;
        }
    }
}
