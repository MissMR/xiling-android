package com.xiling.module.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.ConfirmUserDialog;
import com.xiling.shared.component.dialog.WJDialog;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.ValidateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.transfer
 * @since 2017-08-03
 */
public class StepFirstActivity extends BaseActivity {

    @BindView(R.id.phoneEt)
    protected EditText mPhoneEt;
    private IUserService mUserService;
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_step_first);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private void initView() {
        showHeader();
        setLeftBlack();
        switch (mType) {
            case AppTypes.TRANSFER.MONEY:
                setTitle("转帐");
                break;
            case AppTypes.TRANSFER.SCORE:
                setTitle("积分转增");
                break;
        }
    }

    private void getIntentData() {
        mType = getIntent().getIntExtra(Config.INTENT_KEY_TYPE_NAME, AppTypes.TRANSFER.MONEY);
    }

    /**
     * 转账
     *
     * @param user
     */
    private void showTransferMoneyDialog(final User user) {
        if (user.authStatus == 2) {
            ConfirmUserDialog confirmUserDialog = new ConfirmUserDialog(StepFirstActivity.this, user);
            confirmUserDialog.setOnConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transNext(user);
                }
            });
            confirmUserDialog.show();
        } else {
            WJDialog dialog = new WJDialog(StepFirstActivity.this);
            dialog.show();
            dialog.setContentText("对方未进行账户认证\n无法转帐");
        }
    }


    /**
     * 转增积分
     *
     * @param user
     */
    private void showTransferScoreDialog(final User user) {
        ConfirmUserDialog confirmUserDialog = new ConfirmUserDialog(StepFirstActivity.this, user, AppTypes.TRANSFER.SCORE);
        confirmUserDialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transNext(user);
            }
        });
        confirmUserDialog.show();
    }

    private void transNext(User user) {
        Intent intent = new Intent(StepFirstActivity.this, StepSecondActivity.class);
        intent.putExtra("payee", user);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, mType);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick(R.id.confirmBtn)
    protected void onSubmit() {
        String phone = mPhoneEt.getText().toString();
        if (phone.isEmpty()) {
            ToastUtil.error("请输入手机号");
            return;
        }
        if (!ValidateUtil.isPhone(phone)) {
            ToastUtil.error("手机号格式不正确");
            return;
        }
        APIManager.startRequest(mUserService.getUserInfoByPhone(phone), new BaseRequestListener<User>(this) {

            @Override
            public void onSuccess(final User user) {
                switch (mType) {
                    case AppTypes.TRANSFER.MONEY:
                        showTransferMoneyDialog(user);
                        break;
                    case AppTypes.TRANSFER.SCORE:
                        showTransferScoreDialog(user);
                        break;
                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void transferHandler(EventMessage message) {
        if (message.getEvent().equals(Event.transferSuccess)) {
            finish();
        }
    }
}
