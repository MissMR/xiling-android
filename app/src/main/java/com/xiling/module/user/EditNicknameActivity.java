package com.xiling.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.xiling.R;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改昵称
 */
public class EditNicknameActivity extends BaseActivity {
    @BindView(R.id.nicknameEt)
    protected EditText mNicknameEt;
    @BindView(R.id.confirmBtn)
    TextView confirmBtn;
    private String mNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        ButterKnife.bind(this);
        setTitle("修改昵称");
        setLeftBlack();
        mNicknameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mNicknameEt.getText().toString().length() > 0) {
                    confirmBtn.setEnabled(true);
                } else {
                    confirmBtn.setEnabled(false);
                }

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
        INewUserService userService = ServiceManager.getInstance().createService(INewUserService.class);
        APIManager.startRequest(userService.editNickname(nickName), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                UserManager.getInstance().setNickName(nickName);
                EventBus.getDefault().post(new EventMessage(Event.UPDATE_NICK, nickName));
                finish();
            }
        });
    }


}
