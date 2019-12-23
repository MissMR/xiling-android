package com.xiling.module.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.module.address.AddressListActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.Address;
import com.xiling.shared.bean.LotteryWinner;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Action;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.util.FrescoUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WinnerDetailsActivity extends BaseActivity {

    @BindView(R.id.prizeDetailsIv)
    SimpleDraweeView mPrizeDetailsIv;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.prizeDetailsMsg)
    TextView mPrizeDetailsMsg;
    @BindView(R.id.prizeDetailsTime)
    TextView mPrizeDetailsTime;
    @BindView(R.id.prizeDetailsState)
    TextView mPrizeDetailsState;
    @BindView(R.id.prizeDetailsAddressTv)
    TextView mPrizeDetailsAddressTv;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    private LotteryWinner mLotteryWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        getIntentData();
    }

    private void getIntentData() {
        mLotteryWinner = (LotteryWinner) getIntent().getSerializableExtra("data");
        FrescoUtil.setImage(mPrizeDetailsIv, mLotteryWinner.prizeImg);
        mTvTitle.setText(mLotteryWinner.prizeName);
        mPrizeDetailsMsg.setText(mLotteryWinner.intro);
        mPrizeDetailsTime.setText("获奖时间：" + mLotteryWinner.createDate);
        mPrizeDetailsState.setText("获奖状态：" + mLotteryWinner.statusStr);
        mPrizeDetailsAddressTv.setText(
                mLotteryWinner.getLotteryAddress()
        );
        if (mLotteryWinner.status == 1 && mLotteryWinner.prizeType == 2) {
            mTvSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        setTitle("奖品详情");
        setLeftBlack();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tvSubmit)
    public void onViewClicked() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra("action", Key.SELECT_ADDRESS);
        intent.putExtra("isLottery", true);
        intent.putExtra("drawId", mLotteryWinner.drawId);
        startActivityForResult(intent, Action.SELECT_ADDRESS);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMessage(LotteryWinner lotteryWinner) {
        mLotteryWinner = lotteryWinner;
        EventBus.getDefault().removeStickyEvent(lotteryWinner);

        FrescoUtil.setImage(mPrizeDetailsIv, lotteryWinner.prizeImg);
        mTvTitle.setText(lotteryWinner.prizeName);
        mPrizeDetailsMsg.setText(lotteryWinner.intro);
        mPrizeDetailsTime.setText("获奖时间：" + lotteryWinner.createDate);
        mPrizeDetailsState.setText("获奖状态：" + lotteryWinner.statusStr);
        mPrizeDetailsAddressTv.setText(
                lotteryWinner.getLotteryAddress()
        );
        if (lotteryWinner.status == 1 && lotteryWinner.prizeType == 2) {
            mTvSubmit.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectAddress(EventMessage message) {
        if (message.getEvent() == Event.acceptPrizeSuccess) {
            Address address = (Address) message.getData();
            mPrizeDetailsAddressTv.setText(address.getLotteryAddress());
            mPrizeDetailsState.setText("获奖状态：已领取");
            mTvSubmit.setVisibility(View.GONE);
        }
    }
}
