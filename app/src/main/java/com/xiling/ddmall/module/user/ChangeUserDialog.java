package com.xiling.ddmall.module.user;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.ValidateUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.user
 * @since 2017-03-10
 */
public class ChangeUserDialog extends Dialog {

    @BindView(R.id.phoneEt)
    protected EditText mPhoneEt;
    @BindView(R.id.errorTipsTv)
    protected TextView mErrorTipsTv;
    private IUserService mUserService;

    public ChangeUserDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ChangeUserDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_user_layout);

        ButterKnife.bind(this);

        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        initWindow();
        mPhoneEt.requestFocus();
    }

    private void initWindow() {
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.BottomDialogStyle);
            window.getDecorView().setPadding(0, 0, 0, 0);
            //获得window窗口的属性
            android.view.WindowManager.LayoutParams lp = window.getAttributes();
            //设置窗口宽度为充满全屏
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            //设置窗口高度为包裹内容
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //将设置好的属性set回去
            window.setAttributes(lp);
        }
    }

    @OnClick(R.id.cancelBtn)
    protected void onCancel() {
        dismiss();
    }

    @OnClick(R.id.okBtn)
    protected void onOk() {
        String phone = mPhoneEt.getText().toString();
        if (Strings.isNullOrEmpty(phone)) {
            ToastUtil.error("请输入手机号");
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            return;
        }
        getUserInfoByPhone(phone);
    }

    private void getUserInfoByPhone(String phone) {
        APIManager.startRequest(mUserService.getUserInfoByPhone(phone), new BaseRequestListener<User>() {
            @Override
            public void onSuccess(User result) {
                EventBus.getDefault().post(new EventMessage(Event.inviterUpdate, result));
                mErrorTipsTv.setVisibility(View.GONE);
                dismiss();
            }
        });
    }

    private void setErrorTips(String message) {
        mErrorTipsTv.setText(message);
        mErrorTipsTv.setVisibility(View.VISIBLE);
    }

}
