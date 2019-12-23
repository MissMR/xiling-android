package com.xiling.shared.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.common.AdvancedCountdownTimer;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountDownGray extends LinearLayout {

    @BindView(R.id.tvHours)
    TextView mTvHours;
    @BindView(R.id.tvMinutes)
    TextView mTvMinutes;
    @BindView(R.id.tvSeconds)
    TextView mTvSeconds;
    private AdvancedCountdownTimer mCountdownTimer;

    public CountDownGray(Context context) {
        super(context);
        initViews();
    }

    public CountDownGray(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        View view = inflate(getContext(), R.layout.cmp_count_down_gray_layout, this);
        ButterKnife.bind(this, view);
    }

    public void setTimeLeft(long time, @Nullable final OnFinishListener listener) {
        mCountdownTimer = new AdvancedCountdownTimer(time, 1000) {

            @Override
            public void onTick(long millisUntilFinished, int percent) {
                millisUntilFinished /= 1000;
                long days = millisUntilFinished / 3600 / 24;
                long hours = millisUntilFinished / 3600 % 24;
                long minutes = millisUntilFinished / 60 % 60;
                long seconds = millisUntilFinished % 60;
                if (days > 0) {
                    hours += days * 24;
                }
                mTvHours.setText(String.format(Locale.getDefault(), "%02d", hours));
                mTvMinutes.setText(String.format(Locale.getDefault(), "%02d", minutes));
                mTvSeconds.setText(String.format(Locale.getDefault(), "%02d", seconds));
            }

            @Override
            public void onFinish() {
                if (null != listener) {
                    listener.onFinish();
                }
            }
        };

        mCountdownTimer.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
            mCountdownTimer = null;
        }
        super.onDetachedFromWindow();
    }

    public interface OnFinishListener {
        void onFinish();
    }

}
