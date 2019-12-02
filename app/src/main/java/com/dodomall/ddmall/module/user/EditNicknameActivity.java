package com.dodomall.ddmall.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.google.common.base.Strings;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chan on 2017/6/9.
 */

public class EditNicknameActivity extends BaseActivity {
    @BindView(R.id.nicknameEt)
    protected EditText mNicknameEt;
    private String mNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        ButterKnife.bind(this);
        mNickname = getIntent().getExtras().getString("nickname");
        if (mNickname == null) {
            ToastUtil.error("参数错误");
            finish();
        } else {
            mNicknameEt.setText(mNickname);
            mNicknameEt.setSelection(mNicknameEt.getText().length());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("修改昵称");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick(R.id.confirmBtn)
    protected void onConfirm() {
        final String nickName = mNicknameEt.getText().toString();
        if (Strings.isNullOrEmpty(nickName)) {
            ToastUtil.error("请输入新的昵称");
            return;
        }
        if (nickName.equals(mNickname)) {
            ToastUtil.error("昵称没有修改");
            return;
        }
        IUserService userService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(userService.editNickname(nickName), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                User loginUser = SessionUtil.getInstance().getLoginUser();
                loginUser.nickname = nickName;
                SessionUtil.getInstance().setLoginUser(loginUser);
                EventBus.getDefault().post(new EventMessage(Event.updateNickname, nickName));
                finish();
            }
        });
    }
}
