package com.xiling.ddmall.shared.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.common.AdvancedCountdownTimer;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountDown extends LinearLayout {

    private int mTextColor;
    private int mDotColor;
    private Drawable mBackground;
    private boolean isFrame = false;

    @BindView(R.id.hoursTv)
    protected TextView mHoursTv;
    @BindView(R.id.minutesTv)
    protected TextView mMinutesTv;
    @BindView(R.id.secondsTv)
    protected TextView mSecondsTv;
    @BindView(R.id.hourDotTv)
    protected TextView mHourDotTv;
    @BindView(R.id.minuteDotTv)
    protected TextView mMinuteDotTv;
    private AdvancedCountdownTimer mCountdownTimer;

    public CountDown(Context context) {
        super(context);
        mTextColor = getResources().getColor(R.color.text_black);
        mDotColor = getResources().getColor(R.color.text_black);
        initViews();
    }

    public CountDown(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDown);
        mTextColor = typedArray.getColor(R.styleable.CountDown_cd_color, getResources().getColor(R.color.ddm_yellow_product_detail));
        mDotColor = typedArray.getColor(R.styleable.CountDown_cd_dot_color, getResources().getColor(R.color.ddm_red));
        Drawable drawable = typedArray.getDrawable(R.styleable.CountDown_cd_background);
        if (drawable != null) {
            mBackground = drawable;
        } else {
            mBackground = ContextCompat.getDrawable(getContext(), R.drawable.bg_count_down_red);
        }
        isFrame = typedArray.getBoolean(R.styleable.CountDown_cd_frame, false);
        typedArray.recycle();

        initViews();
    }

    private void initViews() {
        View view;
        if (isFrame) {
            view = inflate(getContext(), R.layout.cmp_count_down_frame_layout, this);
        } else {
            view = inflate(getContext(), R.layout.cmp_count_down_layout, this);
        }
        ButterKnife.bind(this, view);

        renderStyle();

    }

    /**
     * 仅支持 白 红
     *
     * @param color Color.RED  Color.WHITE
     */
    public void setColorStyle(int color) {

        if (color == Color.WHITE) {
            mBackground = ContextCompat.getDrawable(getContext(), R.drawable.bg_count_down_white);
            mDotColor = ContextCompat.getColor(getContext(), R.color.white);
        } else {
            mBackground = ContextCompat.getDrawable(getContext(), R.drawable.bg_count_down_red);
            mDotColor = ContextCompat.getColor(getContext(), R.color.ddm_red);
        }

        renderStyle();

    }

    private void renderStyle() {
        mHoursTv.setTextColor(mTextColor);
        mMinutesTv.setTextColor(mTextColor);
        mSecondsTv.setTextColor(mTextColor);

        mHourDotTv.setTextColor(mDotColor);
        mMinuteDotTv.setTextColor(mDotColor);

        mHoursTv.setBackground(mBackground);
        mMinutesTv.setBackground(mBackground);
        mSecondsTv.setBackground(mBackground);
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

                if (hours > 99) {
                    hours = millisUntilFinished / 3600 % 24;

                    mHoursTv.setText(String.format(Locale.getDefault(), "%02d", days));
                    mMinutesTv.setText(String.format(Locale.getDefault(), "%02d", hours));
                    mHourDotTv.setText(" 天 ");
                    mMinuteDotTv.setText(" 小时 ");
                    mSecondsTv.setVisibility(GONE);
                } else {
                    mHoursTv.setText(String.format(Locale.getDefault(), "%02d", hours));
                    mMinutesTv.setText(String.format(Locale.getDefault(), "%02d", minutes));
                    mSecondsTv.setText(String.format(Locale.getDefault(), "%02d", seconds));
                }

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
