package com.xiling.module.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.bean.InviterInfoBean;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.DDMHintDialog;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 邀请码界面  注册的最后流程
 */
public class InviteCodeActivity extends BaseActivity {

    public static final int REQUEST_INVITE_CODE = 1;

    @BindView(R.id.et_pin_captcha)
    PinEntryEditText mPeEditText;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_inviter_name)
    TextView tvInviterName;
    @BindView(R.id.tv_inviter_phone)
    TextView tvInviterPhone;
    @BindView(R.id.iv_inviter_level)
    ImageView ivInviterLevel;
    @BindView(R.id.ll_inviter_info)
    LinearLayout llInviterInfo;

    private INewUserService mUserService;
    private DDMHintDialog mDDMHintDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        ButterKnife.bind(this);

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
                if (charSequence.length() == 6) {
                    KeyboardUtils.hideSoftInput(InviteCodeActivity.this);
                    mTvConfirm.setEnabled(true);
                    //请求接口 根据邀请码获取用户信息
                    getAccountInfoForInvite(charSequence.toString());

                } else {
                    llInviterInfo.setVisibility(View.INVISIBLE);
                    mTvConfirm.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void initService() {
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
    }


    void showInviteCodeDialog() {
        if (null == mDDMHintDialog) {
            mDDMHintDialog = new DDMHintDialog(this);
        }
        mDDMHintDialog.show();
    }


    @OnClick(R.id.tv_confirm)
    public void onClickConfirm() {
        String code = mPeEditText.getText().toString().toUpperCase();
        bindInviteCode(code);
    }

    private void getAccountInfoForInvite(String inviteCode) {
        APIManager.startRequest(mUserService.getAccountInfoForInvite(inviteCode),
                new BaseRequestListener<InviterInfoBean>(this) {
                    @Override
                    public void onSuccess(InviterInfoBean result) {
                        super.onSuccess(result);
                        if (result != null) {
                            llInviterInfo.setVisibility(View.VISIBLE);
                            GlideUtils.loadHead(context, ivHead, result.getHeadImage());
                            tvInviterName.setText(result.getNickName());
                            tvInviterPhone.setText(result.getPhone());
                            switch (result.getRole()) {
                                case 0:
                                    ivInviterLevel.setImageResource(R.drawable.bg_home_register);
                                    break;
                                case 1:
                                    ivInviterLevel.setImageResource(R.drawable.bg_home_user);
                                    break;
                                case 2:
                                    ivInviterLevel.setImageResource(R.drawable.bg_home_vip);
                                    break;
                                case 3:
                                    ivInviterLevel.setImageResource(R.drawable.bg_home_back);
                                    break;
                            }

                        } else {
                            llInviterInfo.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        llInviterInfo.setVisibility(View.INVISIBLE);
                        //ToastUtil.error(e.getMessage());
                    }
                });
    }


    /**
     * 绑定邀请码
     */
    private void bindInviteCode(String code) {
        APIManager.startRequest(mUserService.bindInviteCode(code),
                new BaseRequestListener<NewUserBean>(this) {
                    @Override
                    public void onSuccess(NewUserBean result) {
                        super.onSuccess(result);
                        //登录成功
                        UserManager.getInstance().loginSuccess(context, result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.error(e.getMessage());
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.LOGIN_SUCCESS)) {
            //登录成功后清除扫码邀请码
            MyApplication.getInstance().setInviteCode("");
            finish();
        }
    }

}
