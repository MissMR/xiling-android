package com.xiling.ddmall.module.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.store.StoreSettingActivity;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.constant.AppTypes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 所有提交状态的页面
 */
public class SubmitStatusActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ivStatus)
    ImageView mIvStatus;
    @BindView(R.id.tvStatus)
    TextView mTvStatus;
    @BindView(R.id.tvTips)
    TextView mTvTips;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.tvBigText)
    TextView mTvBigText;
    private int mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_status);
        ButterKnife.bind(this);
        mTvSubmit.setOnClickListener(this);
        showHeader();
        setLeftBlack();
        setTitle("实名认证");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMsg(MsgStatus msgStatus) {
        mAction = msgStatus.getAction();
        switch (mAction) {
            case Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_SUBMIT_SUCCESS:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                mTvStatus.setText("提交成功");
                mTvTips.setText("我们将在1~2个工作日内审核完成");
                mTvSubmit.setText("完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_succeed);
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_WAIT:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                mTvStatus.setText("审核中");
                mTvTips.setText("我们将在1~2个工作日内审核完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_wait);
                mTvSubmit.setVisibility(View.GONE);
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_FAIL:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                mTvStatus.setText("审核失败");
                mTvTips.setText("失败原因:" + msgStatus.getTips());
                mTvSubmit.setText("重新提交");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_fail);
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_CARD_SUBMIT_SUCCESS:
                setTitle("绑定银行卡");
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                mTvStatus.setText("提交成功");
                mTvTips.setText("我们将在1~2个工作日内审核完成");
                mTvSubmit.setText("完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_succeed);
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_CARD_WAIT:
                setTitle("绑定银行卡");
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                mTvStatus.setText("审核中");
                mTvTips.setText("我们将在1~2个工作日内审核完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_wait);
                mTvSubmit.setVisibility(View.GONE);
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_CARD_FAIL:
                setTitle("绑定银行卡");
                mTvStatus.setText("审核失败");
                mTvTips.setText("失败原因:" + msgStatus.getTips());
                mTvSubmit.setText("重新提交");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_fail);
                break;
            case AppTypes.TRANSFER.TRANSFER_MONEY_SUCESS:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                setTitle("转账成功");
                mTvStatus.setText("转账成功");
                mTvTips.setText(msgStatus.getTips());
                mTvSubmit.setText("完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_succeed);
                mTvBigText.setText(String.format("%.2f", msgStatus.getMoney()));
                mTvBigText.setVisibility(View.VISIBLE);
                break;
            case AppTypes.TRANSFER.TRANSFER_SCORE_SUCESS:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                setTitle("转赠成功");
                mTvStatus.setText("转赠成功");
                mTvTips.setText(msgStatus.getTips());
                mTvSubmit.setText("完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_succeed);
                mTvBigText.setText(String.format("%d积分", msgStatus.getMoney()));
                mTvBigText.setVisibility(View.VISIBLE);
                break;
            case AppTypes.TRANSFER.DEAL_SUCESS:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                setTitle("申请成功");
                mTvStatus.setText("申请成功");
                mTvTips.setText("审核完成后，将在5个工作日内打款到您的银行卡");
                mTvSubmit.setText("完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_wait);
                break;
            case AppTypes.STATUS.SUBMIT_SUCESS_STORE:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                setTitle("店铺配置");
                mTvStatus.setText("提交成功");
                mTvTips.setText("我们将在1-2个工作日内审核完成");
                mTvSubmit.setText("完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_succeed);
                break;
            case AppTypes.STATUS.WAIT_STORE:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                setTitle("店铺配置");
                mTvStatus.setText("审核中");
                mTvTips.setText("我们将在1-2个工作日内审核完成");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_wait);
                mTvSubmit.setVisibility(View.GONE);
                break;
            case AppTypes.STATUS.FAIL_STORE:
                EventBus.getDefault().removeStickyEvent(MsgStatus.class);
                setTitle("店铺配置");
                mTvStatus.setText("审核失败");
                mTvTips.setText("失败原因");
                mIvStatus.setImageResource(R.drawable.ic_user_auth_submit_fail);
                mTvSubmit.setText("重新提交");
                break;
                default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (mAction) {
            case Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_SUBMIT_SUCCESS:
                finish();
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_WAIT:
                finish();
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_FAIL:
                Intent intent = new Intent(this, AuthPhoneActivity.class);
                intent.putExtra(Config.INTENT_KEY_TYPE_NAME, Config.USER.INTENT_KEY_TYPE_AUTH_PHONE);
                intent.putExtra("isEdit", true);
                startActivity(intent);
                finish();
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_CARD_FAIL:
                Intent intent2 = new Intent(this, AuthPhoneActivity.class);
                intent2.putExtra(Config.INTENT_KEY_TYPE_NAME, Config.USER.INTENT_KEY_TYPE_AUTH_CARD);
                intent2.putExtra("isEdit", true);
                startActivity(intent2);
                finish();
                break;
            case AppTypes.STATUS.FAIL_STORE:
                startActivity(new Intent(this, StoreSettingActivity.class));
                finish();
                break;
            default:
                finish();
                break;
        }
    }
}
