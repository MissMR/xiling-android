package com.xiling.module.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.module.auth.AuthPhoneActivity;
import com.xiling.module.auth.Config;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.DecimalEditText;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.constant.Event;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.StringUtil;
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
public class StepSecondActivity extends BaseActivity {

    @BindView(R.id.avatarIv)
    protected SimpleDraweeView mAvatarIv;
    @BindView(R.id.nameTv)
    protected TextView mNameTv;
    @BindView(R.id.phoneTv)
    protected TextView mPhoneTv;
    @BindView(R.id.moneyEt)
    protected DecimalEditText mMoneyEt;
    @BindView(R.id.remarkEt)
    protected EditText mRemarkEt;
    @BindView(R.id.tvTips)
    TextView mTvTips;
    @BindView(R.id.confirmBtn)
    TextView mConfirmBtn;

    private int mType;
    private User mPayee;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_step_second);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        EventBus.getDefault().register(this);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mType = intent.getIntExtra(Config.INTENT_KEY_TYPE_NAME, AppTypes.TRANSFER.MONEY);
        mPayee = (User) intent.getExtras().getSerializable("payee");
    }

    private void initView() {
        showHeader();
        setLeftBlack();
        switch (mType) {
            case AppTypes.TRANSFER.MONEY:
                setTitle("转帐到对方帐户");
                break;
            case AppTypes.TRANSFER.SCORE:
                setTitle("转到对方账户");
                mTvTips.setText("转增数额");
                mConfirmBtn.setText("转增");
                mMoneyEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                mMoneyEt.setHint("0");
                break;
        }
        FrescoUtil.setImageSmall(mAvatarIv, mPayee.avatar);
        mNameTv.setText(String.format("%s（%s）", mPayee.nickname, mPayee.userName));
        mPhoneTv.setText(mPayee.phone);
    }

    private void transferMoney() {
        String moneyStr = mMoneyEt.getText().toString();
        if (moneyStr.isEmpty()) {
            ToastUtil.error("请填写转帐金额");
            return;
        }
        if (!ValidateUtil.isMoney(moneyStr)) {
            ToastUtil.error("转帐金额格式不正确");
            return;
        }
        double money = Double.parseDouble(moneyStr);
        if (1 > money || money > 1000000) {
            ToastUtil.error("金额只能为 1 到 1000000 之间");
            return;
        }
        String remark = mRemarkEt.getText().toString();
        Intent intent = new Intent(this, StepThirdActivity.class);
        intent.putExtra("payee", mPayee);
        intent.putExtra("money", money);
        intent.putExtra("remark", remark);
        startActivity(intent);
    }

    private void transferScore() {
        String moneyStr = mMoneyEt.getText().toString();
        if (StringUtils.isEmpty(moneyStr)) {
            ToastUtil.error("请填写转账数额");
        } else {
            long money = Long.parseLong(moneyStr);
            if (money == 0) {
                ToastUtil.error("转赠金额不能为0");
            } else {
                Intent intent = new Intent(this, AuthPhoneActivity.class);
                intent.putExtra(Config.INTENT_KEY_TYPE_NAME,AppTypes.TRANSFER.SCORE);
                intent.putExtra("phone",mPayee.phone);
                intent.putExtra("transferScore",money);
                intent.putExtra("remark", mRemarkEt.getText().toString());
                String userName = StringUtil.maskName(mPayee.userName);
                intent.putExtra("statusTips", String.format("%s(%s)已收到你的转赠",mPayee.nickname,userName));
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.confirmBtn)
    protected void onSubmit() {
        switch (mType) {
            case AppTypes.TRANSFER.MONEY:
                transferMoney();
                break;
            case AppTypes.TRANSFER.SCORE:
                transferScore();
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void transferHandler(EventMessage message) {
        if (message.getEvent().equals(Event.transferSuccess)) {
            finish();
        }
    }
}
