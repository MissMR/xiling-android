package com.xiling.module.user;

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
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.tools.DLog;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.qrcode.DDMScanActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseBean;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.WeChatLoginModel;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.DDMDialog;
import com.xiling.shared.component.dialog.DDMHintDialog;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.UserService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ToastUtil;
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
                        UserManager.getInstance().loginSuccess(context,result);
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
