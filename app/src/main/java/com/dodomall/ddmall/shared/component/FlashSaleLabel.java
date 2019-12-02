package com.dodomall.ddmall.shared.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;

import java.util.Date;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/28.
 * 商品详情里面的限时特卖标签
 */
public class FlashSaleLabel extends FrameLayout {
    TextView mTvStatus;
    TextView mTvTimeStatus;
    LinearLayout mLayoutDownTime;
    TextView mTvMoney;
    CountDown mCountDown;
    private CountDown.OnFinishListener mOnFinishListener;

    private boolean isSale = false;

    public FlashSaleLabel(Context context) {
        this(context, null);
    }

    public FlashSaleLabel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = inflate(context, R.layout.layout_flash_sale, this);
        mTvStatus = (TextView) inflate.findViewById(R.id.tvStatus);
        mTvTimeStatus = (TextView) inflate.findViewById(R.id.tvTimeStatus);
        mLayoutDownTime = (LinearLayout) inflate.findViewById(R.id.layoutDownTime);
        mTvMoney = (TextView) inflate.findViewById(R.id.tvMoney);
        mCountDown = (CountDown) inflate.findViewById(R.id.countDownView);
    }

    public void setData(String money, Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            isSale = true;
            return;
        }

        mTvMoney.setText(money);
        Date date = new Date();
        if (date.getTime() > endDate.getTime()) {
            mTvStatus.setText("已结束");
            mTvStatus.setVisibility(VISIBLE);
            mLayoutDownTime.setVisibility(GONE);
            onFinishInstant();
        } else if (date.getTime() < startDate.getTime()) {
            mTvStatus.setText("即将开抢");
            mTvTimeStatus.setText("距开始还剩:");
            startDownTime(startDate.getTime() - date.getTime());
            onFinishInstant();
        } else {
            mTvStatus.setText("");
            mTvTimeStatus.setText("距结束还剩:");
            isSale = true;
            startDownTime(endDate.getTime() - date.getTime());
        }
    }

    private void onFinishInstant(){
        if (null != getOnFinishListener()) {
            getOnFinishListener().onFinish();
        }
    }

    private void startDownTime(long time) {
        mCountDown.setTimeLeft(time, getOnFinishListener());
    }

    public boolean isSale() {
        return isSale;
    }

    public void setSale(boolean sale) {
        isSale = sale;
    }

    public void setOnFinishListener(CountDown.OnFinishListener onFinishListener) {
        mOnFinishListener = onFinishListener;
    }

    public CountDown.OnFinishListener getOnFinishListener() {
        return mOnFinishListener;
    }

    public String getStatusStr() {
        return mTvStatus.getText().toString();
    }
}
