package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易密码
 */
public class TransactionPasswordActivity extends BaseActivity {
    INewUserService mUserService;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_set)
    TextView btnSet;

    int type = -1;// 0 - 设置交易密码  1- 修改交易密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_password);
        ButterKnife.bind(this);
        setTitle("交易密码");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        hasPassword();
    }

    /**
     * 校验有没有密码
     */
    private void hasPassword() {
        APIManager.startRequest(mUserService.hasPassword(), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (result) {
                    tvTitle.setText("已设置交易密码\n交易密码用于订单余额支付与提现功能使用");
                    btnSet.setText("修改交易密码");
                    type = 1;
                } else {
                    tvTitle.setText("您还没有设置交易密码～");
                    btnSet.setText("设置交易密码");
                    type = 0;
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);


            }
        });
    }

    @OnClick(R.id.btn_set)
    public void onViewClicked() {
        if (type == 0){
            //新建密码
            startActivity(new Intent(context, PassWordInputActivity.class));
        }else{
            //修改密码
            startActivity(new Intent(this,PasswordMobilePhoneActivity.class));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.UPDE_PASSWORD)) {
            hasPassword();
        }
    }

}
